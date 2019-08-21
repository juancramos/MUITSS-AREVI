package com.upv.muitss.arevi.logic.webrtc.implementations;

import android.content.Intent;
import android.media.projection.MediaProjection;
import android.util.Log;
import android.view.View;

import com.upv.muitss.arevi.ArActivity;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.logic.webrtc.models.IceServer;
import com.upv.muitss.arevi.helpers.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.ScreenCapturerAndroid;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.upv.muitss.arevi.logic.webrtc.implementations.SignallingClient.*;

public class WebRTCModule implements SignalingInterface {

    private static WeakReference<ArActivity> owner;
    private final String TAG = this.getClass().getCanonicalName();

    private boolean gotUserMedia;

    private EglBase rootEglBase;
    private PeerConnectionFactory peerConnectionFactory;
    private VideoTrack localVideoTrack;
    private VideoCapturer videoCapturer;
    private SurfaceViewRenderer remoteVideoView;
    private PeerConnection localPeer;
    private List<PeerConnection.IceServer> peerIceServers = new ArrayList<>();

    public WebRTCModule (WeakReference<ArActivity> pOwner) {
        owner = pOwner;

        rootEglBase = EglBase.create();
    }

    public void InitRTC(Intent data){

        //Now create a VideoCapturer instance. Callback methods are there if you want to do something! Duh!
        videoCapturer = new ScreenCapturerAndroid(data, new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                Log.e(TAG, "user has revoked permissions");
            }
        });

    }

    /**
     * This method will be called directly by the app when it is the initiator and has got the local media
     * or when the remote peer sends a message through socket that it is ready to transmit AV data
     */
    @Override
    public void onTryToStart() {
        owner.get().runOnUiThread(() -> {
            if (!SignallingClient.getInstance().isStarted && localVideoTrack != null && SignallingClient.getInstance().isChannelReady) {
                createPeerConnection();
                SignallingClient.getInstance().isStarted = true;
                if (SignallingClient.getInstance().isInitiator) {
                    doCall();
                }
            }
        });
    }


    /**
     * Creating the local peerconnection instance
     */
    private void createPeerConnection() {
        PeerConnection.RTCConfiguration rtcConfig =
                new PeerConnection.RTCConfiguration(peerIceServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
        localPeer = peerConnectionFactory.createPeerConnection(rtcConfig, new CustomPeerConnectionObserver("localPeerCreation") {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                onIceCandidateReceived(iceCandidate);
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                Utils.showToast(owner.get() ,"Received Remote stream");
                super.onAddStream(mediaStream);
                gotRemoteStream(mediaStream);
            }
        });

        addStreamToLocalPeer();
    }

    /**
     * Adding the stream to the localpeer
     */
    private void addStreamToLocalPeer() {
        //creating local mediastream
        MediaStream stream = peerConnectionFactory.createLocalMediaStream("102");
        stream.addTrack(localVideoTrack);
        localPeer.addStream(stream);
    }

    /**
     * This method is called when the app is initiator - We generate the offer and send it over through socket
     * to remote peer
     */
    private void doCall() {
        MediaConstraints sdpConstraints = new MediaConstraints();
        sdpConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveVideo", "true"));
        localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"), sessionDescription);
                Log.d("onCreateSuccess", "SignallingClient emit ");
                SignallingClient.getInstance().emitMessage(sessionDescription);
            }
        }, sdpConstraints);
    }

    /**
     * Received remote peer's media stream. we will get the first video track and render it
     */
    private void gotRemoteStream(MediaStream stream) {
        //we have remote video stream. add to the renderer.
        final VideoTrack videoTrack = stream.videoTracks.get(0);
        owner.get().runOnUiThread(() -> {
            try {
                remoteVideoView.setVisibility(View.VISIBLE);
                videoTrack.addSink(remoteVideoView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Received local ice candidate. Send it to remote peer through signalling for negotiation
     */
    private void onIceCandidateReceived(IceCandidate iceCandidate) {
        //we have received ice candidate. We can set it to the other peer.
        SignallingClient.getInstance().emitIceCandidate(iceCandidate);
    }

    /**
     * SignallingCallback - called when the room is created - i.e. you are the initiator
     */
    @Override
    public void onCreatedRoom() {
        Utils.showToast(owner.get() ,"You created the room " + gotUserMedia);
        if (gotUserMedia) {
            SignallingClient.getInstance().emitMessage("got user media");
        }
    }

    /**
     * SignallingCallback - called when you join the room - you are a participant
     */
    @Override
    public void onJoinedRoom() {
        Utils.showToast(owner.get() ,"You joined the room " + gotUserMedia);
        if (gotUserMedia) {
            SignallingClient.getInstance().emitMessage("got user media");
        }
    }

    @Override
    public void onNewPeerJoined() {
        Utils.showToast(owner.get() ,"Remote Peer Joined");
    }

    @Override
    public void onRemoteHangUp(String msg) {
        Utils.showToast(owner.get() ,"Remote Peer hungup");
        owner.get().runOnUiThread(this::hangup);
    }

    private void hangup() {
        try {
            localPeer.close();
            localPeer = null;
            SignallingClient.getInstance().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * SignallingCallback - Called when remote peer sends offer
     */
    @Override
    public void onOfferReceived(final JSONObject data) {
        Utils.showToast(owner.get() ,"Received Offer");
        owner.get().runOnUiThread(() -> {
            if (!SignallingClient.getInstance().isInitiator && !SignallingClient.getInstance().isStarted) {
                onTryToStart();
            }

            try {
                localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
                doAnswer();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void doAnswer() {
        localPeer.createAnswer(new CustomSdpObserver("localCreateAns") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocal"), sessionDescription);
                SignallingClient.getInstance().emitMessage(sessionDescription);
            }
        }, new MediaConstraints());
    }

    /**
     * SignallingCallback - Called when remote peer sends answer to your offer
     */

    @Override
    public void onAnswerReceived(JSONObject data) {
        Utils.showToast(owner.get() ,"Received Answer");
        try {
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.fromCanonicalForm(data.getString("type").toLowerCase()), data.getString("sdp")));
            //updateVideoViews(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remote IceCandidate received
     */
    @Override
    public void onIceCandidateReceived(JSONObject data) {
        try {
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"), data.getString("candidate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    public void MountLocalView(SurfaceViewRenderer videoView) {
//
//        videoView.setMirror(false);
//        videoView.init(rootEglBase.getEglBaseContext(), null);
//        videoView.setZOrderMediaOverlay(true);
//
//        localVideoView = videoView;
//
//        localVideoTrack.addSink(localVideoView);
//    }

    public void MountRemoteView(SurfaceViewRenderer videoView) {
        //Initialize PeerConnectionFactory globals.
        //Params are context, initAudio,initVideo and videoCodecHwAcceleration
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                .builder(owner.get())
                .createInitializationOptions());
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory =
                new DefaultVideoEncoderFactory(rootEglBase.getEglBaseContext(), true, true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory =
                new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());
        peerConnectionFactory  = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();

        getIceServers();
        SignallingClient.getInstance().init(this, Utils.getResourceString(R.string.signaling_server_url));


        //Create MediaConstraints - Will be useful for specifying video and audio constraints. More on this later!
        // MediaConstraints constraints = new MediaConstraints();

        //Create a VideoSource instance
        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource);

        //create an AudioSource instance
//        AudioSource audioSource = peerConnectionFactory.createAudioSource(constraints);
//        localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource);

        //we will start capturing the video from the camera
        //params are width,height and fps
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
        videoCapturer.initialize(surfaceTextureHelper, owner.get().getApplicationContext(), videoSource.getCapturerObserver());
        videoCapturer.startCapture(1000, 1000, 30);


        gotUserMedia = true;
        if (SignallingClient.getInstance().isInitiator) {
            onTryToStart();
        }

        videoView.setMirror(false);
        videoView.init(rootEglBase.getEglBaseContext(), null);
        videoView.setZOrderMediaOverlay(false);

        remoteVideoView = videoView;
    }

    private void getIceServers() {
        List<IceServer> iceServers = Utils.getIceServerData(owner.get());
        for (IceServer iceServer : iceServers) {
            if (iceServer.credential == null) {
                PeerConnection.IceServer peerIceServer = PeerConnection.IceServer.builder(iceServer.url).createIceServer();
                peerIceServers.add(peerIceServer);
            } else {
                PeerConnection.IceServer peerIceServer = PeerConnection.IceServer.builder(iceServer.url)
                        .setUsername(iceServer.username)
                        .setPassword(iceServer.credential)
                        .createIceServer();
                peerIceServers.add(peerIceServer);
            }
        }
    }
}


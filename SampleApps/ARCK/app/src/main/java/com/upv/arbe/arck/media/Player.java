package com.upv.arbe.arck.media;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.upv.arbe.arck.MainActivity;

import java.lang.ref.WeakReference;

public class Player {

    // The color to filter out of the video.
    public static final Color CHROMA_KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);

    private final WeakReference<MainActivity> owner;
    private static final String TAG = "PlayerLoader";

    @Nullable
    private ModelRenderable videoRenderable;

    // Controls the height of the video in world space.
    private static final float VIDEO_HEIGHT_METERS = 0.85f;

    private MediaPlayer mediaPlayer;
    private ExternalTexture texture;

    // Set the scale of the node so that the aspect ratio of the video is correct.
    private float videoWidth;
    private float videoHeight;

    public Player(WeakReference<MainActivity> pOwner) {
        owner = pOwner;
        // Create an ExternalTexture for displaying the contents of the video.
        texture = new ExternalTexture();
    }

    public ExternalTexture getTexture() {
        return texture;
    }

    public void setVideoRenderable(@Nullable ModelRenderable pVideoRenderable) {
        videoRenderable = pVideoRenderable;
    }

    public void startPlayer(HitResult hitResult, ArFragment arFragment){
        if (owner.get() == null) {
            Log.d(TAG, "Activity is null.  Cannot load model.");
            return;
        }

        if (videoRenderable == null) {
            return;
        }

        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create a node to render the video and add it to the anchor.
        Node videoNode = new Node();
        videoNode.setParent(anchorNode);

        if (mediaPlayer == null) {
            // Create an Android MediaPlayer to capture the video on the external texture's surface.
            // IMPORTANT! URL has to be https
            String url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"; // your URL here
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                //mediaPlayer.prepare(); // might take long! (for buffering, etc)
            }
            catch (Exception e){
                e.printStackTrace();
            }

            mediaPlayer.setOnErrorListener((mp,what,extra)->{
                mp.reset();
                return false;
            });

            mediaPlayer.setOnPreparedListener((mp)->{

                mp.setSurface(texture.getSurface());

                // Set the scale of the node so that the aspect ratio of the video is correct.
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();

                setScale(videoNode);

                // Start playing the video when the first node is placed.
                if (!mp.isPlaying()) {
                    mp.start();
                }
            });

        } else {
            setScale(videoNode);
            videoNode.setRenderable(videoRenderable);
        }
    }

    private void setScale(Node videoNode) {
        videoNode.setLocalScale(
                new Vector3(
                        VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));

        // Wait to set the renderable until the first frame of the  video becomes available.
        // This prevents the renderable from briefly appearing as a black quad before the video
        // plays.
        texture
                .getSurfaceTexture()
                .setOnFrameAvailableListener(
                        (SurfaceTexture surfaceTexture) -> {
                            videoNode.setRenderable(videoRenderable);
                            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                        });
    }

    public void destroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (texture != null) {
            texture = null;
        }
    }
}

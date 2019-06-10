package com.upv.arbe.arck;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class Player {

    private MediaPlayer mediaPlayer;

    // Controls the height of the video in world space.
    private static final float VIDEO_HEIGHT_METERS = 0.85f;

    private static ExternalTexture texture;


    public Player(Context context, ExternalTexture pTexture) {
        texture = pTexture;

        // Create an Android MediaPlayer to capture the video on the external texture's surface.
        mediaPlayer = MediaPlayer.create(context, R.raw.lion_chroma);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);
    }

    public void startPlayer(HitResult hitResult, ArFragment arFragment, ModelRenderable videoRenderable){
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

        // Set the scale of the node so that the aspect ratio of the video is correct.
        float videoWidth = mediaPlayer.getVideoWidth();
        float videoHeight = mediaPlayer.getVideoHeight();
        videoNode.setLocalScale(
                new Vector3(
                        VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));

        // Start playing the video when the first node is placed.
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();

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
        } else {
            videoNode.setRenderable(videoRenderable);
        }
    }

    public void destroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

package com.upv.arbe.aovb;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;

public class Player {
    @Nullable
    private ModelRenderable videoRenderable;
    private ExternalTexture texture;
    private MediaPlayer mediaPlayer;
    // The color to filter out of the video.
    private static final Color CHROMA_KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);
    // Controls the height of the video in world space.
    private static final float VIDEO_HEIGHT_METERS = 0.85f;

    public Player(Context context, ModelRenderable renderable) {
        videoRenderable = renderable;
        videoRenderable.getMaterial().setExternalTexture("videoTexture", texture);
        videoRenderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);

        // Create an ExternalTexture for displaying the contents of the video.
        texture = new ExternalTexture();
        try {
            // Create an Android MediaPlayer to capture the video on the external texture's surface.
            mediaPlayer = MediaPlayer.create(context, R.raw.lion_chroma);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);
    }

    public void startPlayer(final AnchorNode parent) {
        if (videoRenderable == null) {
            return;
        }

        Node videoNode = new Node();
        videoNode.setParent(parent);

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
            texture.getSurfaceTexture()
                    .setOnFrameAvailableListener(
                            (SurfaceTexture surfaceTexture) -> {
                                videoNode.setRenderable(videoRenderable);
                                texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                            });
        } else {
            videoNode.setRenderable(videoRenderable);
        }
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

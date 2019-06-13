package com.upv.arbe.arck.helpers;

import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.ar.core.HitResult;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.upv.arbe.arck.MainActivity;
import com.upv.arbe.arck.media.Player;
import com.upv.arbe.arck.model.Handler;

import java.lang.ref.WeakReference;

public class ModelLoader {

    private final WeakReference<MainActivity> owner;
    private static final String TAG = "ModelLoader";

    private Player player;
    private Handler handler;

    public ModelLoader(WeakReference<MainActivity> pOwner) {
        owner = pOwner;
    }

    public void loadModel(HitResult hitResult, String path) {
        if (owner.get() == null) {
            Log.d(TAG, "Activity is null.  Cannot load model.");
            return;
        }

        if (handler == null) {
            handler = new Handler(new WeakReference<>(owner.get()));
        }

        ModelRenderable.builder()
                .setSource(owner.get(), Uri.parse(path))
                .build()
                .thenAccept(
                        renderable -> {
                            handler.setRenderable(renderable);

                            handler.addNodeToScene(hitResult, owner.get().getArFragment());
                        })
                .exceptionally(
                        throwable -> {
                            handler.onException(throwable);
                            return null;
                        });
    }

    public void loadMediaModel(HitResult hitResult, String path) {
        if (owner.get() == null) {
            Log.d(TAG, "Activity is null.  Cannot load model.");
            return;
        }

        if (player == null){
            player = new Player(new WeakReference<>(owner.get()));
        }

        if (player.getTexture() == null) {
            Log.d(TAG, "player is null.  Cannot load model.");
            return;
        }

        // Create a renderable with a material that has a parameter of type 'samplerExternal' so that
        // it can display an ExternalTexture. The material also has an implementation of a chroma key
        // filter.
        ModelRenderable.builder()
                .setSource(owner.get(), Uri.parse(path))
                .build()
                .thenAccept(
                        renderable -> {
                            renderable.setShadowReceiver(false);
                            renderable.setShadowCaster(false);
                            renderable.getMaterial().setExternalTexture("videoTexture", player.getTexture());
                            // renderable.getMaterial().setFloat4("keyColor", Player.CHROMA_KEY_COLOR);

                            player.setVideoRenderable(renderable);

                            player.startPlayer(hitResult, owner.get().getArFragment());
                        })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(owner.get(), "Unable to load video renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }

    public void destroy() {
        if (player != null) {
            player.destroy();
        }
    }
}

package com.upv.arbe.arck.model;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.upv.arbe.arck.MainActivity;

import java.lang.ref.WeakReference;

public class Handler {
    private final WeakReference<MainActivity> owner;
    private static final String TAG = "HandleLoader";

    @Nullable
    private ModelRenderable renderable;

    public Handler(WeakReference<MainActivity> pOwner) {
        owner = pOwner;
    }

    public void setRenderable(@Nullable ModelRenderable pRenderable) {
        renderable = pRenderable;
    }

    private void togglePauseAndResume(ModelAnimator animator) {
        if (animator.isPaused()) {
            animator.resume();
        } else if (animator.isStarted()) {
            animator.pause();
        } else {
            animator.start();
        }
    }

    private void startAnimation(TransformableNode node, ModelRenderable renderable){
        if(renderable==null || renderable.getAnimationDataCount() == 0) {
            return;
        }
        for(int i = 0;i < renderable.getAnimationDataCount();i++){
            // AnimationData animationData = renderable.getAnimationData(i);
            renderable.getAnimationData(i);
        }
        ModelAnimator animator = new ModelAnimator(renderable.getAnimationData(0), renderable);
        animator.start();
        node.setOnTapListener(
                (hitTestResult, motionEvent) -> togglePauseAndResume(animator));
    }

    public void addNodeToScene(HitResult hitResult, ArFragment arFragment) {
        if (renderable == null) {
            return;
        }

        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        node.select();

        startAnimation(node, renderable);

        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }

    public void onException(Throwable throwable){
        if (owner.get() == null) {
            Log.d(TAG, "Activity is null.  Cannot load model.");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(owner.get());
        builder.setMessage(throwable.getMessage()).setTitle("ÀRBE error!");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

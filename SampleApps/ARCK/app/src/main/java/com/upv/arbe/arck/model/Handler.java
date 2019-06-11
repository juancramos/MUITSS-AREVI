package com.upv.arbe.arck.model;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
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

    public void addNodeToScene(HitResult hitResult, ArFragment arFragment) {
        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        node.select();

        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }

    public void onException(Throwable throwable){
        AlertDialog.Builder builder = new AlertDialog.Builder(owner.get());
        builder.setMessage(throwable.getMessage())
                .setTitle("Codelab error!");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

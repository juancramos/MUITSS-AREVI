package com.upv.muitss.arevi.views;

import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.upv.muitss.arevi.ArActivity;
import com.upv.muitss.arevi.entities.Work;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class CustomTransformableNode extends TransformableNode {

    private static WeakReference<ArView> owner;

    CustomTransformableNode(WeakReference<ArView> pOwner) {
        super(pOwner.get().getTransformationSystem());
        owner = pOwner;
    }

    @Override
    public boolean onTouchEvent (HitTestResult hitTestResult, MotionEvent motionEvent){
        //We are only interested in the ACTION_UP events - anything else just return
        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }

        Node hitNode = hitTestResult.getNode();

        // Check for touching a Sceneform node
        if (hitNode != null) {
            owner.get().currentScore.scaleV = hitNode.getLocalScale().x;
            owner.get().currentScore.scaleV1 = hitNode.getLocalScale().y;
            owner.get().currentScore.scaleV2 = hitNode.getLocalScale().z;

            Utils.showToast(Objects.requireNonNull(owner.get().getActivity()), "We've hit Andy!!");

            AnchorNode aNode = null;
            while (aNode == null){
                Node parent = hitNode.getParent();
                if (parent == null) {
                    return false;
                }
                else {
                    parent.removeChild(hitNode);
                    hitNode = parent;
                    if (hitNode instanceof AnchorNode){
                        aNode = (AnchorNode) hitNode;
                    }
                }
            }

            Anchor a = aNode.getAnchor();
            Frame frame = owner.get().getArSceneView().getArFrame();

            if (a == null || frame == null) return false;

            Pose cameraPose = frame.getCamera().getPose();
            Pose objectPose = a.getPose();

            float dx = objectPose.tx() - cameraPose.tx();
            float dy = objectPose.ty() - cameraPose.ty();
            float dz = objectPose.tz() - cameraPose.tz();

            ///Compute the straight-line distance.
            float distanceMeters = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
            Utils.showToast(owner.get().getActivity() ,"Distance from camera: " + distanceMeters + " metres");
            owner.get().currentScore.distance = distanceMeters;

            a.detach();
            aNode.setAnchor(owner.get().currentRandomAnchor);

            boolean continueTask = owner.get().loadTask();

            AppState.getInstance().addScoreToRound(owner.get().currentScore);
            owner.get().initCurrentScore();

            if (AppState.getInstance().getRound().isLocal()) {
                AREVIRepository.getInstance().postRound(AppState.getInstance().getRound());
            } else {
                AREVIRepository.getInstance().patchRound(AppState.getInstance().getRound().id, AppState.getInstance().getRound().score, !continueTask);
            }

            owner.get().currentRandomAnchorNode = null;
            if (continueTask) owner.get().getArSceneView().getScene().addOnUpdateListener(owner.get().randomRenderListener);
            else {
                owner.get().backToMain();
            }
        }
        return false;
    }
}

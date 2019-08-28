package com.upv.muitss.arevi.views;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Timer;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;

import java.lang.ref.WeakReference;

public class CustomTransformableNode extends TransformableNode {

    private static WeakReference<ArView> owner;
    private GestureDetector gestureDetector;

    CustomTransformableNode(WeakReference<ArView> pOwner) {
        super(pOwner.get().getTransformationSystem());
        owner = pOwner;
        gestureDetector = new GestureDetector(owner.get().getContext(), new GestureListener());
    }

    @Override
    public boolean onTouchEvent (HitTestResult hitTestResult, MotionEvent motionEvent){
        // get masked (not specific to a pointer) action
        int maskedAction = motionEvent.getActionMasked();

        switch(maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                return onTouchEventSelect(hitTestResult);
            case MotionEvent.ACTION_MOVE :
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        this.gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private boolean onTouchEventSelect(HitTestResult hitTestResult){
        if (AppState.getInstance().getIsLongPress()) {
            return onTouchEventDown(hitTestResult);
        }

        Node hitNode = hitTestResult.getNode();

        if (hitNode == null) return false;

        CustomTransformableNode aNode = null;
        if (hitNode instanceof CustomTransformableNode){
            aNode = (CustomTransformableNode) hitNode;
        }
        else {
            while (aNode == null){
                Node parent = hitNode.getParent();
                if (parent == null) {
                    return false;
                }
                else {
                    hitNode = parent;
                    if (hitNode instanceof CustomTransformableNode){
                        aNode = (CustomTransformableNode) hitNode;
                    }
                }
            }
        }

        AnchorNode an = (AnchorNode) aNode.getParent();
        if (an != null) {
            an.removeChild(aNode);

            owner.get().currentScore.scaleV = aNode.getLocalScale().x + 0.05F;
            owner.get().currentScore.scaleV1 = aNode.getLocalScale().y + 0.05F;
            owner.get().currentScore.scaleV2 = aNode.getLocalScale().z + 0.05F;

            ModelRenderable mr = (ModelRenderable) aNode.getRenderable();

            CustomTransformableNode node = new CustomTransformableNode(new WeakReference<>(owner.get()));
            node.setRenderable(mr);

            owner.get().addTransformableNode(an, node);
        }
        return true;
    }

    private boolean onTouchEventDown(HitTestResult hitTestResult){
        Timer.getInstance().pause();
        Node hitNode = hitTestResult.getNode();
        Activity ac = owner.get().getActivity();

        // Check for touching a Sceneform node
        if (hitNode != null && ac != null) {
            //Set current in scene object scale
            owner.get().currentScore.scaleV = hitNode.getLocalScale().x;
            owner.get().currentScore.scaleV1 = hitNode.getLocalScale().y;
            owner.get().currentScore.scaleV2 = hitNode.getLocalScale().z;

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

            if (a != null && frame != null) {
                Pose cameraPose = frame.getCamera().getPose();
                Pose objectPose = a.getPose();
                ///Compute the straight-line distance.
                float dx = objectPose.tx() - cameraPose.tx();
                float dy = objectPose.ty() - cameraPose.ty();
                float dz = objectPose.tz() - cameraPose.tz();
                owner.get().currentScore.distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
                //Set current score time since task started
                owner.get().currentScore.seconds = Timer.getInstance().getSeconds();
                owner.get().currentScore.minutes = Timer.getInstance().getMinutes();
                owner.get().currentScore.milliSeconds = Timer.getInstance().getMilliSeconds();

                Utils.showToast(ac ,"Distance from camera: " + owner.get().currentScore.distance + " metres");

                a.detach();
                owner.get().currentRandomAnchorNode = null;

                boolean continueTask = owner.get().loadTask();

                AppState.getInstance().addScoreToRound(owner.get().currentScore);
                owner.get().initCurrentScore();

                if (AppState.getInstance().getRound().isLocal()) {
                    AREVIRepository.getInstance().postRound(AppState.getInstance().getRound());
                } else {
                    AREVIRepository.getInstance().patchRound(AppState.getInstance().getRound().id, AppState.getInstance().getRound().score, !continueTask);
                }

                if (continueTask) owner.get().getArSceneView().getScene().addOnUpdateListener(owner.get().randomRenderListener);
                else {
                    owner.get().startAssessmentActivity();
                }
            }
            else return false;
        }
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }
    }
}

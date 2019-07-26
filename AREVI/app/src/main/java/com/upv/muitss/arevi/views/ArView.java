package com.upv.muitss.arevi.views;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.upv.muitss.arevi.MainActivity;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.drawables.PointerDrawable;
import com.upv.muitss.arevi.helpers.AppState;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ArView extends ArFragment {

    private PointerDrawable pointer;
    private View view;
    private Plane firstPlane;
    private AnchorNode anchorNode;
    private static Random rand;

    private static WeakReference<MainActivity> owner;
    private static final String TAG = "ArView";
    private AppState appState;

    public void init(WeakReference<MainActivity> pOwner) {
        owner = pOwner;
        assert owner.get() != null;

        rand = new Random();
        appState = owner.get().getAppState();
        pointer = new PointerDrawable(appState.getCenterX(), appState.getCenterY());

        View ownerView = getView();
        assert ownerView != null;
        view = ownerView.findViewById(R.id.sceneform_pointer);

        getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            onUpdate(frameTime);
            onFrame(frameTime);
        });
    }

    private void onFrame(FrameTime frameTime) {
        // Keep track of the first valid plane detected, update it
        // if the plane is lost or subsumed.

        Session session = getArSceneView().getSession();

        assert session != null;

        if (firstPlane == null || firstPlane.getTrackingState() != TrackingState.TRACKING) {
            Collection<Plane> planes = session.getAllTrackables(Plane.class);
            if (planes.isEmpty()) return;
            firstPlane = planes.iterator().next();
        }
        if (firstPlane.getSubsumedBy() != null) {
            firstPlane = firstPlane.getSubsumedBy();
        }

        boolean trackingChanged = updateTracking();
        if (trackingChanged) {
            if (appState.getIsTracking()) {
                view.getOverlay().add(pointer);
                view.invalidate();
            }
        }

        if (appState.getIsTracking()) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(appState.getIsHitting());
                view.invalidate();
            }

            if (appState.getNodeAge() > 2000000) {
                appState.setNodeAge(frameTime.getStartSeconds() - appState.getNodeAge());
                randomPlacedCube(firstPlane);
            }
            appState.setNodeAge(appState.getNodeAge() + frameTime.getStartSeconds());
        }
    }

    private void randomPlacedCube(Plane plane) {
        //find a random spot on the plane in the X
        // The width of the plan is 2*extentX in the range center.x +/- extentX
        float maxX = plane.getExtentX() * 2;
        float randomX = (maxX * rand.nextFloat()) - plane.getExtentX();

        float maxZ = plane.getExtentZ() * 2;
        float randomZ = (maxZ * rand.nextFloat()) - plane.getExtentZ();

        Pose pose = plane.getCenterPose();
        float[] translation = pose.getTranslation();
        float[] rotation = pose.getRotationQuaternion();

        translation[0] += randomX;
        translation[2] += randomZ;
        pose = new Pose(translation, rotation);

        Session session = getArSceneView().getSession();

        assert session != null;

        Anchor anchor = session.createAnchor(pose);

        if (anchorNode == null) {
            MaterialFactory.makeOpaqueWithColor(owner.get(), new Color(android.graphics.Color.RED))
                    .thenAccept(
                            material -> {
                                ModelRenderable render = ShapeFactory.makeSphere(0.1f,
                                        new Vector3(0.0f, 0.15f, 0.0f)
                                        , material);
                                Node node = new Node();
                                node.setRenderable(render);
                                anchorNode = new AnchorNode(anchor);
                                anchorNode.addChild(node);
                                anchorNode.setParent(getArSceneView().getScene());

                                node.setOnTapListener((hitTestResult, motionEvent) -> {
                                    //We are only interested in the ACTION_UP events - anything else just return
                                    if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                                        return;
                                    }

                                    Node hitNode = hitTestResult.getNode();
                                    // Check for touching a Sceneform node
                                    if (hitNode != null) {
                                        Log.d(TAG,"handleOnTouch hitTestResult.getNode() != null");

                                        Toast.makeText(owner.get(), "We've hit Andy!!", Toast.LENGTH_SHORT).show();
                                        AnchorNode aNode = ((AnchorNode) hitNode.getParent());
                                        assert aNode != null;
                                        Anchor a = aNode.getAnchor();
                                        assert a != null;
                                        a.detach();
                                        aNode.setAnchor(anchor);
                                    }
                                });
                            });

//            ViewRenderable.builder()
//                    .setView(owner.get(), R.layout.menu_layout)
//                    .build()
//                    .thenAccept(renderable -> {
//                        Node node = new Node();
//                        node.setRenderable(renderable);
//                        anchorNode = new AnchorNode(anchor);
//                        anchorNode.addChild(node);
//                        anchorNode.setParent(getArSceneView().getScene());
//
//                        node.setOnTapListener((hitTestResult, motionEvent) -> {
//                            //We are only interested in the ACTION_UP events - anything else just return
//                            if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
//                                return;
//                            }
//
//                            Node hitNode = hitTestResult.getNode();
//                            // Check for touching a Sceneform node
//                            if (hitNode != null) {
//                                Log.d(TAG,"handleOnTouch hitTestResult.getNode() != null");
//
//                                Toast.makeText(owner.get(), "We've hit Andy!!", Toast.LENGTH_SHORT).show();
//                                getArSceneView().getScene().removeChild(hitNode);
//                                assert hitNode.getParent() != null;
//                                Objects.requireNonNull(((AnchorNode) hitNode.getParent()).getAnchor()).detach();
//                                hitNode.setParent(null);
//                            }
//                        });
//
//                    });
        } else {
            Anchor a = anchorNode.getAnchor();
            assert a != null;
            a.detach();
            anchorNode.setAnchor(anchor);
        }
    }

    private boolean updateTracking() {
        Frame frame = getArSceneView().getArFrame();
        boolean wasTracking = appState.getIsTracking();
        appState.setIsTracking(frame != null &&
                frame.getCamera().getTrackingState() == TrackingState.TRACKING);
        return appState.getIsTracking() != wasTracking;
    }

    private boolean updateHitTest() {
        Frame frame = getArSceneView().getArFrame();
        Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = appState.getIsHitting();
        appState.setIsHitting(false);
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    appState.setIsHitting(true);
                    break;
                }
            }
        }
        owner.get().TouchView(getView());
        return wasHitting != appState.getIsHitting();
    }

    private Point getScreenCenter() {
        return new Point(appState.getCenterX(), appState.getCenterY());
    }
}


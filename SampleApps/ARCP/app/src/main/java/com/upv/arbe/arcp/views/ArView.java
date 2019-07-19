package com.upv.arbe.arcp.views;

import android.graphics.Point;
import android.util.DisplayMetrics;
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
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.upv.arbe.arcp.MainActivity;
import com.upv.arbe.arcp.R;
import com.upv.arbe.arcp.helpers.PointerDrawable;

import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ArView extends ArFragment {

    private PointerDrawable pointer;
    private View view;
    private static WeakReference<MainActivity> owner;
    private Plane firstPlane;
    private AnchorNode anchorNode;
    private static final String TAG = "ArView";
    private static Random rand = new Random();

    private int centerX;
    private int centerY;
    private boolean isTracking;
    private boolean isHitting;
    private boolean isFocusing;
    private float nodeAge;

    public void init(DisplayMetrics metrics, WeakReference<MainActivity> pOwner) {
        owner = pOwner;
        assert owner.get() != null;
        centerX = metrics.widthPixels / 2;
        centerY = metrics.heightPixels / 2;

        pointer = new PointerDrawable(centerX, centerY);
        View ownerView = getView();
        assert ownerView != null;
        view = ownerView.findViewById(R.id.sceneform_pointer);

        getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            onUpdate(frameTime);
            onFrame(frameTime);
            updateVIew();
        });
    }

    private void onFrame(FrameTime frameTime) {
        // Keep track of the first valid plane detected, update it
        // if the plane is lost or subsumed.

        Session session = getArSceneView().getSession();

        assert session != null;

        if (firstPlane == null || firstPlane.getTrackingState() != TrackingState.TRACKING) {
            Collection<Plane> planes = session.getAllTrackables(Plane.class);
            if (!planes.isEmpty()) {
                firstPlane = planes.iterator().next();
            }
            return;
        }
        if (firstPlane.getSubsumedBy() != null) {
            firstPlane = firstPlane.getSubsumedBy();
        }

        // Every 2 seconds move the node to a different spot.
        if (nodeAge > 10000000) {

            nodeAge = frameTime.getStartSeconds() - nodeAge;
            randomPlacedCube(firstPlane);
        }
        nodeAge += frameTime.getStartSeconds();
        Log.d(TAG, "" + nodeAge);
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
            ViewRenderable.builder()
                .setView(owner.get(), R.layout.menu_layout)
                .build()
                .thenAccept(renderable -> {
                    Node node = new Node();
                    node.setRenderable(renderable);
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
                            getArSceneView().getScene().removeChild(hitNode);
                            assert hitNode.getParent() != null;
                            Objects.requireNonNull(((AnchorNode) hitNode.getParent()).getAnchor()).detach();
                            hitNode.setParent(null);
                        }
                    });

                });
        } else {
            anchorNode.getAnchor().detach();
            anchorNode.setAnchor(anchor);
        }
    }

    private void updateVIew() {
        boolean trackingChanged = updateTracking();
        if (trackingChanged) {
            if (isTracking) {
                view.getOverlay().add(pointer);
            } else {
                view.getOverlay().remove(pointer);
            }
            view.invalidate();
        }

        if (isTracking) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(isHitting);
                view.invalidate();
            }
        }
    }

    private boolean updateTracking() {
        Frame frame = getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame != null &&
                frame.getCamera().getTrackingState() == TrackingState.TRACKING;
        return isTracking != wasTracking;
    }

    private boolean updateHitTest() {
        Frame frame = getArSceneView().getArFrame();
        Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = isHitting;
        isHitting = false;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    owner.get().TouchView(view);
                    isHitting = true;
                    break;
                }
            }
        }
        return wasHitting != isHitting;
    }

    private Point getScreenCenter() {
        return new Point(centerX, centerY);
    }

    public void setIsFocusing(boolean pIsFocusing) {
        isFocusing = pIsFocusing;
    }

    public boolean getIsFocusing() {
        return isFocusing;
    }
}

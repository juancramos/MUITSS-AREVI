package com.upv.muitss.arevi.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.text.TextUtils;
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
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.upv.muitss.arevi.ArActivity;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.drawables.PointerDrawable;
import com.upv.muitss.arevi.entities.PolyAsset;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.logic.web.implementations.PolyRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ArView extends ArFragment {

    private PointerDrawable pointer;
    private View pointerView;
    private Plane firstPlane;
    private AnchorNode webRTCNode;
    private AnchorNode randomNode;
    private static Random rand;

    private static WeakReference<ArActivity> owner;
    private AppState appState;

    public void init(WeakReference<ArActivity> pOwner) {
        owner = pOwner;
        assert owner.get() != null;

        rand = new Random();
        appState = AppState.getInstance();
        pointer = new PointerDrawable(appState.getCenterX(), appState.getCenterY());

        View ownerView = getView();
        assert ownerView != null;
        pointerView = ownerView.findViewById(R.id.sceneform_pointer);

        getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            onUpdate(frameTime);
            onUpdate();
        });

        getArSceneView().getScene().addOnUpdateListener(randomRenderListener);
    }

    public void attachWebRTCView(){
        if (webRTCNode == null) getArSceneView().getScene().addOnUpdateListener(webRtcRenderListener);
        else removeViewRenderable();
    }

    private Scene.OnUpdateListener randomRenderListener = frameTime -> {
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

        if (appState.getIsTracking()) {
            randomPlacedCube(firstPlane);
        }
    };

    private void onUpdate() {
        boolean trackingChanged = updateTracking();
        if (trackingChanged) {
            if (appState.getIsTracking()) {
                pointerView.getOverlay().add(pointer);
                pointerView.invalidate();
            }
        }

        if (appState.getIsTracking()) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(appState.getIsHitting());
                pointerView.invalidate();
            }
        }
    }

    private Scene.OnUpdateListener webRtcRenderListener = frameTime -> {

        if (!appState.getIsTracking()) return;

        Frame frame = getArSceneView().getArFrame();
        if(frame == null) { return; }

        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);
        if (planes.isEmpty()) return;
        Plane anchorPlane = planes.iterator().next();

        if (anchorPlane.getSubsumedBy() != null) {
            anchorPlane = anchorPlane.getSubsumedBy();
        }

        Pose pose = anchorPlane.getCenterPose();
        Anchor anchor = anchorPlane.createAnchor(pose);

        ViewRenderable.builder()
                .setView(owner.get(), R.layout.webrtc_view)
                .build()
                .thenAccept(renderable -> {
                    renderable.setShadowCaster(false);
                    renderable.setShadowReceiver(false);
                    addViewRenderable(anchor, renderable);
                });
    };

    private void addViewRenderable(Anchor anchor, ViewRenderable render) {
        getArSceneView().getScene()
                .removeOnUpdateListener(webRtcRenderListener);


        TransformableNode node = new TransformableNode(getTransformationSystem());
        node.setRenderable(render);
        // Create the Sceneform AnchorNode
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.addChild(node);

        anchorNode.setParent(getArSceneView().getScene());
        webRTCNode = anchorNode;

        owner.get().MountViewData((SurfaceViewRenderer) render.getView());
    }

    private void removeViewRenderable(){
        Toast.makeText(owner.get(), "Remove pointerView", Toast.LENGTH_SHORT).show();
        getArSceneView().getScene().removeChild(webRTCNode);
        Node parent = webRTCNode.getParent();
        if (parent != null) {
            Anchor anchorParent = ((AnchorNode) parent).getAnchor();
            if (anchorParent != null){
                anchorParent.detach();
                webRTCNode.setParent(null);
                webRTCNode = null;
            }
        }
    }

    private void randomPlacedCube(Plane plane) {
        getArSceneView().getScene()
                .removeOnUpdateListener(randomRenderListener);

        //find a random spot on the plane in the X
        // The width of the plan is 2*extentX in the range center.x +/- extentX
        float maxX = plane.getExtentX() * 2;
        float randomX = (maxX * rand.nextFloat()) - plane.getExtentX();

        Pose pose = plane.getCenterPose();
        float[] translation = pose.getTranslation();
        float[] rotation = pose.getRotationQuaternion();

        translation[0] += randomX;
        pose = new Pose(translation, rotation);
        Anchor anchor = plane.createAnchor(pose);

        if (randomNode == null) {
            getCurrentRenderable(anchor);
        } else {
            Anchor a = randomNode.getAnchor();
            assert a != null;
            a.detach();
            randomNode.setAnchor(anchor);
        }
    }

    private void getCurrentRenderable(Anchor anchor){
        Context context = this.getContext();
        Activity activity = this.getActivity();

        if (activity == null || context == null) return;

        activity.runOnUiThread(() -> PolyRepository.getInstance().getApiAsset("c-tEGK9e49p", new ActivityMessage() {
            @Override
            public <T> void onResponse(T response) {
                if (response instanceof PolyAsset){
                    PolyAsset pa = (PolyAsset) response;

                    if (TextUtils.isEmpty(pa.modelUrl)) return;

                    RenderableSource source = RenderableSource.builder().setSource(context,
                            Uri.parse(pa.modelUrl), RenderableSource.SourceType.GLTF2)
                            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                            .build();


                    ModelRenderable.builder().setRegistryId(pa.key)
                            .setSource(context, source)
                            .build()
                            .thenAccept(renderable -> {
                                randomNode = new AnchorNode(anchor);
                                randomNode.setParent(getArSceneView().getScene());

                                // Create the transformable andy and add it to the anchor.
                                TransformableNode node = new TransformableNode(getTransformationSystem());
                                node.setRenderable(renderable);
                                //set rotation in direction (x,y,z) in degrees 0
                                node.setLocalRotation(Quaternion.axisAngle(new Vector3(0f, 0, 0), 0f));
                                // Set the min and max scales of the ScaleController.
                                // Default min is 0.75, default max is 1.75.
                                node.getScaleController().setMinScale(0.4f);
                                node.getScaleController().setMaxScale(1.0f);

                                // Set the local scale of the node BEFORE setting its parent
                                node.setLocalScale(new Vector3(0.05f, 0.05f, 0.05f));

                                node.setParent(randomNode);
                                node.select();

                                node.setOnTapListener((hitTestResult, motionEvent) -> {
                                    //We are only interested in the ACTION_UP events - anything else just return
                                    if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                                        return;
                                    }

                                    Node hitNode = hitTestResult.getNode();
                                    // Check for touching a Sceneform node
                                    if (hitNode != null) {
                                        Toast.makeText(owner.get(), "We've hit Andy!!", Toast.LENGTH_SHORT).show();
                                        AnchorNode aNode = ((AnchorNode) hitNode.getParent());
                                        assert aNode != null;
                                        Anchor a = aNode.getAnchor();
                                        assert a != null;
                                        a.detach();
                                        aNode.setAnchor(anchor);
                                    }
                                });
                            })
                            .exceptionally(throwable -> null);
                }
            }
        }));
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
        if(getView() == null) {
            return new android.graphics.Point(0,0);
        }

        int w = getView().getWidth()/2;
        int h = getView().getHeight()/2;
        return new Point(w, h);

        //return new Point(appState.getCenterX(), appState.getCenterY());
    }
}


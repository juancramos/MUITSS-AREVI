package com.upv.muitss.arevi.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.upv.muitss.arevi.ArActivity;
import com.upv.muitss.arevi.MainActivity;
import com.upv.muitss.arevi.PagerActivity;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.drawables.PointerDrawable;
import com.upv.muitss.arevi.entities.PolyAsset;
import com.upv.muitss.arevi.entities.Work;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;

import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ArView extends ArFragment {

    private PointerDrawable pointer;
    private View pointerView;
    private ProgressBar spinner;

    private Plane firstPlane;
    private AnchorNode webRTCNode;
    private Anchor currentRandomAanchor;
    private AnchorNode currentRandomAnchorNode;
    private static Random rand;

    private Work currentScore;

    private static WeakReference<ArActivity> owner;

    public void init(WeakReference<ArActivity> pOwner) {
        owner = pOwner;
        if (owner.get() == null) return;

        rand = new Random();

        if (getView() == null) return;

        currentScore = new Work();

        pointerView = getView().findViewById(R.id.sceneform_pointer);
        spinner= getView().findViewById(R.id.sceneform_progress_bar);
        spinner.setVisibility(View.GONE);

        getView().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Point pt = getScreenCenter();
            if (pointer == null){
                pointer = new PointerDrawable(pt.x, pt.y);
            }
            else {
                pointer.setPoint(pt);
            }
        });

        getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            onUpdate(frameTime);
            onUpdate();
        });
        getArSceneView().getScene().addOnUpdateListener(randomRenderListener);
    }

    ///ARCORE
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

        if (AppState.getInstance().getIsTracking()) {
            randomPlacedPoly(firstPlane);
        }
    };

    private void onUpdate() {
        if (getView() == null) return;
        boolean trackingChanged = updateTracking();
        if (trackingChanged) {
            if (AppState.getInstance().getIsTracking()) {
                pointerView.getOverlay().add(pointer);
            } else {
                pointerView.getOverlay().remove(pointer);
            }
            pointerView.invalidate();
        }

        if (AppState.getInstance().getIsTracking()) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(AppState.getInstance().getIsHitting());
                pointerView.invalidate();
            }
        }
    }

    private void randomPlacedPoly(Plane plane) {
        getArSceneView().getScene()
                .removeOnUpdateListener(randomRenderListener);

        spinner.setVisibility(View.VISIBLE);

        //find a random spot on the plane in the X
        // The width of the plan is 2*extentX in the range center.x +/- extentX
        float maxX = plane.getExtentX() * 2;
        float randomX = (maxX * rand.nextFloat()) - plane.getExtentX();

        Pose pose = plane.getCenterPose();
        float[] translation = pose.getTranslation();
        float[] rotation = new float[]{0,0,0,0};

        translation[0] += randomX;
        pose = new Pose(translation, rotation);
        currentRandomAanchor = plane.createAnchor(pose);

        if (currentRandomAnchorNode == null) {
            getCurrentRenderable();
        } else {
            Anchor a = currentRandomAnchorNode.getAnchor();
            if (a == null) return;
            a.detach();
            currentRandomAnchorNode.setAnchor(currentRandomAanchor);
        }
    }

    private void getCurrentRenderable(){
        Context context = this.getContext();

        if (context == null) return;

        PolyAsset pa = AppState.getInstance().pollPolyAsset();

        if (pa == null || TextUtils.isEmpty(pa.modelUrl)) return;

        RenderableSource source = RenderableSource.builder().setSource(context,
                Uri.parse(pa.modelUrl), RenderableSource.SourceType.GLTF2)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();


        ModelRenderable.builder().setRegistryId(pa.key)
                .setSource(context, source)
                .build()
                .thenAccept(renderable -> {
                    currentRandomAnchorNode = new AnchorNode(currentRandomAanchor);
                    currentRandomAnchorNode.setParent(getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode node = new TransformableNode(getTransformationSystem());
                    node.setRenderable(renderable);

                    // Set the min and max scales of the ScaleController.
                    node.getScaleController().setMinScale(0.1f);

                    // Set the local scale of the node BEFORE setting its parent
                    node.setLocalScale(new Vector3(pa.scaleV, pa.scaleV1, pa.scaleV2));

                    node.setParent(currentRandomAnchorNode);
                    node.select();

                    spinner.setVisibility(View.GONE);

                    currentScore.id = pa.assetId;
                    currentScore.scaleV = pa.scaleV;
                    currentScore.scaleV1 = pa.scaleV1;
                    currentScore.scaleV2 = pa.scaleV2;

                    node.setOnTapListener(this::onTapListener);
                }).exceptionally(throwable -> null);
    }

    private void onTapListener(HitTestResult hitTestResult, MotionEvent motionEvent){

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
            aNode.setAnchor(currentRandomAanchor);

            boolean continueTask = owner.get().loadTask();

            AppState.getInstance().addScore(currentScore);
            if (AppState.getInstance().getRound().isLocal()) {
                AREVIRepository.getInstance().postRound(AppState.getInstance().getRound());
            } else {
                AREVIRepository.getInstance().patchRound(AppState.getInstance().getRound().id, AppState.getInstance().getRound().score, !continueTask);
            }

            currentRandomAnchorNode = null;
            if (continueTask) getArSceneView().getScene().addOnUpdateListener(randomRenderListener);
            else {
                startMainActivity();
            }
        }
    }

    private boolean updateTracking() {
        Frame frame = getArSceneView().getArFrame();
        boolean isTracking = frame != null &&
                frame.getCamera().getTrackingState() == TrackingState.TRACKING;
        boolean wasTracking = AppState.getInstance().getIsTracking();
        AppState.getInstance().setIsTracking(isTracking);
        return isTracking != wasTracking;
    }

    private boolean updateHitTest() {
        Frame frame = getArSceneView().getArFrame();
        Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = AppState.getInstance().getIsHitting();
        AppState.getInstance().setIsHitting(false);
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    AppState.getInstance().setIsHitting(true);
                    break;
                }
            }
        }
        owner.get().TouchView(getView(), pt);
        return wasHitting != AppState.getInstance().getIsHitting();
    }

    ///WEBRTC
    private Scene.OnUpdateListener webRtcRenderListener = frameTime -> {

        if (!AppState.getInstance().getIsTracking()) return;

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

    public void attachWebRTCView(){
        if (webRTCNode == null) getArSceneView().getScene().addOnUpdateListener(webRtcRenderListener);
        else removeViewRenderable();
    }

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

    private void startMainActivity(){
        Intent toMain = new Intent(owner.get(), MainActivity.class);
        startActivity(toMain);
        owner.get().finish();
    }

    private Point getScreenCenter() {
        if(getView() == null) {
            return new Point(0,0);
        }

        int w = getView().getWidth()/2;
        int h = getView().getHeight()/2;
        return new Point(w, h);
    }
}


package com.upv.muitss.arevi.views;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.collision.Box;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.upv.muitss.arevi.ArActivity;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.drawables.PointerDrawable;
import com.upv.muitss.arevi.entities.Work;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Timer;
import com.upv.muitss.arevi.models.PolyAsset;

import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ArView extends ArFragment {

    private PointerDrawable pointer;
    private View pointerView;
    private ProgressBar spinner;

    private Plane firstPlane;
    private Node polyAssetInfoNode;
    private AnchorNode webRTCNode;
    private boolean isWebRTCNodeVisible = false;
    private static Random rand = new Random();;

    Work currentScore;
    AnchorNode currentRandomAnchorNode;
    PolyAsset currentPolyAsset;

    private static WeakReference<ArActivity> owner;

    public void init(WeakReference<ArActivity> pOwner) {
        owner = pOwner;
        if (getView() != null) {

            initCurrentScore();

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
        }

        getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            onUpdate(frameTime);
            onUpdate();
        });
        getArSceneView().getScene().addOnUpdateListener(randomRenderListener);

        Timer.getInstance().reset();
    }

    public boolean loadTask(){
        return owner.get().loadTask();
    }

    ///ARCORE
    Scene.OnUpdateListener randomRenderListener = frameTime -> {

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

    public void addTransformableNode(AnchorNode anchorNode, CustomTransformableNode node){
        attachInfoCardNode(node, currentPolyAsset);

        // Set the min and max scales of the ScaleController.
        node.getScaleController().setMinScale(0.1f);

        Vector3 vector3 = new Vector3(currentScore.scaleV, currentScore.scaleV1, currentScore.scaleV2);
        // Set the local scale of the node BEFORE setting its parent
        node.setLocalScale(vector3);

        node.setParent(anchorNode);
        node.select();

        Timer.getInstance().start();

        spinner.setVisibility(View.GONE);
    }

    private void onUpdate() {
        if (getView() == null || pointer == null || pointerView == null) return;
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

        polyAssetInfoLookToCamera();
    }

    private void randomPlacedPoly(Plane plane) {
        getArSceneView().getScene()
                .removeOnUpdateListener(randomRenderListener);

        spinner.setVisibility(View.VISIBLE);

        //find a random spot on the plane in the X
        // The width of the plan is 2*extentX in the range center.x +/- extentX
        float maxX = plane.getExtentX() * 1.5f;
        float randomX = (maxX * rand.nextFloat()) - plane.getExtentX();

        Pose pose = plane.getCenterPose();
        float[] translation = pose.getTranslation();
        float[] rotation = new float[]{0,0,0,0};

        translation[0] += randomX;
        pose = new Pose(translation, rotation);
        Anchor anchor = plane.createAnchor(pose);

        if (currentRandomAnchorNode == null) {
            getCurrentRenderable(anchor);
        } else {
            Anchor a = currentRandomAnchorNode.getAnchor();
            if (a == null) return;
            a.detach();
            currentRandomAnchorNode.setAnchor(anchor);
        }
    }

    private void getCurrentRenderable(Anchor anchor){
        Context context = this.getContext();

        if (context == null) return;

        currentPolyAsset = AppState.getInstance().pollPolyAsset();

        if (currentPolyAsset == null || TextUtils.isEmpty(currentPolyAsset.modelUrl)) {
            backToMain();
            return;
        }

        RenderableSource source = RenderableSource.builder().setSource(context,
                Uri.parse(currentPolyAsset.modelUrl), RenderableSource.SourceType.GLTF2)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();


        ModelRenderable.builder().setRegistryId(currentPolyAsset.key)
                .setSource(context, source)
                .build().thenAccept((render) -> addModelToScene(anchor, render)).exceptionally(throwable -> backToMain());
    }

    private void addModelToScene(Anchor anchor, ModelRenderable render) {
        currentScore.id = currentPolyAsset.assetId;
        currentScore.scaleV = currentPolyAsset.scaleV;
        currentScore.scaleV1 = currentPolyAsset.scaleV1;
        currentScore.scaleV2 = currentPolyAsset.scaleV2;

        currentRandomAnchorNode = new AnchorNode(anchor);
        currentRandomAnchorNode.setParent(getArSceneView().getScene());

        // Create the transformable andy and add it to the anchor.
        CustomTransformableNode node = new CustomTransformableNode(new WeakReference<>(this));
        node.setRenderable(render);

        addTransformableNode(currentRandomAnchorNode, node);
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
        return wasHitting != AppState.getInstance().getIsHitting();
    }

    private void attachInfoCardNode(TransformableNode parent, PolyAsset selectedItem) {
        polyAssetInfoNode = new Node();
        renderArView(R.layout.ar_model_info_view)
                .thenAccept(renderable -> {
                    renderable.setShadowCaster(false);
                    renderable.setShadowReceiver(false);
                    polyAssetInfoNode.setRenderable(renderable);
                    setModelLabel(renderable, selectedItem);
                }).exceptionally(throwable -> backToMain());
        polyAssetInfoNode.setParent(parent);
        float height = .5f;
        if (parent.getRenderable() instanceof ModelRenderable) {
            height = getRenderableHeight((ModelRenderable) parent.getRenderable());
        }
        polyAssetInfoNode.setLocalPosition(new Vector3(0, height, 0));
    }

    private CompletableFuture<ViewRenderable> renderArView(int drawableId){
        return ViewRenderable.builder()
                .setView(owner.get(), drawableId)
                .build();
    }

    private float getRenderableHeight(ModelRenderable renderable) {
        Box box = (Box) renderable.getCollisionShape();
        return Objects.requireNonNull(box).getCenter().y + box.getExtents().y;
    }

    private void setModelLabel(@NonNull ViewRenderable viewRenderable,
                               @NonNull PolyAsset selectedItem) {
        TextView textView = (TextView) viewRenderable.getView();
        textView.setText(String.format("%s by %s\n%s",
                selectedItem.displayName, selectedItem.authorName, selectedItem.license));
    }

    private void polyAssetInfoLookToCamera() {
        if (getArSceneView().getScene() == null) {
            return;
        }
        Camera camera = getArSceneView().getScene().getCamera();
        // Rotate the card to look at the camera.
        if (polyAssetInfoNode != null) {
            Vector3 cameraPosition = camera.getWorldPosition();
            Vector3 cardPosition = polyAssetInfoNode.getWorldPosition();
            Vector3 direction = Vector3.subtract(cameraPosition, cardPosition);
            Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
            polyAssetInfoNode.setWorldRotation(lookRotation);
        }
    }


    ///WEBRTC
    public void attachWebRTCView(){
        if (webRTCNode == null) getArSceneView().getScene().addOnUpdateListener(webRtcRenderListener);
        else {
            removeViewRenderable();
            owner.get().resetWebRTCModule();
        }
    }

    public void setRemoteVideoViewVisibility(){
        if (webRTCNode != null && !isWebRTCNodeVisible) {
            isWebRTCNodeVisible = true;
            webRTCNode.setParent(getArSceneView().getScene());
        }
        else if (webRTCNode != null) {
            isWebRTCNodeVisible = false;
            getArSceneView().getScene().onRemoveChild(webRTCNode);
            webRTCNode.setParent(null);
        }
    }

    public Point getScreenCenter() {
        if(getView() == null) {
            return new Point(0,0);
        }

        int w = getView().getWidth()/2;
        int h = getView().getHeight()/2;
        return new Point(w, h);
    }

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

        renderArView(R.layout.webrtc_view)
                .thenAccept(render -> addViewRenderable(anchor, render)) .exceptionally(throwable -> backToMain());
    };

    private void addViewRenderable(Anchor anchor, ViewRenderable render) {
        getArSceneView().getScene()
                .removeOnUpdateListener(webRtcRenderListener);

        render.setShadowCaster(false);
        render.setShadowReceiver(false);

        TransformableNode node = new TransformableNode(getTransformationSystem());
        node.setRenderable(render);
        // Create the Sceneform AnchorNode
        webRTCNode = new AnchorNode(anchor);
        webRTCNode.addChild(node);

        owner.get().MountViewData((SurfaceViewRenderer) render.getView());
    }

    private void removeViewRenderable(){
        getArSceneView().getScene().removeChild(webRTCNode);
        Anchor anchorParent = webRTCNode.getAnchor();
        if (anchorParent != null){
            anchorParent.detach();
        }
        webRTCNode.setParent(null);
        webRTCNode = null;
    }

    private Void backToMain(){
        owner.get().startMainActivity();
        return null;
    }

    void startAssessmentActivity(){
        owner.get().startAssessmentActivity();
    }

    void initCurrentScore() {
        currentScore = new Work();
    }

    @Override
    public void onResume(){
        super.onResume();

        rand = new Random();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pointer = null;
        pointerView = null;
        spinner = null;

        firstPlane = null;
        webRTCNode = null;
        currentRandomAnchorNode = null;
        polyAssetInfoNode = null;
        rand = null;

        currentScore = null;
        currentPolyAsset = null;

        owner = null;
    }
}


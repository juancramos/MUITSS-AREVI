package com.upv.arbe.arcp.views;

import android.graphics.Point;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.ux.ArFragment;
import com.upv.arbe.arcp.MainActivity;
import com.upv.arbe.arcp.R;
import com.upv.arbe.arcp.helpers.PointerDrawable;

import java.lang.ref.WeakReference;
import java.util.List;

public class ArView extends ArFragment {

    private PointerDrawable pointer;
    private View view;
    private static WeakReference<MainActivity> owner;
    private static final String TAG = "ArView";

    private int centerX;
    private int centerY;
    private boolean isTracking;
    private boolean isHitting;
    private boolean isFocusing;

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
            updateVIew();
        });
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

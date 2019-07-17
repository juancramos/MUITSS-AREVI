package com.upv.arbe.arcp.views;

import android.content.Context;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.ux.ArFragment;
import com.upv.arbe.arcp.helpers.PointerDrawable;

import java.util.List;

public class ARView extends View {

    private int centerX;
    private int centerY;
    private boolean isTracking;
    private boolean isHitting;

    private PointerDrawable pointer;

    private ArFragment fragment;

    public ARView(Context context) {
        this(context, null);
    }

    public ARView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(DisplayMetrics metrics, ArFragment arFragment) {
        centerX = metrics.widthPixels / 2;
        centerY = metrics.heightPixels / 2;

        pointer = new PointerDrawable();

        fragment = arFragment;
        assert fragment != null;
        fragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            fragment.onUpdate(frameTime);
            onUpdate();
        });
    }

    private void onUpdate() {
        boolean trackingChanged = updateTracking();
        if (trackingChanged) {
            if (isTracking) {
                this.getOverlay().add(pointer);
            } else {
                this.getOverlay().remove(pointer);
            }
            this.invalidate();
        }

        if (isTracking) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(isHitting);
                this.invalidate();
            }
        }
    }

    private boolean updateTracking() {
        Frame frame = fragment.getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame != null &&
                frame.getCamera().getTrackingState() == TrackingState.TRACKING;
        return isTracking != wasTracking;
    }

    private boolean updateHitTest() {
        Frame frame = fragment.getArSceneView().getArFrame();
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
                    TouchView(this);
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

    private void TouchView(View view)
    {
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, centerX, centerY, 0));
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, centerX, centerY, 0));
    }
}

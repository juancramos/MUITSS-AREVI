package com.upv.arbe.arcp;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.upv.arbe.arcp.helpers.AppState;
import com.upv.arbe.arcp.logic.webrtc.implementations.WebRTCModule;
import com.upv.arbe.arcp.views.ArView;
import com.upv.arbe.arcp.views.MenuView;

import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private AppState appState;
    private WeakReference<MainActivity> weakReference;
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;

    private DisplayMetrics metrics;
//    private SurfaceViewRenderer localVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appState = new AppState();

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        appState.setCenterX(metrics.widthPixels / 2);
        appState.setCenterY(metrics.heightPixels / 2);

        ArView arView = (ArView) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        MenuView gallery = findViewById(R.id.gallery_layout);

        assert arView != null && gallery != null;

        weakReference = new WeakReference<>(this);
        arView.init(weakReference);
        gallery.init(weakReference);

        startScreenCapture();
    }

    private void startScreenCapture() {
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getApplication().getSystemService(
                        Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                CAPTURE_PERMISSION_REQUEST_CODE);
        Log.d("tagged", ">>>>Method called :- ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CAPTURE_PERMISSION_REQUEST_CODE) { return; }

        WebRTCModule webRTCModule = new WebRTCModule(weakReference);

        SurfaceViewRenderer remoteVideoView = findViewById(R.id.remote_gl_surface_view);
        webRTCModule.MountRemoteView(remoteVideoView);

        webRTCModule.InitRTC(data);

//        localVideoView = findViewById(R.id.local_gl_surface_view);
//
//        webRTCModule.MountLocalView(localVideoView);

    }

    public void TouchView(View view)
    {
        if (!appState.getIsFocusing()) return;
        
        int centerX = appState.getCenterX();
        int centerY = appState.getCenterY();
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_DOWN, centerX, centerY, 0));
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_UP, centerX, centerY, 0));
    }

    public int dpToPx(int dp) {
        return Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public AppState getAppState() {
        return appState;
    }
}

package com.upv.muitss.arevi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.projection.MediaProjectionManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.PolyRepository;
import com.upv.muitss.arevi.logic.webrtc.implementations.WebRTCModule;
import com.upv.muitss.arevi.views.ArView;
import com.upv.muitss.arevi.views.MenuView;

import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;

public class ArActivity extends AppCompatActivity {

    private ArView arView;
    private MenuView gallery;
    private WeakReference<ArActivity> weakReference;
    private final String TAG = this.getClass().getCanonicalName();
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;

    private WebRTCModule webRTCModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_ar);

        // Get the Intent that called for this Activity to open
        //Intent activityThatCalled = getIntent();
        // Get the data that was sent
        // String previousActivity = activityThatCalled.getExtras().getString("callingActivity");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        arView = (ArView) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        gallery = findViewById(R.id.gallery_layout);

        assert arView != null && gallery != null;

        weakReference = new WeakReference<>(this);
        arView.init(weakReference);
        gallery.init(weakReference);

        startScreenCapture();
        loadTask();
    }

    public boolean loadTask() {
        if(AppState.getInstance().ids.isEmpty() && AppState.getInstance().polyQueueIsEmpty()) return false;
        runOnUiThread(() -> {
            while (!AppState.getInstance().ids.isEmpty() && AppState.getInstance().polyQueueHasToLoad()) {
                String nextId = AppState.getInstance().ids.poll();
                PolyRepository.getInstance().getApiAsset(nextId);
            }
        });
        return true;
    }

    public void arAttachWebRTCView(){
        arView.attachWebRTCView();
    }

    private void startScreenCapture() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
        Log.i(TAG, "Requesting confirmation");
        // This initiates a prompt dialog for the user to confirm screen projection.
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                CAPTURE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_PERMISSION_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                gallery.setCastButtonEnabled(false);
                return;
            }

            Log.i(TAG, "Starting screen capture");

            webRTCModule = new WebRTCModule(weakReference);

            webRTCModule.InitRTC(data);
        }
    }

    public void MountViewData(SurfaceViewRenderer remoteVideoView){
        webRTCModule.MountRemoteView(remoteVideoView);
    }

    public void TouchView(View view, Point point)
    {
        if (!AppState.getInstance().getIsFocusing()) return;

        int centerX = point.x;
        int centerY = point.y;
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_DOWN, centerX, centerY, 0));
        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_UP, centerX, centerY, 0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.upv.muitss.arevi;

import android.app.Activity;
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
import android.widget.Toast;

import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.logic.webrtc.implementations.WebRTCModule;
import com.upv.muitss.arevi.views.ArView;
import com.upv.muitss.arevi.views.MenuView;

import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;

public class ArActivity extends AppCompatActivity {

    private AppState appState;
    private WeakReference<ArActivity> weakReference;
    private final String TAG = this.getClass().getCanonicalName();
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;

    private WebRTCModule webRTCModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        // Get the Intent that called for this Activity to open
        //Intent activityThatCalled = getIntent();
        // Get the data that was sent
        // String previousActivity = activityThatCalled.getExtras().getString("callingActivity");

        appState = AppState.getInstance();

        DisplayMetrics metrics = new DisplayMetrics();
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
                Log.i(TAG, "User cancelled");
                Toast.makeText(this, "User cancelled", Toast.LENGTH_SHORT).show();
                Intent navigateIntent = new Intent(this, MainActivity.class);
                startActivity(navigateIntent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

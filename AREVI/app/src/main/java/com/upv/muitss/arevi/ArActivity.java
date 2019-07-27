package com.upv.muitss.arevi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
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
import com.upv.muitss.arevi.views.ArView;
import com.upv.muitss.arevi.views.MenuView;

import org.webrtc.SurfaceViewRenderer;

import java.lang.ref.WeakReference;

public class ArActivity extends AppCompatActivity {

    private AppState appState;
    private WeakReference<ArActivity> weakReference;
    private static final String TAG = "ArActivity";
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;
    private static final String STATE_RESULT_CODE = "result_code";
    private static final String STATE_RESULT_DATA = "result_data";

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mMediaProjection;
    private DisplayMetrics metrics;
    private int mResultCode;
    private Intent mResultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        if (savedInstanceState != null) {
            mResultCode = savedInstanceState.getInt(STATE_RESULT_CODE);
            mResultData = savedInstanceState.getParcelable(STATE_RESULT_DATA);
        }
        // Get the Intent that called for this Activity to open
        //Intent activityThatCalled = getIntent();
        // Get the data that was sent
        // String previousActivity = activityThatCalled.getExtras().getString("callingActivity");

        appState = AppState.getInstance();

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
        mediaProjectionManager =
                (MediaProjectionManager) getApplication().getSystemService(
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
            mResultCode = resultCode;
            mResultData = data;
            mMediaProjection = mediaProjectionManager.getMediaProjection(mResultCode, mResultData);

//        WebRTCModule webRTCModule = new WebRTCModule(weakReference);
//
//        SurfaceViewRenderer remoteVideoView = findViewById(R.id.remote_gl_surface_view);
//        webRTCModule.MountRemoteView(remoteVideoView);
//
//        webRTCModule.InitRTC(data);

//        localVideoView = findViewById(R.id.local_gl_surface_view);
//
//        webRTCModule.MountLocalView(localVideoView);

        }
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

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tearDownMediaProjection();
    }
}

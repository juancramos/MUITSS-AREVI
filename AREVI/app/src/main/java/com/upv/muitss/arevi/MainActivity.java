 package com.upv.muitss.arevi;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.upv.muitss.arevi.helpers.AppState;

import java.lang.ref.WeakReference;

 public class MainActivity extends AppCompatActivity {

    private AppState appState;
    private WeakReference<MainActivity> weakReference;
    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

     public AppState getAppState() {
         return appState;
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
}

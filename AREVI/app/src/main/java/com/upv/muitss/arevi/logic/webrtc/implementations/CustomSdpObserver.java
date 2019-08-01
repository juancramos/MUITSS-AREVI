package com.upv.muitss.arevi.logic.webrtc.implementations;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public class CustomSdpObserver implements SdpObserver {


    private String TAG = this.getClass().getCanonicalName();

    CustomSdpObserver(String logTag) {
        this.TAG = this.TAG + " " + logTag;
    }


    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        Log.d(TAG, "onCreateSuccess() called with: sessionDescription = [" + sessionDescription + "]");
    }

    @Override
    public void onSetSuccess() {
        Log.d(TAG, "onSetSuccess() called");
    }

    @Override
    public void onCreateFailure(String s) {
        Log.d(TAG, "onCreateFailure() called with: s = [" + s + "]");
    }

    @Override
    public void onSetFailure(String s) {
        Log.d(TAG, "onSetFailure() called with: s = [" + s + "]");
    }

}

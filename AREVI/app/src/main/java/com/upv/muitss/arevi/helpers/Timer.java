package com.upv.muitss.arevi.helpers;

import android.os.Handler;
import android.os.SystemClock;

public class Timer {
    private long millisecondTime, startTime, timeBuff, updateTime = 0L ;
    private int seconds, minutes, milliSeconds;
    private static Handler handler;
    private static Timer timer = null;

    private Timer(){
        handler = new Handler();
    }

    public static Timer getInstance() {
        if (handler == null){
            timer = new Timer();
            return timer;
        }
        return timer;
    }

    private Runnable runnable = new Runnable() {

        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecondTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);
            handler.postDelayed(this, 0);
        }

    };

    public void start() {
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    public void pause() {
        timeBuff += millisecondTime;
        handler.removeCallbacks(runnable);
    }

    public void reset() {
        millisecondTime = 0L;
        startTime = 0L;
        timeBuff = 0L;
        updateTime = 0L;
        seconds = 0;
        minutes = 0;
        milliSeconds = 0;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getMilliSeconds() {
        return milliSeconds;
    }

}

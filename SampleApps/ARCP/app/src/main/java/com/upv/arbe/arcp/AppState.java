package com.upv.arbe.arcp;

public class AppState {

    private int centerX;
    public int getCenterX() { return this.centerX; }
    public void setCenterX(int pCenterX) { this.centerX = pCenterX; }

    private int centerY;
    public int getCenterY() { return this.centerY; }
    public void setCenterY(int pCenterY) { this.centerY = pCenterY; }

    private boolean isTracking;
    public boolean getIsTracking() { return this.isTracking; }
    public void setIsTracking(boolean pIsTracking) { this.isTracking = pIsTracking; }

    private boolean isHitting;
    public boolean getIsHitting() { return this.isHitting; }
    public void setIsHitting(boolean pIsHitting) { this.isHitting = pIsHitting; }

    private boolean isFocusing;
    public boolean getIsFocusing() { return this.isFocusing; }
    public void setIsFocusing(boolean pIsFocusing) { this.isFocusing = pIsFocusing; }

    private float nodeAge;
    public float getNodeAge() { return this.nodeAge; }
    public void setNodeAge(float pNodeAge) { this.nodeAge = pNodeAge; }


    AppState(){
        initState();
    }

    public void initState(){
        centerX = 0;
        centerY = 0;
        isTracking = false;
        isHitting = false;
        isFocusing = false;
        nodeAge = 0;
    }
}

package com.upv.muitss.arevi.helpers;

import com.upv.muitss.arevi.entities.Profile;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;

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

    private boolean isCasting;
    public boolean getIsCasting() { return this.isCasting; }
    public void setIsCasting(boolean pIsCasting) { this.isCasting = pIsCasting; }

//    private float nodeAge;
//    public float getNodeAge() { return this.nodeAge; }
//    public void setNodeAge(float pNodeAge) { this.nodeAge = pNodeAge; }

    private User user;
    public User getUser() { return this.user; }
    public void setUser(User pUser) { this.user = pUser; }

    private UserInfo userInfo;
    public UserInfo getUserInfo() { return this.userInfo; }
    public void setUserInfo(UserInfo pUserInfo) { this.userInfo = pUserInfo; }

    private Profile profile;
    public Profile getProfile() { return this.profile; }
    public void setProfile(Profile pProfile) { this.profile = pProfile; }

    private static AppState instance;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
            instance.initState();
        }
        return instance;
    }

    private void initState(){
        centerX = 0;
        centerY = 0;
        isTracking = false;
        isHitting = false;
        isFocusing = false;
//        nodeAge = 0;
        user = new User();
        userInfo = new UserInfo();
        profile = new Profile();
    }
}


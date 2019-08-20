package com.upv.muitss.arevi.helpers;

import com.upv.muitss.arevi.entities.PolyAsset;
import com.upv.muitss.arevi.entities.Profile;
import com.upv.muitss.arevi.entities.Task;
import com.upv.muitss.arevi.entities.User;
import com.upv.muitss.arevi.entities.UserInfo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AppState {

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

    private Task task;
    public Task getTask() { return this.task; }
    public void setTask(Task pTask) { this.task = pTask; }
    private Queue<String> work;
    public boolean workIsEmpty() { return this.work.isEmpty(); }
    public String pollWork() { return this.work.poll(); }
    public void queueWork(List<String> pWork) { this.work.addAll(pWork); }

    private Queue<PolyAsset> polyQueue;
    public boolean polyQueueHasToLoad() { return this.polyQueue.size() < 2; }
    public boolean polyQueueIsEmpty() { return this.polyQueue.isEmpty(); }
    public PolyAsset pollPolyAsset() { return this.polyQueue.poll(); }
    public void queuePolyAsset(PolyAsset polyAsset) { this.polyQueue.add(polyAsset); }

    private static AppState instance;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
            instance.initState();
        }
        return instance;
    }

    private void initState(){
        isTracking = false;
        isHitting = false;
        isFocusing = false;
//        nodeAge = 0;
        user = new User();
        userInfo = new UserInfo();
        profile = new Profile();
        polyQueue = new LinkedList<>();
        task = new Task();
        work = new LinkedList<>(Arrays.asList(task.work.ids));
    }
}


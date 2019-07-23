package com.upv.arbe.arcp.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.upv.arbe.arcp.helpers.AppState;
import com.upv.arbe.arcp.MainActivity;
import com.upv.arbe.arcp.R;

import java.lang.ref.WeakReference;

public class MenuView extends LinearLayout {

    private static WeakReference<MainActivity> owner;
    private static final String TAG = "MenuView";
    private AppState appState;

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(WeakReference<MainActivity> pOwner) {
        owner = pOwner;
        assert owner.get() != null;

        appState = owner.get().getAppState();

        ImageView focus = new ImageView(getContext());
        focus.setImageResource(R.drawable.ic_center_focus_weak_black_60dp);
        focus.setContentDescription("focus");
        focus.setOnClickListener(view -> {
            appState.setIsFocusing(!appState.getIsFocusing());
            if (appState.getIsFocusing()) focus.setColorFilter(Color.RED);
            else focus.setColorFilter(Color.BLACK);
        });
        this.addView(focus);
    }
}

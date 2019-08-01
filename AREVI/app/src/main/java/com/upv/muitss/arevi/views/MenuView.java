package com.upv.muitss.arevi.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.upv.muitss.arevi.ArActivity;
import com.upv.muitss.arevi.R;
import com.upv.muitss.arevi.helpers.AppState;

import java.lang.ref.WeakReference;

public class MenuView extends LinearLayout {

    private static WeakReference<ArActivity> owner;
    private final String TAG = this.getClass().getCanonicalName();
    private AppState appState;

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(WeakReference<ArActivity> pOwner) {
        owner = pOwner;
        assert owner.get() != null;

        appState = AppState.getInstance();

        ImageView focus = new ImageView(getContext());
        focus.setImageResource(R.drawable.ic_center_focus_strong_black_60dp);
        focus.setContentDescription("focus");
        focus.setOnClickListener(view -> {
            appState.setIsFocusing(!appState.getIsFocusing());
            if (appState.getIsFocusing()) focus.setColorFilter(Color.RED);
            else focus.setColorFilter(Color.BLACK);
        });
        this.addView(focus);
    }
}
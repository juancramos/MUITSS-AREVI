package com.upv.muitss.arevi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
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

    private int tintColor;
    private int tintColorDisabled;
    private ImageView cast;

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(WeakReference<ArActivity> pOwner) {
        owner = pOwner;
        assert owner.get() != null;

        TypedArray ta = getContext().getTheme().obtainStyledAttributes(R.styleable.ViewStyle);
        tintColor = ta.getColor(R.styleable.ViewStyle_fillPrimaryColor, -1);
        tintColorDisabled = ta.getColor(R.styleable.ViewStyle_fillSecondaryColor, -1);

        appState = AppState.getInstance();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(160, 160);
        layoutParams.gravity = Gravity.CENTER;

        ImageView focus = new ImageView(getContext());
        focus.setImageResource(R.drawable.ic_center_focus_strong_black_60dp);
        focus.setContentDescription("focus");
        focus.setScaleType(ImageView.ScaleType.FIT_CENTER);
        focus.setAdjustViewBounds(true);
        focus.setLayoutParams(layoutParams);
        focus.setOnClickListener(view -> {
            appState.setIsFocusing(!appState.getIsFocusing());
            if (appState.getIsFocusing()) focus.setColorFilter(Color.RED);
            else focus.setColorFilter(tintColor);
        });
        this.addView(focus);
        cast = new ImageView(getContext());
        cast.setImageResource(R.drawable.ic_cast_black_24dp);
        cast.setScaleType(ImageView.ScaleType.FIT_CENTER);
        cast.setAdjustViewBounds(true);
        cast.setContentDescription("cast");
        cast.setLayoutParams(layoutParams);
        cast.setOnClickListener(view -> {
            appState.setIsCasting(!appState.getIsCasting());
            owner.get().arAttachWebRTCView();

            if (appState.getIsCasting()) cast.setColorFilter(Color.RED);
            else cast.setColorFilter(tintColor);
        });
        this.addView(cast);
    }

    public void setCastButtonEnabled(boolean enabled){
        cast.setEnabled(enabled);
        cast.setColorFilter(tintColorDisabled);
    }
}
package com.upv.arbe.arcp.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.upv.arbe.arcp.MainActivity;
import com.upv.arbe.arcp.R;

import java.lang.ref.WeakReference;

public class MenuView extends LinearLayout {

    private static WeakReference<MainActivity> owner;
    private static final String TAG = "MenuView";

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(WeakReference<MainActivity> pOwner) {
        owner = pOwner;
        assert owner.get() != null;

        ImageView focus = new ImageView(getContext());
        focus.setImageResource(R.drawable.ic_center_focus_weak_black_60dp);
        focus.setContentDescription("focus");
        focus.setOnClickListener(view -> owner.get().setFocusArView());
        this.addView(focus);
    }
}

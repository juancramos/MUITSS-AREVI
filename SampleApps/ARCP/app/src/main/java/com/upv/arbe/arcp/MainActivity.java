package com.upv.arbe.arcp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.upv.arbe.arcp.views.ArView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ArView fragment = (ArView)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        assert fragment != null;
        fragment.init(metrics);
    }
}

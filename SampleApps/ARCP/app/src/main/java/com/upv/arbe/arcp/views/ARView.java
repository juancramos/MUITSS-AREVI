package com.upv.arbe.arcp.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.ar.sceneform.ux.ArFragment;
import com.upv.arbe.arcp.R;
import com.upv.arbe.arcp.logic.ARModule;

public class ARView extends AppCompatActivity {

    private ARModule arModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arview);

        ArFragment arFragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        arModule = new ARModule(arFragment);
    }
}

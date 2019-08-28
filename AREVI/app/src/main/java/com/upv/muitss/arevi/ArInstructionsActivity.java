package com.upv.muitss.arevi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.upv.muitss.arevi.entities.Profile;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

public class ArInstructionsActivity extends AppCompatActivity implements ActivityMessage {

    private View focusView, triggerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_ar_instructions);

        boolean userLogin = Utils.getLogIn().isValidState();

        focusView = findViewById(R.id.ar_instructions_focus_view);
        triggerView = findViewById(R.id.ar_instructions_trigger_view);

        triggerView.setVisibility(View.GONE);

        if (userLogin) {
            runOnUiThread(() -> {
                AREVIRepository.getInstance().getApiProfile(Utils.getUserId(), this);
            });
        }
    }

    public void onEnterArButtonClick(View view) {
        Intent navigateIntent = new Intent(ArInstructionsActivity.this, ArActivity.class);
        startActivity(navigateIntent);
    }

    private void configurationChanged(boolean useGoogleCardboardBoolean) {
        if (useGoogleCardboardBoolean) {
            focusView.setVisibility(View.VISIBLE);

            triggerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public <T> void onResponse(T response) {
        if (response instanceof Profile){
            Profile p = (Profile) response;

            configurationChanged(p.getConfiguration().getUseGoogleCardboardBoolean());
        }
    }
}

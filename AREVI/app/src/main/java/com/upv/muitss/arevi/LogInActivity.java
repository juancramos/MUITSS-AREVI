package com.upv.muitss.arevi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

public class LogInActivity extends AppCompatActivity implements ActivityMessage {

    private UserLogIn userLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_log_in);

        userLogIn = Utils.getLogIn();

        EditText mEmailTxt = findViewById(R.id.text_input_email);
        mEmailTxt.setText(userLogIn.email);
        mEmailTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                userLogIn.email = Utils.validateInput(mEmailTxt);
            }
        });

        EditText passwordTxt = findViewById(R.id.text_input_password);
        passwordTxt.setText(userLogIn.password);
        passwordTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                userLogIn.password = Utils.validateInput(passwordTxt);
            }
        });
    }

    public void onLogInButtonClick(View view) {

        if (userLogIn.isValidState()){
            runOnUiThread(()->{
                Utils.popProgressDialog(this, "Loading...");
                AREVIRepository.getInstance().logIn(userLogIn,this);
            });
        }
    }

    public void onRegisterButtonClick(View view) { startProfileWizard(); }

    private void startProfileWizard(){
        Intent toAct = new Intent(this, PagerActivity.class);
        startActivity(toAct);
    }

    @Override
    public <T> void onResponse(T response) {

    }
}

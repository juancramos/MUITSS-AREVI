package com.upv.muitss.arevi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Constants;
import com.upv.muitss.arevi.helpers.UserPreferences;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.UserRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;


public class MainActivity extends AppCompatActivity implements ActivityMessage {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppState.getInstance();

        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.init(this);

        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_main);

        runOnUiThread(()->{
            Utils.popProgressDialog(this, "Loading...");
            UserRepository.getInstance().logIn(this);
        });
    }

    public void onStartArButtonClick(View view) {

        Intent navigateIntent = new Intent(this, ArActivity.class);
        startActivity(navigateIntent);

//         final int result = 1;
//         getNameScreenIntent.putExtra("callingActivity", "MainActivity");
//         startActivityForResult(getNameScreenIntent, result);
    }

    public void onStartWizardButtonClick(View view) {
        startProfileWizard();
    }

    private void startProfileWizard(){
        Intent toAct = new Intent(this, PagerActivity.class);
        startActivity(toAct);
    }

    @Override
    public void onRestart() {

        super.onRestart();

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public <T> void onResponse(T response) {
        if (response == null){
            startProfileWizard();
        }
        else if(response instanceof String && ((String)response).isEmpty()){
            startProfileWizard();
        }
    }
}

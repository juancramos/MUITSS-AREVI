package com.upv.muitss.arevi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.UserPreferences;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

public class MainActivity extends AppCompatActivity implements ActivityMessage {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isOnline()) return;

        AppState.getInstance();

        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.init(this);

        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_main);

        runOnUiThread(()->{
            Utils.popProgressDialog(this, "Loading...");
            AREVIRepository.getInstance().logIn(this);
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

    private void startProfileWizard(){
        Intent toAct = new Intent(this, PagerActivity.class);
        startActivity(toAct);
    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}

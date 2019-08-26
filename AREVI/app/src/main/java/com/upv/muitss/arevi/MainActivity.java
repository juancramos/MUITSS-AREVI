package com.upv.muitss.arevi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.upv.muitss.arevi.entities.UserLogIn;
import com.upv.muitss.arevi.helpers.AppState;
import com.upv.muitss.arevi.helpers.Constants;
import com.upv.muitss.arevi.helpers.UserPreferences;
import com.upv.muitss.arevi.helpers.Utils;
import com.upv.muitss.arevi.logic.web.implementations.AREVIRepository;
import com.upv.muitss.arevi.logic.web.interfaces.ActivityMessage;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivityMessage {

    public static Context appContext;
    private Button startArBtn, manageProfileBtn;
    private View loginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isOnline()) return;

        AppState.getInstance();

        UserPreferences userPreferences = UserPreferences.getInstance();
        userPreferences.init(this);

        setTheme(Utils.getSavedThemeStyle());
        setContentView(R.layout.activity_main);

        loginView = findViewById(R.id.update_app_layout_view);
        loginView.setVisibility(View.GONE);
        startArBtn = findViewById(R.id.activity_main_ar_scene_btn);
        startArBtn.setEnabled(false);
        manageProfileBtn = findViewById(R.id.activity_main_manage_profile_btn);
        manageProfileBtn.setEnabled(false);

        appContext = this.getBaseContext();

        UserLogIn userLogIn = Utils.getLogIn();
        String savedTheme = Utils.getSavedTheme();

        if (userLogIn.isValidState()){
            runOnUiThread(()->{
                Utils.popProgressDialog(this, Utils.getResourceString(R.string.dialog_loading_text));
                AREVIRepository.getInstance().logIn(userLogIn,this);
            });
        }
        else if (savedTheme == null){
            startProfileWizard();
        }
        else startLogIn();
    }

    public void onStartArButtonClick(View view) {
        if (AppState.getInstance().getUser().isLocal()) {
            Utils.showToast(this, Utils.getResourceString(R.string.toast_register_AREVI));
            startProfileWizard();
            return;
        }

        Intent navigateIntent = new Intent(this, ArActivity.class);
        startActivity(navigateIntent);

//         final int result = 1;
//         getNameScreenIntent.putExtra("callingActivity", "MainActivity");
//         startActivityForResult(getNameScreenIntent, result);
    }

    public void onLogInButtonClick(View view) {
        startProfileWizard();
    }

    private void startProfileWizard(){
        Intent toAct = new Intent(MainActivity.this, PagerActivity.class);
        startActivity(toAct);
    }

    private void startLogIn(){
        Intent toAct = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(toAct);
    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();

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
            startLogIn();
        }
        else if(response instanceof String && ((String)response).isEmpty()){
            startLogIn();
        }
        else if(response instanceof List && !((List)response).isEmpty() && ((List)response).contains(Constants.CURRENT_BUILD)){
            String buildVersion = (String) ((List)response).get(1);
            String localBuildVersion = Utils.getCurrentBuild();
            if (TextUtils.isEmpty(localBuildVersion)) {
                Utils.saveCurrentBuild(buildVersion);
            }
            else if (!TextUtils.equals(buildVersion, localBuildVersion)){
                loginView.setVisibility(View.VISIBLE);
            }
        }
        else {
            enableButtons();
            AREVIRepository.getInstance().getApiBuildVersion(this);
        }
    }

    private void enableButtons() {
        startArBtn.setEnabled(true);
        manageProfileBtn.setEnabled(true);
    }

    public void onUpdateAppButtonClick(View view) {
        String url = Utils.getResourceString(R.string.app_releases_url);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Utils.showToast(this, Utils.getResourceString(R.string.toast_error_loading_release));
        }
    }
}

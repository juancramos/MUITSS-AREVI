 package com.upv.muitss.arevi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.upv.muitss.arevi.helpers.AppState;


 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppState.getInstance();
    }

     public void onStartArButtonClick(View view) {

         Intent navigateIntent = new Intent(this, ArActivity.class);
         startActivity(navigateIntent);

//         final int result = 1;
//         getNameScreenIntent.putExtra("callingActivity", "MainActivity");
//         startActivityForResult(getNameScreenIntent, result);
     }

     public void onStartWizardButtonClick(View view) {

         Intent toAct = new Intent(this, PagerActivity.class);
         startActivity(toAct);
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
 }

package com.example.comp211.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import static com.example.comp211.quiz.R.id.toolbar;

public class SingleQuiz extends AppCompatActivity {

    public String PlayerName;

    private boolean phoneDevice = true; // force portrait mode when device is a phone

    //configuring the SingleQuiz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_quiz);
        PlayerName = getIntent().getStringExtra("PlayerName");

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Portrait Orientation setting
        // determine screen size
        int screenSize = getResources().getConfiguration().screenLayout &
        Configuration.SCREENLAYOUT_SIZE_MASK;

        // if device is a tablet, set phoneDevice to false
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
        screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false; // not a phone-sized device

        // if running on phone-sized device, allow only portrait orientation
        if (phoneDevice)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }


    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();
        SingleQuizFragment quizFragment = (SingleQuizFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.quizFragment);
        Log.d("startQuiz", PlayerName);
        if (PlayerName != null)
            Log.d("startQuiz", "not null");
        if (quizFragment != null)
            Log.d("quizFragment", "not null");
        quizFragment.getPlayerName(PlayerName);
        quizFragment.resetQuiz();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // check orientation first
        int orientation = getResources().getConfiguration().orientation;

        // Inflate menu only if orientation is portrait
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        Intent questionListIntent = new Intent(this, Questions_List_Activity.class);
        startActivity(questionListIntent);

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {}


}

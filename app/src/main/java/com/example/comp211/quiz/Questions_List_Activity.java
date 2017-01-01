package com.example.comp211.quiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class Questions_List_Activity extends AppCompatActivity {

    public String PlayerName;
    public int Score;
    public int[] questionslist = new int[11];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlayerName = getIntent().getStringExtra("PlayerName");
        questionslist = getIntent().getIntArrayExtra("questionList");
        Score = getIntent().getIntExtra("score",0);

        Log.d("Arith ql act sc", Integer.toString(Score));
        Log.d("Arith ql act", Integer.toString(questionslist[1]));

        setContentView(R.layout.activity_questions__list_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Questions_List_ActivityFragment questionslistFragment = (Questions_List_ActivityFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.questions_list_fragment);
        questionslistFragment.getQuestionList(questionslist);
    }

    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();
        Questions_List_ActivityFragment questionslistFragment = (Questions_List_ActivityFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.questions_list_fragment);

        questionslistFragment.getPlayerName(PlayerName);
        questionslistFragment.receiveScore(Score);

        /*Log.d("startMenu", PlayerName);
        if (PlayerName != null)
            Log.d("startmenu", "not null");
        if (questionslistFragment != null)
            Log.d("questionlistFragment", "not null");
        */
        //questionslistFragment.getPlayerName(PlayerName);
    }
}

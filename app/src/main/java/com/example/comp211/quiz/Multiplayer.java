package com.example.comp211.quiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class Multiplayer extends AppCompatActivity {

    //TAG for debugging purposes
    private static final String TAG = "Arith MP";

    //quiz components
    private String Player1, Player2;
    private static final int NUMBER_OF_QUESTIONS = 10; // no of questions in the quiz
    private int correctAnswers1, correctAnswers2; // number of correct guesses
    int qid1 = 0, qid2 = 0;
    private Question currentQ;

    // for the question database
    private QuestionCreate db;
    private List<Question> quesList;

    private Animation shakeAnimation; // animation for incorrect guess
    private Handler handler;
    private LinearLayout quizLinearLayout; // layout that contains the quiz
    private TextView questionNumberTextView; // shows current question #
    private TextView mathsQuestionTextView; // displays a maths question
    private TextView answerTextView; // displays correct answer
    private Button button1, button2, button3, button4, button5, button6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        // get Player1 and Player2 names
        Player1 = getIntent().getStringExtra("Player1");
        Player2 = getIntent().getStringExtra("Player2");

        Log.d(TAG, Player1);
        Log.d(TAG, Player2);

        // in case no name is entered by both/either players
        if (Player1.equals(""))
            Player1 = "Player 1";
        if (Player2.equals(""))
            Player2 = "Player 2";


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        // load the shake animation that's used for incorrect answers
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times

        // get references to GUI components
        quizLinearLayout = (LinearLayout) findViewById(R.id.content_multiplayer);
        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mathsQuestionTextView = (TextView) findViewById(R.id.mathsQuestionTextView);
        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        button1.setOnClickListener(guessButtonListener);
        button2.setOnClickListener(guessButtonListener);
        button3.setOnClickListener(guessButtonListener);
        button4.setOnClickListener(guessButtonListener);
        button5.setOnClickListener(guessButtonListener);
        button6.setOnClickListener(guessButtonListener);

        db = new QuestionCreate(this);
        db.resetQuestions();
        quesList = db.getAllQuestions();  // this will fetch all questions

    }

    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();
        loadNextQuestion();
    }

    private void loadNextQuestion() {

        if (qid1 <= qid2) { //when it is Player1's turn
            questionNumberTextView.setText(getString(R.string.mpQuestions, //using string resource
                    Player1, //playername
                    (qid1 + 1), //current question
                    NUMBER_OF_QUESTIONS)); //total questions -
            answerTextView.setText(""); // clear answerTextView
            currentQ = quesList.get(qid1); // prepare the current question
            qid1++;
        }
        else { //for Player2
            questionNumberTextView.setText(getString(R.string.mpQuestions,
                    Player2,
                    (qid2 + 1),
                    NUMBER_OF_QUESTIONS));
            answerTextView.setText(""); // clear answerTextView
            currentQ = quesList.get(qid2); // prepare the current question
            qid2++;
        }
        // assign values to guess buttons
        //for (int row = 0; row < guessRows; row++) {
        mathsQuestionTextView.setText(currentQ.getQUESTION());
        button1.setText(currentQ.getANSA());
        button2.setText(currentQ.getANSB());
        button3.setText(currentQ.getANSC());
        button4.setText(currentQ.getANSD());
        button5.setText("Cheat");
        button6.setText("Skip");
        enableButtons();

        Log.d(TAG, currentQ.getANSWER().toString());

    }

    // animates the entire quizLinearLayout on or off screen
    private void animate(boolean animateOut) {
        // calculate center x and center y
        int centerX = (quizLinearLayout.getLeft() +
                quizLinearLayout.getRight()) / 2; // calculate center x
        int centerY = (quizLinearLayout.getTop() +
                quizLinearLayout.getBottom()) / 2; // calculate center y

        // calculate animation radius
        int radius = Math.max(quizLinearLayout.getWidth(),
                quizLinearLayout.getHeight());

        Animator animator;

        // if the quizLinearLayout should animate out rather than in
        if (animateOut) {
            // create circular reveal animation
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, radius, 0);
            animator.addListener(
                    new AnimatorListenerAdapter() {
                        // called when the animation finishes
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Log.d(TAG, "finishes first question");
                        }
                    }
            );
        } else { // if the quizLinearLayout should animate in
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, 0, radius);
        }

        animator.setDuration(500); // set animation duration to 500 ms
        animator.start(); // start the animation
    }

    // called when a guess Button is touched
    public View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = currentQ.getANSWER();

            if (guess.equals(answer)) { // if the guess is correct

                if (qid1 > qid2)
                    ++correctAnswers1; // increment the number of correct answers
                else if (qid1 == qid2)
                    ++correctAnswers2;
                else
                    Log.d(TAG, "impossible case check");

                // display correct answer in green text
                answerTextView.setText(getString(R.string.answerIsCorrect, currentQ.getANSWER()));
                answerTextView.setTextColor(
                        getResources().getColor(R.color.correct_answer,
                                Multiplayer.this.getTheme()));

                disableButtons(); // disable all guess Buttons
                // answer is correct but quiz is not over
                // load the next question after a 2-second delay
                /*handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                animate(true); // animate the flag off the screen
                            }
                        }, 2000); // 2000 milliseconds for 2-second delay
                */

            }
            //if use cheat button and if use skip button needs to be included here
            else if (guess.equals("Cheat")) {
                // display cheat message and answer in red
                answerTextView.setText(getString(R.string.cheat, answer));
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, Multiplayer.this.getTheme()));
            }

            else if (guess.equals("Skip")) {
                // display "Why skip..." in red
                answerTextView.setText(R.string.skip);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, Multiplayer.this.getTheme()));
            }
            else {
                // answer was incorrect
                disableButtons(); // disable all guess Buttons
                mathsQuestionTextView.startAnimation(shakeAnimation); // play shake

                // display "Incorrect!" in red
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, Multiplayer.this.getTheme()));
            }
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            animate(true);
                        }
                    }, 1000); // 2000 milliseconds for 2-second delay

            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if ((qid2) == NUMBER_OF_QUESTIONS)    // check if quiz ends ie all questions have been answered
                                quizEnds();
                            else
                                loadNextQuestion();
                        }
                    }, 1500); // 2000 milliseconds for 2-second delay
            // animate() runs 500ms so together with its handler that delays by 1000ms, right as the
            // animation ends, the next action (quizEnds() or loadNextQuestion()) should load.
        }
    };

    public void quizEnds() {

        AlertDialog.Builder display = new AlertDialog.Builder(Multiplayer.this);
        if (correctAnswers1 > correctAnswers2)
            display.setTitle(getString(R.string.mpAlertDialog, Player1));
        else if (correctAnswers1 < correctAnswers2)
            display.setTitle(getString(R.string.mpAlertDialog, Player2));
        else
            display.setTitle(R.string.mpTieAlertDialog);

        display.setMessage(getString(R.string.mpResults, Player1,
                correctAnswers1,
                Player2,
                correctAnswers2));

        display.setNegativeButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        Intent l = new Intent(Multiplayer.this, Multiplayer.class);
                        l.putExtra("Player1", Player1);
                        l.putExtra("Player2", Player2);
                        startActivity(l);
                        finish();
                    }
                }
        );

        display.setPositiveButton(R.string.backToMenu, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int id) {
                Intent f = new Intent(Multiplayer.this, MainActivity.class);
                startActivity(f);
            }
        });


        display.setCancelable(false);

        //create alert dialog that was built
        AlertDialog scorePopup = display.create();
        scorePopup.show();

    }


    // utility method that disables all answer Buttons
    private void disableButtons() {
        /*
        //for (int row = 0; row < guessRows; row++) {
        for (int row = 0; row < 2; row++) {
            LinearLayout guessRow = guessLinearLayouts[row];
            for (int i = 0; i < guessRow.getChildCount(); i++)
                guessRow.getChildAt(i).setEnabled(false);
        }
        */
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button5.setEnabled(false);
        button6.setEnabled(false);
    }

    // utility method that disables all answer Buttons
    private void enableButtons() {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button5.setEnabled(true);
        button6.setEnabled(true);
    }


}


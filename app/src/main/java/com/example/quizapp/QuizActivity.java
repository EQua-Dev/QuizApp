package com.example.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";

//    we want to set the countdown timer 30 seconds ie 30000 milliseconds
    private static final long COUNTDOWN_IN_MILLIS = 30000;

//    we create variables to store the values that we want to keep when we rotate our device
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";


    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCategory;
    private TextView textViewDifficulty;
    private TextView textViewCountdown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

//    variable to store the default color of the option radioButton (which will be changed later depending on the given answer)
    private ColorStateList textColorDefaultRb;

//    variable to store the color change of the timer text
    private ColorStateList textColorDefaultcd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

//    list array variable to contain all the questions
    private ArrayList<Question> questionList;

//    variable to store the number of answered questions
    private int questionCounter;

//    variable to store the total number of questions answered
    private int questionCountTotal;

//    variable to store the current Question being displayed
    private Question currentQuestion;

    private int score;

//    variable to determine what happens when the button is clicked; lock Question if not answered, next if answered
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);
        textViewDifficulty = findViewById(R.id.text_view_difficulty);
        textViewCountdown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

//        here we get the in-built color objects and set it to the radioButtons
        textColorDefaultRb = rb1.getTextColors();

//        here we get the in-built color objects and set it to the countdownTextView
        textColorDefaultcd = textViewCountdown.getTextColors();

//        we fetch the data of the intent where the difficulty level was selected in the StartingActivity class
        Intent intent = getIntent();

//        we then get all the intents passed from the startActivity and set them to variables defined in this class
        int categoryID = intent.getIntExtra(StartingActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(StartingActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(StartingActivity.EXTRA_DIFFICULTY);

//        we display the current difficulty level on the screen
        textViewCategory.setText("Category: " + categoryName);
//        we display the current difficulty level on the screen
        textViewDifficulty.setText("Difficulty: " + difficulty);

//        we check there is no data in the savedInstance, then we run our database display afresh
        if (savedInstanceState == null){

//        creates a class variable reference to the QuizDbHelper class
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(QuizActivity.this);

//        populates the list array variable we created in this class with the getQuestions method we created in the DbHelper
//            we then set the questions to be fetched to be in relation to the difficulty and category selected
        questionList = dbHelper.getQuestions(categoryID, difficulty);
        questionCountTotal = questionList.size();

//        sets the questions to be displayed in a random order
        Collections.shuffle(questionList);

//        the method that defines the display of each Question is called
        showNextQuestion();
        }else{
//            if there is data in the savedInstance state, we load it
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
////            if there are no questions left in the restored bundle
//            if (questionList == null){
//                finish();
//            }

//            we calculate the size of the questionCountTotal from the size of the questionList restored
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

//            if the question was not answered before being saved in the bundle, we continue the timer
            if (!answered){
                startCountDown();
            }else{
//                if the question had been answered we call the following methods
                updateCountDownText();
                showSolution();
            }
        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                when the confirm button is clicked and the question has not been confirmed as answered,
                if (!answered) {
//                    we check if any of the option has been checked
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
//                        if yes we call the checkAnswer method
                        checkAnswer();
                    } else {
//                        if none has been checked, we make a toast
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    if the question has been confirmed as answered and the next button is clicked, the showNextQuestion method is called
                    showNextQuestion();
                }

            }
        });

    }


    private void showNextQuestion(){
//        sets each color of the radioButtons to the default color
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);

//        ensures that all the radio buttons are not selected
        rbGroup.clearCheck();

//        checks if the number of questions answered is more than the total available questions
        if (questionCounter < questionCountTotal){
//            updates the current Question with the next Question on the list
            currentQuestion = questionList.get(questionCounter);

//            sets the text to display the Question
            textViewQuestion.setText(currentQuestion.getQuestion());

//            sets the options to display the options for the Question
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

//            increments the Question count by one
            questionCounter++;

            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");

//            we set the initial timeLeftInMillis to be 30000ms once the next question is shown
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else{
            finishQuiz();
        }

    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                we set the millisUntilFinished to the timeLeftInMillis variable to enable us access it from outside this method
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
//                without this explicit assignment, the countdown will  only display 1sec when finished
                timeLeftInMillis = 0;
                updateCountDownText();

//                we call the checkAnswer method to be implemented on whatever is on the screen when the is finished...
//                you can set a different function
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
//        the counntdown time is converted from millis to minutes
        int minutes = (int) (timeLeftInMillis / 1000) / 60;

//        the seconds part of the timer is calculated and converted from the remainder of the minutes conversion
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

//        we set the format of the minute and second display to a string variable timeFormatted
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes,seconds);

        textViewCountdown.setText(timeFormatted);

//        in this loop we set the countdown textColor to red if it is below 10seconds
        if (timeLeftInMillis < 10000){
            textViewCountdown.setTextColor(Color.RED);
        }else{
//            ...and set it to its default if it isn't
            textViewCountdown.setTextColor(textColorDefaultcd);
        }
    }

    private void checkAnswer(){
        answered = true;

//        stops the timer if an answer has been selected
        countDownTimer.cancel();

//        gets the layout id of the selected radioButton
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());

//        the selected radioButton is saved as the answer of the question
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

//        we compare the selected answer with that provided in the database
        if (answerNr ==  currentQuestion.getAnswerNr()){
//            if the answers correspond, we increment the score
            score++;
            textViewScore.setText("Score " + score);
        }
//        we do not put an else because we want to show the solution to the question either ways
//        thus the showSolution method is called
        showSolution();

    }

    private void showSolution(){
//        when we show the solution, we set all radioButtons to a default of red
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

//        we check for the option that is correct and set certain functions
        switch (currentQuestion.getAnswerNr()){
//            we check for the case where each option is correct and set that option color to green
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
//                the break button is used to ensure that the following statements are not executed nonetheless
        }

//        if the questions are still remaining;
        if (questionCounter < questionCountTotal){
            buttonConfirmNext.setText("Next");
        }else{
            buttonConfirmNext.setText("Finish");
        }
    }
    private void finishQuiz(){
//        creates an intent to pass the score value to the starting screen
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);

//        we check if the quiz was successfully finished and if yes, pass the resultIntent
        setResult(RESULT_OK,resultIntent);
        finish();
    }



    @Override
    public void onBackPressed() {
//        checks if the time which the back button was pressed twice successively is < 2secs and calls the finishQuiz method
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
//        we save the system current time in the backPressedTime variable
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        checks if the timer is running
        if (countDownTimer != null){
//            cancels the timer when the app is finished to prevent it from running in background
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);

//        we use ParcelableArrayList because our questions arrayList neither comprises of only int nor String
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);

    }
}


    //    we save the current score of the current user in an instance state
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("score", score);
//    }


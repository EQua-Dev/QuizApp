package com.example.quizapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class StartingActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1;

//    since the difficulty and category are selected in this activity, we create the intent activities that will be sent to the QuizActivity
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

//    the variable name for the shared preference is defined
    public static final String SHARED_PREFS = "sharedPrefs";

//    the key name to store the highscore in the shared preference is defined
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private TextView textViewHighscore;
    private Spinner spinnerCategory;
    private Spinner spinnerDifficulty;

    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);

//        we call the loadCategories method once the quiz activity is launched
        loadCategories();

//        we call the loadDifficultyLevels method once the quiz activity is launched
        loadDifficultyLevels();

//        we call the loadHighscore method once the quiz activity is launched
        loadHighscore();

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);

        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }

    private void startQuiz(){
//        we fetch the selected category item and store it to a selectedCategory variable
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();

//        we then fetch the ID of the category item selected from our Category class and store it in the categoryID variable
        int categoryID = selectedCategory.getId();

//        we also fetch the name of the category selected from the Category class and pass it as a string to be displayed and store it in the categoryName string variable
        String categoryName = selectedCategory.getName();

//        we save the value of the difficulty selected and save it in the difficulty string variable
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

//        we then attach the difficulty level selected while passing the intent to start the quiz
        Intent intent = new Intent(this, QuizActivity.class);

//        we create an intent to the quizActivity passing the ID of the category selected
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);

//        we also create an intent to the quizActivity passing the name of the category selected
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);

//        we then also create an intent to the quizActivity passing the difficulty selected
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);

//        startActivityForResult is used over just startActivity when you want to get a result from the activity it starts
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        we compare the equality of the auto generated requestCode to that request_code_quiz that we created
        if (requestCode == REQUEST_CODE_QUIZ){
//            if compare for the equality of the auto generated resultCode with the Result_OK we set in our QuizActivity
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
//                we check if the current score is greater than the current score, we call the updateHighscore method
                if (score > highscore){
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadCategories(){
//        we create a variable reference to the QuizDbHelper singleton class called dbHelper and invoke it on this activity
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);

//        we then populate the categories into a List
        List<Category> categories = dbHelper.getAllCategories();

//        we then set the categories into an auto generated android spinner layout
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        we then set the categories list to our spinner in our xml
        spinnerCategory.setAdapter(adapterCategories);

    }

    private void loadDifficultyLevels(){
        //        we create a string array variable and store the difficulty array stored in the Question class
        String[] difficultyLevels = Question.getAllDifficultyLevels();

//        we create an array adapter to contain the contents of the spinner difficultyLevels
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);

//        just to make the spinner dropdown better looking
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        we set the arrayAdapter to the spinner variable
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }

    private void loadHighscore(){
//        we load the current highscore from the sharedPreference
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);

    }

    private void updateHighscore(int highscoreNew){
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);

//        we save the current highscore in sharedPrefence
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}

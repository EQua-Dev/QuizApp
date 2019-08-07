package com.example.quizapp;

import android.os.Parcel;
import android.os.Parcelable;

//  we implement the Parcelable object to enable us add the questionList in our instance state in our QuizActivity
public class Question implements Parcelable {

//    we will create the variables for the difficulty levels in this class as constants in order to enable us access and modify them easily in future
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

//    we then create a variable to pass the id of the question
    private int id;
    private String question;
    private String option1;
    private String option2;
    private String option3;

//    we create a number variable for the correct answer; 1 for option1, 2 for option2 and 3 for option3
    private int answerNr;

//    we create a member variable to store the value of the selectd difficulty level
    private String difficulty;

//    we then create a variable to pass the category id as a string
    private int categoryID;

    public Question(){

    }

//    Constructor to contain the content of the various created variables
    public Question(String question, String option1, String option2, String option3,
                    int answerNr, String difficulty, int categoryID) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNr = answerNr;
        this.difficulty = difficulty;
        this.categoryID = categoryID;
    }


//    the parse in object is used to retain certain data when the orientation changes
//    NOTE: the order of variables in the ParcelIn should be exactly the same with the writeToParcel
    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        answerNr = in.readInt();
        difficulty = in.readString();
        categoryID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeInt(answerNr);
        dest.writeString(difficulty);
        dest.writeInt(categoryID);
    }

//    this method is created when you implement the parcelable object on the Question class
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    //    we create a String array method to contain all our difficulty levels as string so that we can easily call this method whenever we need out difficulty level list
    public static String[] getAllDifficultyLevels(){
        return new String[]{
                DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD,
        };
    }
}


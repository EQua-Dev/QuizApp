package com.example.quizapp;

//this contract class is used to create certain constants that will later be used in our SQL

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {

    }

//    we programmatically create a new table for the categories
public static class CategoriesTable implements BaseColumns {
    public static final String TABLE_NAME = "quiz_categories";
    public static final String COLUMN_NAME = "name";
}
//    we create a new class for each table in our database

//    we implement BaseColumns interface to automatically add the constants of the id with the supported naming convention
    public static class QuestionsTable implements BaseColumns {

//        we give a programmatic structural representation of each constant of our table, assigning them cell containers

    public static final String TABLE_NAME = "quiz_questions";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_OPTION1 = "option1";
    public static final String COLUMN_OPTION2 = "option2";
    public static final String COLUMN_OPTION3 = "option3";
    public static final String COLUMN_ANSWER_NR = "answer_nr";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_CATEGORY_ID = "category_id";



}
}

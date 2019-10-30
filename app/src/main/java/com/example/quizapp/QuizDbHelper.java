package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

//we import the every content of the QuizContract class
import com.example.quizapp.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION  = 1;

//    we make this QuizDbHelper class a singleton
    private static QuizDbHelper instance;

//    creates a reference to the actual database and enables us to access it from here
    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    synchronized: if we want to access this QuizDbHelper from multiple threads
    public static synchronized QuizDbHelper getInstance(Context context){

//        checks if the QuizDbHelper has not been initialized:
        if (instance == null){
//            then creates a new QuizDbHelper to be initialized on the whole application lifecycle
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        this method will be called the very first time we try to access the database
        this.db = db;

//        the sql to create the categories table is written
        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

//        this is the method where the table is created

//        Here the database table rows and columns are actually created in the SQL convention (the SQL schema is defined here)
//        NOTE: This SQL is very sensitive to its lexis and structures

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

//        we execute the categories table before the questions table is executed
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);

//        the database creation is executed
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

//        the fillCategoriesTable method is called to insert the questions once the table is created
        fillCategoriesTable();

//        the fillQuestionTable method is called to insert the questions once the table is created
        fillQuestionsTable();

    }

//    the method of table upgrade is defined in this onUpgrade method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        the following statement tells the SQL to delete the Categories table and recreate it with the updated values
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);

//        the following statement tells the SQL to delete the Questions table and recreate it with the updated values
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

//    this method creates ForeignKey constraints which will avoid input of an invalid id
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    private void fillCategoriesTable(){
//        here we create the actual categories to be stored in our database and referenced to all questions
        Category c1 = new Category("Programming");
        addCategory(c1);
        Category c2 = new Category("Sports");
        addCategory(c2);
        Category c3 = new Category("History");
        addCategory(c3);
    }

    private void addCategory(Category category){
//        this method populates the questions to the COLUMN_NAME in the QuestionContract class
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
//        here we create the actual questions to be stored in our database and invoked to the screen
        Question q1 = new Question("Which of these is not a programming language",
                "Python", "Java", "Cobra", 3, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQuestion(q1);
        Question q2 = new Question("A person who programs is called",
                "Sailor", "Programmer", "Farmer", 2, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQuestion(q2);
        Question q3 = new Question("Which of these best describes loop statement",
                "do-while", "tell-if", "go-when", 1, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQuestion(q3);
        Question q4 = new Question("Which of these is not a data type",
                "String", "Language", "Integer", 2, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQuestion(q4);
        Question q5 = new Question("Which of these is a mark-up language",
                "LISP", "HTML", "ADA", 2, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQuestion(q5);
        Question q6 = new Question("Which of these is not a script language",
                "Python", "Java", "Javascript", 2, Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        addQuestion(q6);
        Question q7 = new Question("IDE stands for",
                "Integrated Development Environment", "Independent Developers Entity", "Internal Deployment Extension", 1, Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        addQuestion(q7);
        Question q8 = new Question("Which of these was released first",
                "Python", "Java", "Swift", 1, Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        addQuestion(q8);
        Question q9 = new Question("Which of these is used in for Android Development",
                "Python", "Java", "C#", 2, Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        addQuestion(q9);
        Question q10 = new Question("Which of these is not a pillar of Object Oriented Programming",
                "Polymorphism", "Architecture Independence", "Inheritance", 2, Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        addQuestion(q10);
        Question q11 = new Question("Which of these was the first programming language",
                "ADA", "FORTRAN", "Plankalkul", 3, Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        addQuestion(q11);
        Question q12 = new Question("Which of these is a method",
                "Private final string method;", "public static void main{}", "private void method(){}", 3, Question.DIFFICULTY_HARD,
                Category.PROGRAMMING);
        addQuestion(q12);
        Question q13 = new Question("A boolean statement put into code that the programmer expects to always be true is called",
                "Harness", "Assertion", "Exception", 2, Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        addQuestion(q13);
        Question q14 = new Question("JavaScript is a trademark of",
                "Sun Microsystems", "Google", "Oracle", 3, Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        addQuestion(q14);
        Question q15 = new Question("Which of is not a functional programming language",
                "F#", "Joy", "Fortran", 3, Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        addQuestion(q15);
        Question q16 = new Question("How many players are in a playing Basketball team",
                "5", "6", "10", 1, Question.DIFFICULTY_EASY, Category.SPORTS);
        addQuestion(q16);
        Question q17 = new Question("Races are run in",
                "Bowls", "Pitches", "Tracks", 3, Question.DIFFICULTY_EASY, Category.SPORTS);
        addQuestion(q17);
        Question q18 = new Question("Which of these is not a sport",
                "Skiing", "Mumble Talking", "Cycling", 2, Question.DIFFICULTY_EASY, Category.SPORTS);
        addQuestion(q18);
        Question q19 = new Question("Which of these is noy involved in football",
                "Coach", "Overseer", "Refree", 2, Question.DIFFICULTY_EASY, Category.SPORTS);
        addQuestion(q19);
        Question q20 = new Question("A football match if played for a minimum of",
                "60 minutes", "45 minutes", "90 minutes", 3, Question.DIFFICULTY_EASY, Category.SPORTS);
        addQuestion(q20);
        Question q21 = new Question("A basketball game is played for a minimum of",
                "90 minutes", "48 minutes", "60 minutes", 2, Question.DIFFICULTY_MEDIUM, Category.SPORTS);
        addQuestion(q21);
        Question q22 = new Question("Which team has the most UEFA Europa Trophy",
                "Sevilla", "Napoli", "Inter Milan", 1, Question.DIFFICULTY_MEDIUM, Category.SPORTS);
        addQuestion(q22);
        Question q23 = new Question("How many umpires officiate a Basketball game",
                "4", "1", "3", 3, Question.DIFFICULTY_MEDIUM, Category.SPORTS);
        addQuestion(q23);
        Question q24 = new Question("Who has the most F1 Drivers' Championship titles",
                "Michael Schumacher", "Lewis Hamilton", "Niki Lauda", 1, Question.DIFFICULTY_MEDIUM, Category.SPORTS);
        addQuestion(q24);
        Question q25 = new Question("How does FIFA make their money",
                "TV rights", "Nations' contributions", "World Bank makes it for them", 1, Question.DIFFICULTY_MEDIUM, Category.SPORTS);
        addQuestion(q25);
        Question q26 = new Question("Which of these teams have never been relegated in the Spanish La Liga",
                "Valencia CF", "Athletic Bilbao", "Atletico Madrid", 2, Question.DIFFICULTY_HARD, Category.SPORTS);
        addQuestion(q26);
        Question q27 = new Question("The first FIFA World Cup was held in what year, hosted by who and won by who",
                "1950, Brazil, Germany", "1942, Sweden, France", "1930, Uruguay, Uruguay", 3, Question.DIFFICULTY_HARD, Category.SPORTS);
        addQuestion(q27);
        Question q28 = new Question("Who has won the most Grand Slam Championships",
                "Rafael Nadal", "Roger Federer", "Rod Laver", 2, Question.DIFFICULTY_HARD, Category.SPORTS);
        addQuestion(q28);
        Question q29 = new Question("How many teams participate in the Rugby World Cup",
                "20", "32", "7", 1, Question.DIFFICULTY_HARD, Category.SPORTS);
        addQuestion(q29);
        Question q30 = new Question("What is the current world record time for the Men's Half-Marathon",
                "60:02 minutes", "42:19 minutes", "58:18 minutes", 3, Question.DIFFICULTY_HARD, Category.SPORTS);
        addQuestion(q30);
        Question q31 = new Question("Who is the first man to go to space",
                "Niel Armstrong", "Yuri Gagarin", "Buzz Adin", 2, Question.DIFFICULTY_EASY, Category.TECHNOLOGY);
        addQuestion(q31);
        Question q32 = new Question("Which of these is not a computer",
                "Calculator", "Notepad", "Mobile Phone", 2, Question.DIFFICULTY_EASY, Category.TECHNOLOGY);
        addQuestion(q32);
        Question q33 = new Question("An output that produces sound is called",
                "Audio", "Image", "Video", 1, Question.DIFFICULTY_EASY, Category.TECHNOLOGY);
        addQuestion(q33);
        Question q34 = new Question("Which of these defnes a computer in the right order",
                "Process-Input-Output", "Output-Input-Process", "Input-Process-Output", 3, Question.DIFFICULTY_EASY, Category.TECHNOLOGY);
        addQuestion(q34);
        Question q35 = new Question("A moving image is called",
                "Audio", "Video", "Picture", 2, Question.DIFFICULTY_EASY, Category.TECHNOLOGY);
        addQuestion(q35);
        Question q36 = new Question("The 3rd generation computers used",
                "Micro Processors", "Vacuum Tubes", "Transistors", 1, Question.DIFFICULTY_MEDIUM, Category.TECHNOLOGY);
        addQuestion(q36);
        Question q37 = new Question("Which of these is associated with time",
                "Meterology", "Hydrostatics", "Chronology", 3, Question.DIFFICULTY_MEDIUM, Category.TECHNOLOGY);
        addQuestion(q37);
        Question q38 = new Question("What was the name of Niel Armstrong's lander",
                "Apollo", "Eagle", "Alien", 2, Question.DIFFICULTY_MEDIUM, Category.TECHNOLOGY);
        addQuestion(q38);
        Question q39 = new Question("Sound is measured in",
                "Amp", "Voltage", "Decibels", 3, Question.DIFFICULTY_MEDIUM, Category.TECHNOLOGY);
        addQuestion(q39);
        Question q40 = new Question("Sound travels faster in",
                "Gases/ Air", "Liquid", "Solid", 3, Question.DIFFICULTY_MEDIUM, Category.TECHNOLOGY);
        addQuestion(q40);
        Question q41 = new Question("The first Android version was",
                "Android Mini", "Android Donut", "Android Jelly Bean", 2, Question.DIFFICULTY_HARD, Category.TECHNOLOGY);
        addQuestion(q41);
        Question q42 = new Question("In what year was the first mobile phone invented and which?",
                "1978, Nokia 3310", "1995, T-Mobile", "1992, Nokia 1011", 3, Question.DIFFICULTY_HARD, Category.TECHNOLOGY);
        addQuestion(q42);
        Question q43 = new Question("In what country and century was the first rocket launched",
                "China, 10th  century", "USA, 18th century", "Russia, 15th century", 1, Question.DIFFICULTY_HARD, Category.TECHNOLOGY);
        addQuestion(q43);
        Question q44 = new Question("Which of these games is the oldest",
                "1942", "Tetris", "Space Invaders", 3, Question.DIFFICULTY_HARD, Category.TECHNOLOGY);
        addQuestion(q44);
        Question q45 = new Question("Who invented wireless communication",
                "Nikolas Tesla", "Heinrich Hertz", "Gugliemo Marconi", 3, Question.DIFFICULTY_HARD, Category.TECHNOLOGY);
        addQuestion(q45);




    }

    private void addQuestion(Question question){
//        this method populates the questions to the COLUMN_QUESTION in the QuestionContract class
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());

        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }



    //    Method to retrieve the data from the database
public ArrayList<Question> getAllQuestions() {
    ArrayList<Question> questionList = new ArrayList<>();
    db = getReadableDatabase();
    Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
//        checks if the TABLE_NAME has been moved to the first column:
    if (c.moveToFirst()) {
//        fills in the rest of the columns with the Question, options and answers



    do {
        Question question = new Question();
        question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
        question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
        question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
        question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
        question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
        question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
        question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
        question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));

        questionList.add(question);
//                while filling them in the next available columns
            }
            while (c.moveToNext());
        }


        c.close();
        return questionList;
    }

    public List<Category> getAllCategories(){
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();

//        the SQL statement below queries the db to fetch from the table all questions that match with the selected category
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

//        checks if the TABLE_NAME has been moved to the first column:
        if (c.moveToFirst()){
//            fills in the rest of the columns with the category of each question
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);

//                while filling them in the next available columns
            }while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

//    this method acts as a filter of all questions in relation to the selected difficulty and category
    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

////        we create a new string array in the DB and the content will bw the difficulty level
//        String[] selectArgs = new String[]{difficulty};
//
////        the SQL statement below queries the db to fetch from the table all questions that match with the selected difficulty level
//        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
//                " WHERE " + QuestionsTable.COLUMN_DIFFICULTY + " = ?", selectArgs);
//        checks if the TABLE_NAME has been moved to the first column:

//        here we create a db filter for the difficulty and category in respect to what was selected
        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";

//        the values of the difficulty and category are stored in a string array
        String [] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db. query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (c.moveToFirst()) {
//        fills in the rest of the columns with the Question, options and answers



            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));

                questionList.add(question);
//                while filling them in the next available columns
            }
            while (c.moveToNext());
        }


        c.close();
        return questionList;
    }
}

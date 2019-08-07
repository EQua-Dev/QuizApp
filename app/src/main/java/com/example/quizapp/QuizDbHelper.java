package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.setForeignKeyConstraintsEnabled(true);
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
        Question q1 = new Question("Programming, Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQuestion(q1);
        Question q2 = new Question("Sports, Medium: B is correct",
                "A", "B", "C", 2, Question.DIFFICULTY_MEDIUM, Category.SPORTS);
        addQuestion(q2);
        Question q3 = new Question("History, Hard: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_HARD, Category.HISTORY);
        addQuestion(q3);
        Question q4 = new Question("Programming, Hard: B is correct",
                "A", "B", "C", 2, Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        addQuestion(q4);
        Question q5 = new Question("Sports, Easy: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_EASY, Category.SPORTS);
        addQuestion(q5);
        Question q6 = new Question("History, Medium: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_MEDIUM, Category.HISTORY);
        addQuestion(q6);
        Question q7 = new Question("History, Medium: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_MEDIUM, 4);
        addQuestion(q7);
        Question q8 = new Question("Extinct, Medium: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_MEDIUM, 5);
        addQuestion(q8);




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

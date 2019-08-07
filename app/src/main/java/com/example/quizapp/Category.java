package com.example.quizapp;


// we create a category class store the content of our category table
public class Category {

    //    we create a number variable for each category; 1 for PROGRAMMING, 2 for SPORTS and 3 for HISTORY to act as a reference to populate our database the first time

    public static final int PROGRAMMING = 1;
    public static final int SPORTS = 2;
    public static final int HISTORY = 3;

    private int id;
    private String name;

    public Category(){

    }

    //    the following getter and setter objects will be used to alter/ invoke the content of the category table outside of this class

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

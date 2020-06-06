package com.example.fun;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Todo {
    private static final String TIME_FORMAT = "dd.MM.yyyy  HH:mm";

    private String todoText;
    private boolean clicked;
    private String creationTime;
    private String modificationTime;


    private static int count = 0;

    private int id;


    public Todo(String todoText,boolean clicked) {
        this.id = count;
        count++;
        this.todoText = todoText;
        this.clicked = clicked;
        this.creationTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(new Date());
        this.modificationTime = creationTime;
    }
    public void markDone(){
        clicked = true;
    }
    public void markUnDone(){
        clicked = false;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
    }

    public String getTodoText() {
        return todoText;
    }

    public boolean isClicked() {
        return clicked;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    public static void setCount(int count) {
        Todo.count = count;
    }


}

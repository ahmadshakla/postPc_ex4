package com.example.fun;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TodoHandler extends Application {
    private final static String SHARED_PREFERENCE = "shared preference";
    private final static String TODOS_ARR = "todoos_ARR";
    private final static String DEFAULT_ARR = "[]";

    private ArrayList<Todo> todoArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        initArrays();
    }


    /**
     * initializes the ArrayLists that hold the todos
     */
    private void initArrays(){
        String allTodos = sharedPreferences.getString(TODOS_ARR,DEFAULT_ARR);
        if (allTodos.equals("[]")){
            todoArrayList = new ArrayList<>();
        }
        else {
            Type type = new TypeToken<ArrayList<Todo>>() {
            }.getType();
            todoArrayList = gson.fromJson(allTodos, type);
        }
    }

    /**
     * @return the arrayList containing the todos.
     */
    public ArrayList<Todo> getTodoArrayList() {
        return todoArrayList;
    }

    /**
     * updates the content of the todoList
     * @param newTodos the new updated todoList
     */
    public void updateSharedPreferance(ArrayList<Todo> newTodos){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String todoTodos = gson.toJson(newTodos);
        editor.putString(TODOS_ARR, todoTodos);
        editor.apply();
    }

    /**
     * replaces the todoItem at index index with the new todoItem
     * @param todo a new todoItem
     * @param index the index which we want to replace
     */
    public void replace(Todo todo, int index){
        todoArrayList.remove(index);
        todoArrayList.add(index,todo);
    }


    /**
     * adds a new todoItem to the recyclerView and updates it
     * @param todo the new todoItem we want to add
     */
    public void addToRecyclerView(Todo todo) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        todoArrayList.add(todo);
        String todoTodos = gson.toJson(todoArrayList);
        editor.putString(TODOS_ARR, todoTodos);
        editor.apply();
    }

    /**
     * deletes a todoItem from the recyclerView and updates it
     * @param index the index of the todoItem that we want to delete
     */
    public void deleteFromRecyclerView(int index) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        deleteTodo(index);
        String todoTodos = gson.toJson(todoArrayList);
        editor.putString(TODOS_ARR, todoTodos);
        editor.apply();
    }


    /**
     * deletes the todo item from all the relevant data structures
     * @param index the index of the todoItem
     */
    private void deleteTodo(int index) {
        if (index>=0 && index<todoArrayList.size()){
            todoArrayList.remove(index);
        }
    }

}
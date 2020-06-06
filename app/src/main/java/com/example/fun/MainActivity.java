package com.example.fun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /* data structures to store the todoItems*/
    private ArrayList<Todo> todos;



    /*Magic numbers*/
    private static final String TODO_TO_MOVE = "todoToMove";
    private final static String SHARED_PREFERENCE = "shared preference";
    private final static String TODOS_ARR = "todoos_ARR";
    private final static String ROTATION_STRING = "rotationString";
    private final static String ROTATION_BOOL = "rotationBoolean";
    private final static String EMPTY_STRING_ERR = "You must write something!";
    private final static String TAG = "TODOBOOM";
    private static final String MOVED_INDEX = "moved index";
    public static final String MODIFIED_INDEX = "modified index";
    private static final int DELETED = 0;
    private static final int UNMARKED = 1;

    /*variables*/
    private TextView insertion;
    private RecyclerView recyclerView;
    private Context context;
    private TodoAdapter todoAdapter;
    private TodoHandler todoHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Date c = Calendar.getInstance().getTime();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        todos = new ArrayList<>();
        todoHandler = ((TodoHandler) getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        recyclerView = findViewById(R.id.recycle_view);
        context = this;
        insertion = findViewById(R.id.editText2);
        final Button button = findViewById(R.id.button);
        todoAdapter = new TodoAdapter(todoHandler, this);
        if (savedInstanceState != null) {
            update();
        } else {
            todos = todoHandler.getTodoArrayList();
            ;
            Todo.setCount(todos.size());
            Log.i(TAG, todos.size() + " is the current number of todos");
            update();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = insertion.getText().toString();
                if (message.length() != 0 && !message.matches(" *")) {
                    insertion.setText("");
                    todoHandler.addToRecyclerView(new Todo(message, false));
                    update();
                    editor.apply();
                } else {
                    Toast.makeText(context, EMPTY_STRING_ERR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        ArrayList<String> tasks = new ArrayList<>();
        ArrayList<Todo> mySavedTodos = new ArrayList<>(todoHandler.getTodoArrayList());
        boolean[] clicked = new boolean[mySavedTodos.size()];
        int j = 0;
        for (Todo todo : mySavedTodos) {
            tasks.add(todo.getTodoText());
            clicked[j] = mySavedTodos.get(j).isClicked();
            j++;
        }
        outState.putStringArrayList(ROTATION_STRING, tasks);
        outState.putBooleanArray(ROTATION_BOOL, clicked);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String todoJson = data.getStringExtra(TODO_TO_MOVE);
                int index = data.getIntExtra(MOVED_INDEX, 0);
                Todo updatedTodo = new Gson().fromJson(todoJson, Todo.class);
                todoHandler.replace(updatedTodo, index);
                update();

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                int updatedIndex = data.getIntExtra(MOVED_INDEX, 0);
                int newState = data.getIntExtra(MODIFIED_INDEX, 0);
                if (newState == DELETED) {
                    if (todoHandler.getTodoArrayList().size() > 0) {
                        todoHandler.deleteFromRecyclerView(updatedIndex);
                        todoAdapter.notifyItemRemoved(updatedIndex);
                        update();
                    }
                } else if (newState == UNMARKED) {
                    if (todoHandler.getTodoArrayList().size() > 0) {
                        Todo old = todoHandler.getTodoArrayList().get(updatedIndex);
                        old.markUnDone();
                        todoHandler.replace(old, updatedIndex);
                        update();
                    }
                }
            }
        }
    }

    /**
     * updates the recyclerView on the screen
     */
    public void update() {
        todoAdapter.setTodo(todoHandler.getTodoArrayList());
        recyclerView.setAdapter(todoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public Context getContext() {
        return MainActivity.this;
    }

}
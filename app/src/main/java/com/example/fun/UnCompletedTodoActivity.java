package com.example.fun;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UnCompletedTodoActivity extends AppCompatActivity {
    private static final String TODO_TO_MOVE = "todoToMove";
    private static final int UNFINISHED_CODE = 1;
    private static final String TIME_FORMAT = "dd.MM.yyyy  HH:mm";
    private static final String CREATE_TODO = "Created on: ";
    private static final String MODIFY_TODO = "Last modified: ";
    private static final String CONTENT_CHANGED = "the TODO content was changed successfully! ";
    public static final String MOVED_INDEX = "moved index";

    private Button apply;
    private Button mark;
    private TextView modification;
    private EditText text;
    private Gson gson;
    private int index;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_completed_todo);
        apply = findViewById(R.id.un_mark_button);
        mark = findViewById(R.id.delete_button);
        TextView creation = findViewById(R.id.creation_textView);
        modification = findViewById(R.id.modification_textView);
        text = findViewById(R.id.editText);
        final Intent intent = getIntent();
        gson = new Gson();
        final Todo todo = gson.fromJson(intent.getStringExtra(TODO_TO_MOVE), Todo.class);
        index = intent.getIntExtra(MOVED_INDEX,0);
        text.setText(todo.getTodoText());
        creation.setText(CREATE_TODO + todo.getCreationTime());
        modification.setText(MODIFY_TODO + todo.getModificationTime());
        handleModifyingButton(intent, todo);
        handleMarkingButton(intent, todo);
    }

    /**
     * handles what to do when we press the mark button
     *
     * @param intent the intent we are updating
     * @param todo   the todoItem we are handling
     */
    private void handleMarkingButton(final Intent intent, final Todo todo) {
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo.markDone();
                String updatedTodo = gson.toJson(todo);
                intent.putExtra(TODO_TO_MOVE, updatedTodo);
                intent.putExtra(MOVED_INDEX, index);
                Toast.makeText(UnCompletedTodoActivity.this, "TODO " + todo.getTodoText() + " is now DONE. BOOM!",
                        Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * handles what to do when we press the modify button
     *
     * @param intent the intent we are updating
     * @param todo   the todoItem we are handling
     */
    private void handleModifyingButton(final Intent intent, final Todo todo) {
        apply.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String newText = text.getText().toString();
                todo.setTodoText(newText);
                String newTime = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(new Date());
                todo.setModificationTime(newTime);
                modification.setText(MODIFY_TODO + newTime);
                String updatedTodo = gson.toJson(todo);
                intent.putExtra(TODO_TO_MOVE, updatedTodo);
                intent.putExtra(MOVED_INDEX, index);
                setResult(RESULT_OK, intent);
                Toast.makeText(UnCompletedTodoActivity.this, CONTENT_CHANGED,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

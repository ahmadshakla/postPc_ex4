package com.example.fun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class CompletedTodoActivity extends AppCompatActivity {
    private static final String TODO_TO_MOVE = "todoToMove";
    private final static String TITLE = "Information";
    private final static String MESSAGE = "Are you sure that you want to delete this TODO?";
    public static final String MOVED_INDEX = "moved index";
    public static final String MODIFIED_INDEX = "modified index";
    private static final int DELETED = 0;
    private static final int UNMARKED = 1;

    private TextView todoText;
    private Button unMark;
    private Button delete;
    private Gson gson;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo);
        todoText = findViewById(R.id.todo_text);
        unMark = findViewById(R.id.un_mark_button);
        delete = findViewById(R.id.delete_button);
        final Intent intent = getIntent();
        gson = new Gson();
        final Todo todo = gson.fromJson(intent.getStringExtra(TODO_TO_MOVE), Todo.class);
        index = intent.getIntExtra(MOVED_INDEX,0);
        todoText.setText(todo.getTodoText());
        handleUnMarkButton(intent, todo);
        handleDeleteButton(intent,todo);
    }

    /**
     * handles what to do when we press the mark button
     *
     * @param intent the intent we are updating
     * @param todo   the todoItem we are handling
     */
    private void handleUnMarkButton(final Intent intent, final Todo todo) {
        unMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo.markUnDone();
                String updatedTodo = gson.toJson(todo);
                intent.putExtra(TODO_TO_MOVE, updatedTodo);
                intent.putExtra(MODIFIED_INDEX,UNMARKED);
                Toast.makeText(CompletedTodoActivity.this, "TODO " + todo.getTodoText() + " is now un-marked!",
                        Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    /**
     * handles what to do when we press the mark button
     *
     * @param intent the intent we are updating
     * @param todo   the todoItem we are handling
     */
    private void handleDeleteButton(final Intent intent, final Todo todo) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CompletedTodoActivity.this);
                builder.setTitle(TITLE)
                        .setMessage(MESSAGE).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent.putExtra(MOVED_INDEX,index);
                        intent.putExtra(MODIFIED_INDEX,DELETED);
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }
}

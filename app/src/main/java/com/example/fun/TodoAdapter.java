package com.example.fun;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private static final String TODO_TO_MOVE = "todoToMove";
    private static final int UNCHECKED_CODE = 1;
    private static final int CHECKED_CODE = 2;
    private Context context;
    private Context mainContext;
    private SharedPreferences sharedPreferences;
    private final static String SHARED_PREFERENCE = "shared preference";
    public static final String MOVED_INDEX = "moved index";
    private final static String TODOS_ARR = "todoos_ARR";
    private final static String TITLE = "Information";
    private final static String MESSAGE = "Are you sure that you want to delete this TODO?";
    private ArrayList<Todo> todoItems;

    public TodoAdapter(Context context, Context mainContext) {
        this.todoItems = new ArrayList<>();
        this.context = context;
        this.mainContext = mainContext;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);


    }

    /**
     * sets the contents of the todoArray located in this class
     *
     * @param old the old todoArray
     */
    public void setTodo(ArrayList<Todo> old) {
        todoItems.clear();
        todoItems.addAll(old);
        ((TodoHandler)context).updateSharedPreferance(todoItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Todo todoItem = todoItems.get(position);
        final int taskNum = position + 1;
        holder.textView.setText(taskNum + "- " + todoItem.getTodoText());
        if (todoItem.isClicked()) {
            holder.textView.setAlpha(0.4f);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.apply();
        } else {
            holder.textView.setAlpha(1f);
        }
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!todoItem.isClicked()) {
                    normalClickHandler(UNCHECKED_CODE, position);
                }
                else {
                    normalClickHandler(CHECKED_CODE,position);
                }
            }
        });



    }


    /**
     * handled what to do when the textview is clicked
     * @param code the code of the intent
     */
    private void normalClickHandler(final int code, int position) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
//        todoItems.get(position).markDone();
        Intent intent;
        String todoJson = gson.toJson(todoItems.get(position));
        if (code == UNCHECKED_CODE){
            intent = new Intent(mainContext, UnCompletedTodoActivity.class);
//            intent.putExtra(MOVED_INDEX,position);
//            intent.putExtra(TODO_TO_MOVE, todoJson);
//            ((MainActivity) mainContext).startActivityForResult(intent, code);
        }
        else{
            intent = new Intent(mainContext, CompletedTodoActivity.class);
//            intent.putExtra(MOVED_INDEX,position);
//            intent.putExtra(TODO_TO_MOVE, todoJson);
//            ((MainActivity) mainContext).startActivityForResult(intent, code);
        }
        intent.putExtra(MOVED_INDEX,position);
        intent.putExtra(TODO_TO_MOVE, todoJson);
        ((MainActivity) mainContext).startActivityForResult(intent, code);
//        TodoHandler todoHandlerContext = (TodoHandler) context;
//        todoHandlerContext.setTodos(todoItems);
//        String json = gson.toJson(todoItems);
//        editor.putString(TODOS_ARR, json);
//        editor.apply();
//        holder.textView.setAlpha(0.4f);
    }

    /**
     * handled what to do when we we long click on a textView
     *
     * @param position its position in the array
     */
    private void longClickHandler(final int position) {
        final TodoHandler todoHandlerContext = (TodoHandler) context;
        AlertDialog.Builder builder = new AlertDialog.Builder((mainContext));
        builder.setTitle(TITLE)
                .setMessage(MESSAGE).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                todoHandlerContext.deleteFromRecyclerView(position);
                ((MainActivity) mainContext).update();
                notifyItemRemoved(position);

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}

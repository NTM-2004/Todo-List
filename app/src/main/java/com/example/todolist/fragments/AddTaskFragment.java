package com.example.todolist.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.todolist.R;
import com.example.todolist.db.DB;
import com.example.todolist.model.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskFragment extends Fragment {

    private TextInputEditText etTitle, etContent, etCategory;
    private Button btnDeadline, btnSave, btnDelete;
    private TextView tvSelectedDeadline;
    
    private DB db;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    
    private int taskId = -1;
    private Task currentTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        etTitle = view.findViewById(R.id.etTitle);
        etContent = view.findViewById(R.id.etContent);
        etCategory = view.findViewById(R.id.etCategory);
        btnDeadline = view.findViewById(R.id.btnDeadline);
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);
        tvSelectedDeadline = view.findViewById(R.id.tvSelectedDeadline);
        
        db = new DB(getContext());
        
        if (getArguments() != null) {
            taskId = getArguments().getInt("taskId", -1);
        }
        
        if (taskId != -1) {
            loadTaskData();
            btnDelete.setVisibility(View.VISIBLE);
        }
        
        btnDeadline.setOnClickListener(v -> showDateTimePicker());
        btnSave.setOnClickListener(v -> saveTask());
        btnDelete.setOnClickListener(v -> deleteTask());
    }

    private void loadTaskData() {
        currentTask = db.getTask(taskId);
        if (currentTask != null) {
            etTitle.setText(currentTask.getTitle());
            etContent.setText(currentTask.getContent());
            etCategory.setText(currentTask.getCategory());
            if (currentTask.getDeadline() != null) {
                calendar.setTimeInMillis(currentTask.getDeadline());
                tvSelectedDeadline.setText(dateFormat.format(new Date(currentTask.getDeadline())));
            }
        }
    }

    private void showDateTimePicker() {
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            
            new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                tvSelectedDeadline.setText(dateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (taskId == -1) {
            Task newTask = new Task(title, content, category, System.currentTimeMillis(), calendar.getTimeInMillis(), 0);
            db.addTask(newTask);
            Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
        } else {
            currentTask.setTitle(title);
            currentTask.setContent(content);
            currentTask.setCategory(category);
            currentTask.setDeadline(calendar.getTimeInMillis());
            db.updateTask(currentTask);
            Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
        }
        
        Navigation.findNavController(getView()).navigateUp();
    }

    private void deleteTask() {
        if (taskId != -1) {
            db.deleteTask(taskId);
            Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigateUp();
        }
    }
}

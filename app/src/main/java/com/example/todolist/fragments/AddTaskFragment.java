package com.example.todolist.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.todolist.R;
import com.example.todolist.db.DB;
import com.example.todolist.model.Task;
import com.example.todolist.notification.DeadlineNotificationManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskFragment extends Fragment {

    private TextInputEditText etTitle, etContent;
    private AutoCompleteTextView spinnerCategory;
    private ChipGroup cgPriority;
    private Button btnDeadline, btnSave, btnDelete;
    private TextView tvSelectedDeadline;
    private SwitchCompat switchNotify;

    private DB db;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    private int taskId = -1;
    private Task currentTask;
    private boolean deadlineSet = false;

    private static final String[] CATEGORIES = {"Study", "Work", "Personal", "Health", "Finance", "Other"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DB(getContext());

        etTitle = view.findViewById(R.id.etTitle);
        etContent = view.findViewById(R.id.etContent);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        cgPriority = view.findViewById(R.id.cgPriority);
        btnDeadline = view.findViewById(R.id.btnDeadline);
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);
        tvSelectedDeadline = view.findViewById(R.id.tvSelectedDeadline);
        switchNotify = view.findViewById(R.id.switchNotify);

        // Setup category dropdown
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        spinnerCategory.setAdapter(catAdapter);

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
            spinnerCategory.setText(currentTask.getCategory(), false);

            // Priority
            switch (currentTask.getPriority()) {
                case Task.PRIORITY_MEDIUM: ((Chip) cgPriority.findViewById(R.id.chipMedium)).setChecked(true); break;
                case Task.PRIORITY_HIGH:   ((Chip) cgPriority.findViewById(R.id.chipHigh)).setChecked(true); break;
                default:                   ((Chip) cgPriority.findViewById(R.id.chipLow)).setChecked(true);
            }

            if (currentTask.getDeadline() != null && currentTask.getDeadline() > 0) {
                deadlineSet = true;
                calendar.setTimeInMillis(currentTask.getDeadline());
                tvSelectedDeadline.setText(dateFormat.format(new Date(currentTask.getDeadline())));
            }

            switchNotify.setChecked(currentTask.isNotifyEnabled());
        }
    }

    private void showDateTimePicker() {
        new DatePickerDialog(requireContext(), (v, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            new TimePickerDialog(requireContext(), (v2, hour, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                deadlineSet = true;
                tvSelectedDeadline.setText(dateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private int getSelectedPriority() {
        int chipId = cgPriority.getCheckedChipId();
        if (chipId == R.id.chipMedium) return Task.PRIORITY_MEDIUM;
        if (chipId == R.id.chipHigh) return Task.PRIORITY_HIGH;
        return Task.PRIORITY_LOW;
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String category = spinnerCategory.getText().toString().trim();
        if (category.isEmpty()) category = "Other";

        if (title.isEmpty()) {
            etTitle.setError("Vui lòng nhập tiêu đề");
            return;
        }

        int priority = getSelectedPriority();
        long deadline = deadlineSet ? calendar.getTimeInMillis() : 0L;
        boolean notify = switchNotify.isChecked();

        if (taskId == -1) {
            Task newTask = new Task(title, content, category, System.currentTimeMillis(),
                    deadline, Task.STATUS_INCOMPLETE, priority, notify);
            long id = db.addTask(newTask);
            newTask.setId((int) id);
            if (notify && deadline > 0) {
                DeadlineNotificationManager.scheduleDeadlineNotification(requireContext(), newTask);
            }
            Toast.makeText(getContext(), "✅ Task đã thêm!", Toast.LENGTH_SHORT).show();
        } else {
            currentTask.setTitle(title);
            currentTask.setContent(content);
            currentTask.setCategory(category);
            currentTask.setDeadline(deadline);
            currentTask.setPriority(priority);
            currentTask.setNotifyEnabled(notify);
            db.updateTask(currentTask);
            if (notify && deadline > 0) {
                DeadlineNotificationManager.scheduleDeadlineNotification(requireContext(), currentTask);
            } else {
                DeadlineNotificationManager.cancelNotification(requireContext(), taskId);
            }
            Toast.makeText(getContext(), "✅ Task đã cập nhật!", Toast.LENGTH_SHORT).show();
        }

        Navigation.findNavController(requireView()).navigateUp();
    }

    private void deleteTask() {
        if (taskId != -1) {
            DeadlineNotificationManager.cancelNotification(requireContext(), taskId);
            db.deleteTask(taskId);
            Toast.makeText(getContext(), "🗑 Task đã xóa", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigateUp();
        }
    }
}

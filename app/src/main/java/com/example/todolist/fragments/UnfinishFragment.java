package com.example.todolist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.R;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.db.DB;
import com.example.todolist.model.Task;

import java.util.List;

public class UnfinishFragment extends Fragment implements TaskAdapter.OnTaskStatusChangeListener {

    private RecyclerView rvUnfinishedTasks;
    private TaskAdapter adapter;
    private DB db;

    public UnfinishFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unfinish, container, false);

        rvUnfinishedTasks = view.findViewById(R.id.rvUnfinishedTasks);
        db = new DB(getContext());

        loadUnfinishedTasks();

        return view;
    }

    private void loadUnfinishedTasks() {
        List<Task> tasks = db.getUnfinishedTasks();
        if (adapter == null) {
            adapter = new TaskAdapter(tasks, this);
        } else {
            adapter.updateData(tasks);
        }
        rvUnfinishedTasks.setAdapter(adapter);
    }

    @Override
    public void onStatusChanged(Task task, boolean isDone) {
        task.setStatus(isDone ? 1 : 0);
        db.updateTask(task);
        // In this fragment, when a task is done, it should disappear from the list
        if (isDone) {
            loadUnfinishedTasks();
        }
    }

    @Override
    public void onTaskClick(Task task) {
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", task.getId());
        Navigation.findNavController(getView()).navigate(R.id.fragment_add_task, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUnfinishedTasks();
    }
}

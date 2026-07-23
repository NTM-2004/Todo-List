package com.example.todolist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.R;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.db.DB;
import com.example.todolist.model.Task;
import com.example.todolist.viewmodel.SearchViewModel;

import java.util.List;

public class TaskFragment extends Fragment implements TaskAdapter.OnTaskStatusChangeListener {
    
    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private DB db;
    private SearchViewModel searchViewModel;

    public TaskFragment() {}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        
        rvTasks = view.findViewById(R.id.rvTasks);
        db = new DB(getContext());
        
        loadTasks();
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        searchViewModel.getSearchQuery().observe(getViewLifecycleOwner(), query -> {
            if (adapter != null) {
                adapter.getFilter().filter(query);
            }
        });
    }

    private void loadTasks() {
        List<Task> tasks = db.getAllTasks();
        if (adapter == null) {
            adapter = new TaskAdapter(tasks, this);
        } else {
            adapter.updateData(tasks);
        }
        rvTasks.setAdapter(adapter);
    }

    @Override
    public void onStatusChanged(Task task, boolean isDone) {
        task.setStatus(isDone ? 1 : 0);
        db.updateTask(task);
        // Optional: Refresh list if needed, but here it's fine
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
        loadTasks();
    }
}

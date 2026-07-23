package com.example.todolist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.R;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.db.DB;
import com.example.todolist.model.Task;

import java.util.List;

public class TaskFragment extends Fragment implements TaskAdapter.OnTaskStatusChangeListener {
    
    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private DB db;

    public TaskFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        
        rvTasks = view.findViewById(R.id.rvTasks);
        db = new DB(getContext());
        
        loadTasks();
        
        return view;
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onStatusChanged(Task task, boolean isDone) {
        task.setStatus(isDone ? 1 : 0);
        db.updateTask(task);
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

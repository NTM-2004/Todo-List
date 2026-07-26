package com.example.todolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.db.DB;
import com.example.todolist.model.Task;
import com.example.todolist.viewmodel.TaskViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment implements TaskAdapter.OnTaskActionListener {

    private static final String ARG_FILTER = "filter";

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private DB db;
    private int filter;
    private String currentSort = DB.SORT_BY_CREATED;
    private String currentSearch = "";
    private List<Task> tasks = new ArrayList<>();
    private TextView tvEmpty;

    public static TaskListFragment newInstance(int filter) {
        TaskListFragment f = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FILTER, filter);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filter = getArguments().getInt(ARG_FILTER, DB.FILTER_ALL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DB(getContext());
        recyclerView = view.findViewById(R.id.rvTasks);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter(tasks, this);
        recyclerView.setAdapter(adapter);

        setupSwipeToDelete();
        
        // Setup ViewModel observation
        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        
        viewModel.getSearchQuery().observe(getViewLifecycleOwner(), query -> {
            currentSearch = query;
            loadTasks();
        });
        
        viewModel.getSortOrder().observe(getViewLifecycleOwner(), sort -> {
            currentSort = sort;
            loadTasks();
        });
    }

    private void loadTasks() {
        if (db == null) return;
        tasks = db.getTasksSortedFiltered(filter, currentSort, currentSearch);
        if (adapter != null) adapter.updateData(tasks);

        if (tvEmpty != null) {
            tvEmpty.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task deletedTask = tasks.get(position);
                db.deleteTask(deletedTask.getId());
                tasks.remove(position);
                adapter.notifyItemRemoved(position);

                Snackbar.make(requireView(), "Task đã xóa", Snackbar.LENGTH_LONG)
                        .setAction("Hoàn tác", v -> {
                            deletedTask.setId(0);
                            long newId = db.addTask(deletedTask);
                            deletedTask.setId((int) newId);
                            tasks.add(position, deletedTask);
                            adapter.notifyItemInserted(position);
                            tvEmpty.setVisibility(View.GONE);
                        }).show();
                
                if (tasks.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStatusChanged(Task task, boolean isDone) {
        task.setStatus(isDone ? Task.STATUS_COMPLETE : Task.STATUS_INCOMPLETE);
        db.updateTask(task);
        loadTasks();
    }

    @Override
    public void onTaskClick(Task task) {
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", task.getId());
        Navigation.findNavController(requireView()).navigate(R.id.fragment_add_task, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }
}

package com.example.todolist.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.todolist.db.DB;
import com.example.todolist.fragments.TaskListFragment;

public class TaskPagerAdapter extends FragmentStateAdapter {

    private final int userId;
    private final int[] filters;

    public TaskPagerAdapter(@NonNull Fragment fragment, int userId, DB db, int[] filters, String[] titles) {
        super(fragment);
        this.userId = userId;
        this.filters = filters;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return TaskListFragment.newInstance(filters[position], userId);
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }
}

package com.example.todolist.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.todolist.db.DB;
import com.example.todolist.fragments.TaskListFragment;

public class TaskPagerAdapter extends FragmentStateAdapter {

    private final int[] filters;

    public TaskPagerAdapter(@NonNull Fragment fragment, DB db, int[] filters, String[] titles) {
        super(fragment);
        this.filters = filters;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return TaskListFragment.newInstance(filters[position]);
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }
}

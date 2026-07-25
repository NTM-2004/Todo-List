package com.example.todolist.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todolist.R;
import com.example.todolist.adapter.TaskPagerAdapter;
import com.example.todolist.auth.SessionManager;
import com.example.todolist.db.DB;
import com.example.todolist.viewmodel.TaskViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private EditText etSearch;
    private ImageButton btnSort;

    private DB db;
    private SessionManager session;
    private int userId;
    private TaskViewModel viewModel;

    private int currentFilter = DB.FILTER_ALL;
    private String currentSort = DB.SORT_BY_CREATED;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DB(getContext());
        session = new SessionManager(requireContext());
        userId = session.getCurrentUserId();
        
        // Use activity scope for shared ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        etSearch = view.findViewById(R.id.etSearch);
        btnSort = view.findViewById(R.id.btnSort);

        setupTabs();
        setupSearch();
        setupSortFilter();
    }

    private void setupTabs() {
        String[] tabs = {"Tất cả", "Chưa xong", "Hoàn thành"};
        int[] filters = {DB.FILTER_ALL, DB.FILTER_INCOMPLETE, DB.FILTER_COMPLETE};

        TaskPagerAdapter pagerAdapter = new TaskPagerAdapter(this, userId, db, filters, tabs);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabs[position])).attach();
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString().trim());
            }
        });
    }

    private void setupSortFilter() {
        btnSort.setOnClickListener(v -> {
            FilterSortBottomSheet sheet = new FilterSortBottomSheet(
                    currentSort, currentFilter,
                    (sort, filter) -> {
                        currentSort = sort;
                        currentFilter = filter;
                        viewModel.setSortOrder(sort);
                    });
            sheet.show(getParentFragmentManager(), "filter_sort");
        });
    }
}

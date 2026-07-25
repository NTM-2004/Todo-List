package com.example.todolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.R;
import com.example.todolist.db.DB;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class FilterSortBottomSheet extends BottomSheetDialogFragment {

    public interface OnApplyListener {
        void onApply(String sort, int filter);
    }

    private String currentSort;
    private int currentFilter;
    private OnApplyListener listener;

    public FilterSortBottomSheet(String sort, int filter, OnApplyListener listener) {
        this.currentSort = sort;
        this.currentFilter = filter;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_filter_sort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChipGroup cgSort = view.findViewById(R.id.cgSort);
        ChipGroup cgFilter = view.findViewById(R.id.cgFilter);
        Button btnApply = view.findViewById(R.id.btnApply);

        // Restore sort selection
        if (currentSort.equals(DB.SORT_BY_DEADLINE)) {
            ((Chip) view.findViewById(R.id.chipSortDeadline)).setChecked(true);
        } else if (currentSort.equals(DB.SORT_BY_PRIORITY)) {
            ((Chip) view.findViewById(R.id.chipSortPriority)).setChecked(true);
        } else {
            ((Chip) view.findViewById(R.id.chipSortCreated)).setChecked(true);
        }

        // Restore filter selection
        switch (currentFilter) {
            case DB.FILTER_INCOMPLETE:
                ((Chip) view.findViewById(R.id.chipFilterIncomplete)).setChecked(true); break;
            case DB.FILTER_COMPLETE:
                ((Chip) view.findViewById(R.id.chipFilterComplete)).setChecked(true); break;
            case DB.FILTER_OVERDUE:
                ((Chip) view.findViewById(R.id.chipFilterOverdue)).setChecked(true); break;
            default:
                ((Chip) view.findViewById(R.id.chipFilterAll)).setChecked(true);
        }

        btnApply.setOnClickListener(v -> {
            // Get selected sort
            int sortId = cgSort.getCheckedChipId();
            String sort = DB.SORT_BY_CREATED;
            if (sortId == R.id.chipSortDeadline) sort = DB.SORT_BY_DEADLINE;
            else if (sortId == R.id.chipSortPriority) sort = DB.SORT_BY_PRIORITY;

            // Get selected filter
            int filterId = cgFilter.getCheckedChipId();
            int filter = DB.FILTER_ALL;
            if (filterId == R.id.chipFilterIncomplete) filter = DB.FILTER_INCOMPLETE;
            else if (filterId == R.id.chipFilterComplete) filter = DB.FILTER_COMPLETE;
            else if (filterId == R.id.chipFilterOverdue) filter = DB.FILTER_OVERDUE;

            if (listener != null) listener.onApply(sort, filter);
            dismiss();
        });
    }
}

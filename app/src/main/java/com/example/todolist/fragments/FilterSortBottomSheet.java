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
        void onApply(String sortColumn, boolean isAscending);
    }

    private String currentSort;
    private boolean isAscending;
    private OnApplyListener listener;

    public FilterSortBottomSheet(String sortColumn, boolean isAscending, OnApplyListener listener) {
        this.currentSort = sortColumn;
        this.isAscending = isAscending;
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
        ChipGroup cgDirection = view.findViewById(R.id.cgDirection);
        Button btnApply = view.findViewById(R.id.btnApply);

        // Restore sort selection
        if (currentSort.contains(DB.COLUMN_DEADLINE)) {
            ((Chip) view.findViewById(R.id.chipSortDeadline)).setChecked(true);
        } else if (currentSort.contains(DB.COLUMN_PRIORITY)) {
            ((Chip) view.findViewById(R.id.chipSortPriority)).setChecked(true);
        } else {
            ((Chip) view.findViewById(R.id.chipSortCreated)).setChecked(true);
        }

        // Restore direction selection
        if (isAscending) {
            ((Chip) view.findViewById(R.id.chipAsc)).setChecked(true);
        } else {
            ((Chip) view.findViewById(R.id.chipDesc)).setChecked(true);
        }

        btnApply.setOnClickListener(v -> {
            // Get selected sort column
            int sortId = cgSort.getCheckedChipId();
            String sortColumn = DB.COLUMN_CREATED_TIME;
            if (sortId == R.id.chipSortDeadline) sortColumn = DB.COLUMN_DEADLINE;
            else if (sortId == R.id.chipSortPriority) sortColumn = DB.COLUMN_PRIORITY;

            // Get selected direction
            boolean asc = cgDirection.getCheckedChipId() == R.id.chipAsc;

            if (listener != null) listener.onApply(sortColumn, asc);
            dismiss();
        });
    }
}

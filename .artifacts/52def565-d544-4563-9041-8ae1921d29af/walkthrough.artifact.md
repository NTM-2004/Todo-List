# Walkthrough - Simplified Filter/Sort Bottom Sheet

I have simplified the Sort Bottom Sheet by removing the redundant status filters (which are already handled by the TabLayout) and adding an option to toggle between Tăng dần (Ascending) and Giảm dần (Descending) order.

## Changes Made

### UI Simplification
- **Removed Redundant Filters**: The "Lọc trạng thái" section was removed from [bottom_sheet_filter_sort.xml](file:///D:/College/Android/TodoList/app/src/main/res/layout/bottom_sheet_filter_sort.xml).
- **Added Sort Direction**: A new section "Thứ tự" was added with two chips: **Tăng dần (Ascending)** and **Giảm dần (Descending)**.

### Logic Improvements
- **Updated Bottom Sheet**: [FilterSortBottomSheet.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/FilterSortBottomSheet.java) now handles sort direction instead of status filters.
- **Dynamic Sorting**: [HomeFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/HomeFragment.java) now constructs the sort string dynamically (e.g., `deadline ASC` or `priority DESC`) and passes it to the ViewModel.
- **Improved Shared State**: Added `currentSortColumn` and `isAscending` tracking in `HomeFragment` to persist user selections correctly between Bottom Sheet openings.

## Verification Results

### Automated Tests
- **Gradle Build**: Ran `:app:assembleDebug` and the build finished successfully.

### Manual Verification
1. Open the app and click the **Sort** button.
2. Observe that the status filter chips are gone.
3. Select a criteria (e.g., **Deadline**) and a direction (e.g., **Tăng dần**).
4. Click **Áp dụng**.
5. Verify that the task list is sorted correctly by deadline in ascending order across all tabs.

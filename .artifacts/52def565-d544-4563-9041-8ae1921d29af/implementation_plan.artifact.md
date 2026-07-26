# Implementation Plan - Simplify Filter/Sort Bottom Sheet

The user wants to remove the redundant "Status Filter" from the Bottom Sheet (since it's already in the TabLayout) and instead add an option to toggle between Ascending and Descending order.

## User Review Required

> [!NOTE]
> I will replace the "Status Filter" section with a "Sort Direction" section (Ascending vs Descending).
> The sort logic in `DB.java` currently uses hardcoded directions (e.g., `deadline ASC`). I will update the logic to construct the sort string dynamically.

## Proposed Changes

### UI Components

#### [MODIFY] [bottom_sheet_filter_sort.xml](file:///D:/College/Android/TodoList/app/src/main/res/layout/bottom_sheet_filter_sort.xml)
- Remove the "Lọc trạng thái" TextView and ChipGroup.
- Add a new "Thứ tự" (Direction) section with two Chips: "Tăng dần" (Ascending) and "Giảm dần" (Descending).

### Logic & Data

#### [MODIFY] [FilterSortBottomSheet.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/FilterSortBottomSheet.java)
- Remove filter-related logic.
- Add logic to handle Sort Direction selection.
- Update `OnApplyListener` to pass `String sortColumn` and `boolean isAscending` (or a combined sort string).

#### [MODIFY] [HomeFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/HomeFragment.java)
- Update the `FilterSortBottomSheet` listener to receive the new sort parameters.
- Combine the selected column and direction into a string compatible with `DB.java` (e.g., `deadline ASC`).

#### [MODIFY] [DB.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/db/DB.java)
- Refactor sort constants to be column names only (e.g., `COLUMN_DEADLINE`) or add helper constants for dynamic sorting.

## Verification Plan

### Manual Verification
- Open the Sort Bottom Sheet.
- Select a sort criteria (e.g., Deadline) and a direction (e.g., Descending).
- Click "Apply".
- Verify that the list updates correctly in all tabs.

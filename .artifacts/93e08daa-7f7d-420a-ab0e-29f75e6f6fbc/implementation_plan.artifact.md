# Thêm và Cập nhật Task với Fragment mới

Người dùng muốn có một màn hình mới để thêm công việc (qua nút FAB) và cập nhật công việc (khi nhấn vào một item trong danh sách).

## Các bước thực hiện

### 1. [MODIFY] [DB.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/db/DB.java)
- Thêm phương thức `getTask(int id)` để lấy thông tin chi tiết của một task từ database.

### 2. [NEW] [fragment_add_task.xml](file:///D:/College/Android/TodoList/app/src/main/res/layout/fragment_add_task.xml)
- Thiết kế giao diện nhập liệu:
    - Title (`EditText`)
    - Content (`EditText`)
    - Category (`Spinner` hoặc `EditText`)
    - Deadline (`Button` hoặc `TextView` để mở DatePicker)
    - Nút Save.

### 3. [NEW] [AddTaskFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/AddTaskFragment.java)
- Xử lý logic:
    - Nếu nhận được `taskId` từ Arguments: Tải dữ liệu cũ lên để cập nhật.
    - Nếu không: Để trống các trường để thêm mới.
    - Xử lý chọn ngày (`DatePickerDialog`).
    - Lưu dữ liệu vào SQLite.

### 4. [MODIFY] [mobile_navigation.xml](file:///D:/College/Android/TodoList/app/src/main/res/navigation/mobile_navigation.xml)
- Thêm destination `fragment_add_task`.

### 5. [MODIFY] [MainActivity.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/MainActivity.java)
- Cập nhật sự kiện click của nút FAB để điều hướng tới `AddTaskFragment`.

### 6. [MODIFY] [TaskAdapter.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/adapter/TaskAdapter.java)
- Thêm interface `OnTaskClickListener` để bắt sự kiện nhấn vào toàn bộ item.

### 7. [MODIFY] Fragments Danh sách
- Cập nhật `TaskFragment` và `UnfinishFragment` để lắng nghe sự kiện nhấn vào item và điều hướng tới màn hình cập nhật.

## Kế hoạch kiểm tra

### Kiểm tra thủ công
- Nhấn FAB -> Màn hình thêm mới hiện ra -> Nhập dữ liệu -> Lưu -> Kiểm tra danh sách có task mới không.
- Nhấn vào một task có sẵn -> Màn hình cập nhật hiện ra với dữ liệu cũ -> Thay đổi thông tin -> Lưu -> Kiểm tra thông tin đã được cập nhật chưa.

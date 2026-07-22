# Walkthrough - Thêm và Cập nhật Task

Tôi đã triển khai thành công tính năng thêm mới công việc và cập nhật công việc hiện có thông qua một Fragment mới.

## Các tính năng chính

### 1. Màn hình Add/Edit Task
- Tạo [AddTaskFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/AddTaskFragment.java) và layout [fragment_add_task.xml](file:///D:/College/Android/TodoList/app/src/main/res/layout/fragment_add_task.xml).
- **Chế độ Thêm mới:** Khi nhấn nút FAB ở màn hình chính, các trường dữ liệu sẽ trống để bạn nhập mới.
- **Chế độ Cập nhật:** Khi nhấn vào một task trong danh sách, các trường dữ liệu sẽ tự động điền thông tin cũ của task đó.
- **Xử lý Deadline:** Tích hợp `DatePickerDialog` và `TimePickerDialog` để chọn ngày giờ hạn chót một cách trực quan.

### 2. Điều hướng mượt mà
- Sử dụng Jetpack Navigation để chuyển đổi giữa các màn hình.
- Nút FAB trong `MainActivity` đã được gán sự kiện mở màn hình thêm mới.
- `TaskAdapter` đã được cập nhật để bắt sự kiện click vào từng item trong danh sách.

### 3. Quản lý dữ liệu
- Cập nhật [DB.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/db/DB.java) với phương thức `getTask(int id)` để truy vấn chi tiết công việc.
- Hỗ trợ nút **Delete Task** (chỉ xuất hiện trong chế độ cập nhật) để xóa nhanh công việc không cần thiết.

## Kiểm tra
- Đã kiểm tra tính năng thêm mới: Task mới xuất hiện ngay lập tức trong danh sách.
- Đã kiểm tra tính năng cập nhật: Thay đổi tiêu đề/nội dung thành công.
- Đã kiểm tra tính năng xóa: Task bị xóa khỏi SQLite và danh sách.

![Màn hình thêm/sửa task](file:///D:/College/Android/TodoList/.artifacts/93e08daa-7f7d-420a-ab0e-29f75e6f6fbc/screenshot_edit.png)

> [!TIP]
> Bạn có thể thử thêm các task với deadline khác nhau để xem chúng được sắp xếp như thế nào trong tab "Unfinished Tasks".

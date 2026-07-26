# Tóm tắt Kiến trúc và Luồng hoạt động của Dự án Todo List

Tài liệu này cung cấp cái nhìn tổng quan về cách các file Giao diện (XML) và file Logic (Java) phối hợp với nhau.

---

## 1. Ánh xạ Giao diện (Layout) và Logic (Java)

| File Layout (XML) | File Java ánh xạ | Chức năng chính |
| :--- | :--- | :--- |
| `activity_main.xml` | `MainActivity.java` | Khung xương chính của app, chứa Toolbar và Bottom Navigation. |
| `fragment_home.xml` | `HomeFragment.java` | Màn hình chính chứa thanh tìm kiếm, các Tab và ViewPager. |
| `fragment_task.xml` | `TaskFragment.java` | Màn hình hiển thị danh sách tất cả công việc. |
| `fragment_unfinish.xml` | `UnfinishFragment.java` | Màn hình hiển thị công việc chưa hoàn thành. |
| `fragment_add_task.xml` | `AddTaskFragment.java` | Màn hình nhập liệu để thêm mới hoặc chỉnh sửa task. |
| `item_task.xml` | `TaskAdapter.java` | Định nghĩa giao diện của một dòng (item) trong danh sách. |
| `widget_todo.xml` | `TodoWidgetProvider.java` | Giao diện của tiện ích ngoài màn hình chủ điện thoại (Widget). |
| `activity_login.xml` | `LoginActivity.java` | Màn hình đăng nhập. |
| `activity_register.xml` | `RegisterActivity.java` | Màn hình đăng ký tài khoản. |

---

## 2. Chi tiết các thành phần trong Layout (Components)

- **`fragment_home.xml`**:
    - `etSearch`: Ô nhập từ khóa tìm kiếm.
    - `btnSort`: Nút mở menu sắp xếp.
    - `tabLayout`: Các thanh tiêu đề tab (Tasks, Unfinished).
    - `viewPager`: Vùng chứa để vuốt qua lại giữa các Fragment danh sách.

- **`fragment_add_task.xml`**:
    - `etTitle`, `etContent`, `etCategory`: Các ô nhập văn bản.
    - `btnDeadline`: Nút mở hộp thoại chọn ngày giờ.
    - `btnSave`: Nút lưu dữ liệu.
    - `btnDelete`: Nút xóa (chỉ hiện khi đang sửa).

- **`item_task.xml`**:
    - `tvTitle`, `tvDeadline`, `tvCategory`: Hiển thị thông tin task.
    - `cbStatus`: Ô tích chọn hoàn thành (Checkbox).

---

## 3. Chức năng các Class Java theo tầng (Layer)

### Tầng Dữ liệu (Database & Model)
- **`Task.java`**: Class khuôn mẫu, định nghĩa các thuộc tính của một công việc.
- **`DB.java`**: "Quản kho" SQLite, thực hiện lệnh SQL (INSERT, UPDATE, DELETE, SELECT).

### Tầng Điều phối (Adapter & ViewModel)
- **`TaskAdapter.java`**: Lấy danh sách Task từ Java, "vẽ" chúng vào `item_task.xml` để RecyclerView hiển thị.
- **`TaskViewModel.java`**: Nơi giữ dữ liệu tạm thời để giao diện không bị load lại khi xoay màn hình.

### Tầng Tiện ích (Notification & Auth)
- **`SessionManager.java`**: Lưu trữ trạng thái đăng nhập (đã đăng nhập chưa? user id là gì?).
- **`NotificationHelper.java`**: Tính toán thời gian (Deadline - 1h) và đăng ký lịch hẹn với hệ điều hành Android.
- **`NotificationReceiver.java`**: Khi đến giờ hẹn, class này sẽ "thức dậy" và thực hiện lệnh đẩy thông báo lên màn hình.

---

## 4. Ví dụ Luồng hoạt động: Tính năng Thông báo

1.  **Bước 1 (Kích hoạt)**: Người dùng nhập task trong `AddTaskFragment` và nhấn **Save**.
2.  **Bước 2 (Xử lý)**: `AddTaskFragment` gửi thông tin task sang `NotificationHelper.scheduleNotification()`.
3.  **Bước 3 (Đăng ký)**: `NotificationHelper` dùng `AlarmManager` của Android để đặt một cái "chuông báo thức" vào lúc (Deadline - 1 tiếng).
4.  **Bước 4 (Chờ đợi)**: Ứng dụng có thể bị đóng hoàn toàn, nhưng "chuông báo thức" vẫn nằm trong hệ thống Android.
5.  **Bước 5 (Thực thi)**: Khi đến giờ, Android phát tín hiệu. `NotificationReceiver.onReceive()` bắt được tín hiệu đó.
6.  **Bước 6 (Hiển thị)**: `NotificationReceiver` xây dựng thông báo bằng `NotificationCompat` và hiện lên thanh trạng thái.

> [!NOTE]
> Cách thiết kế này gọi là **Kiến trúc phân tầng**, giúp code của bạn sạch sẽ, dễ tìm lỗi và dễ nâng cấp hơn.

# Giải thích chi tiết cấu trúc đồ án Todo List

Tài liệu này giải thích chi tiết chức năng của từng file trong dự án và cách chúng phối hợp với nhau để thực hiện các tính năng.

---

## 1. Tầng Dữ liệu (Data Layer)

### [Task.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/model/Task.java)
- **Chức năng**: Là lớp Model (Object). Nó định nghĩa một "Công việc" gồm những thông tin gì (id, tiêu đề, nội dung, ngày hạn, trạng thái).
- **Nội dung**: Chứa các biến private và các hàm Getter/Setter để các file khác có thể đọc/ghi dữ liệu của một Task.

### [DB.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/db/DB.java)
- **Chức năng**: Quản lý cơ sở dữ liệu SQLite.
- **Nội dung**:
    - Tạo bảng `tasks` khi app chạy lần đầu.
    - `addTask()`: Thêm task mới vào bộ nhớ điện thoại.
    - `getAllTasks()` / `getUnfinishedTasks()`: Đọc danh sách task từ máy ra để hiển thị.
    - `updateTask()` / `deleteTask()`: Sửa hoặc xóa task.

---

## 2. Tầng Hiển thị (UI Layer)

### [MainActivity.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/MainActivity.java)
- **Chức năng**: Là màn hình chính, đóng vai trò "chủ nhà" chứa các thành phần dùng chung.
- **Nội dung**:
    - Quản lý Thanh công cụ (Toolbar) và Menu bên trái (Drawer).
    - Chứa nút **Floating Action Button (FAB)** để mở màn hình thêm task.
    - Sử dụng `NavController` để điều khiển việc chuyển đổi giữa các Fragment.

### [TaskAdapter.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/adapter/TaskAdapter.java)
- **Chức năng**: Là cầu nối giữa Dữ liệu và Danh sách (RecyclerView).
- **Nội dung quan trọng**:
    - `onBindViewHolder()`: Đổ dữ liệu từ một đối tượng Task vào các TextView, Checkbox trong file `item_task.xml`.
    - `filter(String text)`: Hàm dùng vòng lặp `for` để tìm kiếm task theo tên mà bạn đã yêu cầu. Nó lọc ngay trong RAM nên cực nhanh.

### [TaskFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/TaskFragment.java) & [UnfinishFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/UnfinishFragment.java)
- **Chức năng**: Hiển thị danh sách công việc (toàn bộ hoặc chỉ các task chưa xong).
- **Luồng hoạt động**: Khi mở màn hình -> Gọi `DB.java` lấy dữ liệu -> Đưa cho `TaskAdapter` -> Hiển thị lên màn hình.

### [AddTaskFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/AddTaskFragment.java)
- **Chức năng**: Màn hình để nhập liệu (thêm mới hoặc chỉnh sửa).
- **Luồng hoạt động**:
    - Nếu có `taskId` truyền sang: Đọc dữ liệu từ DB hiện lên để sửa.
    - Khi nhấn **Save**: Lưu vào DB và gọi `NotificationHelper` để hẹn giờ thông báo.

---

## 3. Tầng Thông báo (Notification Layer)

### [NotificationHelper.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/notification/NotificationHelper.java)
- **Chức năng**: Công cụ hỗ trợ gửi yêu cầu cho hệ thống Android.
- **Nội dung**:
    - `createNotificationChannel()`: Đăng ký một "Kênh" thông báo với Android (bắt buộc từ Android 8).
    - `scheduleNotification()`: Tính toán thời gian (Deadline - 1 tiếng) và gửi một "phiếu hẹn" cho `AlarmManager` của Android.

### [NotificationReceiver.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/notification/NotificationReceiver.java)
- **Chức năng**: Là bộ phận "trực chiến" của app.
- **Luồng hoạt động**: File này không chạy thường xuyên. Khi đến giờ hẹn của `AlarmManager`, Android sẽ đánh thức file này dậy -> Nó lấy tiêu đề task và đẩy lên thanh thông báo của điện thoại.

---

## 4. Các file tài nguyên (Resources)

- **[AndroidManifest.xml](file:///D:/College/Android/TodoList/app/src/main/AndroidManifest.xml)**: Khai báo các quyền (xin phép Android gửi thông báo) và đăng ký các thành phần như `NotificationReceiver`.
- **[mobile_navigation.xml](file:///D:/College/Android/TodoList/app/src/main/res/navigation/mobile_navigation.xml)**: Bản đồ điều hướng, định nghĩa việc nhấn nút này thì chuyển sang màn hình nào.
- **Layouts (`.xml`)**: Định nghĩa giao diện (vị trí nút bấm, màu sắc, font chữ).

---

## Tóm tắt luồng tính năng Tìm kiếm:
1. Người dùng nhập chữ vào `SearchView` ở Toolbar.
2. `TaskFragment` nhận được chữ đó.
3. `TaskFragment` ra lệnh cho `TaskAdapter.filter(chuỗi_nhập)`.
4. `TaskAdapter` chạy vòng lặp lọc danh sách và gọi `notifyDataSetChanged()` để vẽ lại màn hình.

> [!TIP]
> Toàn bộ kiến trúc này tuân thủ nguyên tắc chia để trị: Mỗi file làm đúng 1 việc. Điều này giúp bạn dễ dàng bảo trì và mở rộng sau này.

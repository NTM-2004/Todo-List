# Kế hoạch xóa tính năng Đăng nhập, Lưu người dùng và Thống kê

Kế hoạch này sẽ loại bỏ hoàn toàn hệ thống xác thực người dùng (đăng nhập, đăng ký, phiên làm việc) và màn hình thống kê. Ứng dụng sẽ trở thành một ứng dụng Todo cá nhân đơn giản, không yêu cầu tài khoản và dữ liệu được lưu trữ cục bộ cho một người dùng duy nhất.

## User Review Required

> [!IMPORTANT]
> - Sau khi thực hiện, tất cả các task hiện có trong cơ sở dữ liệu sẽ không còn được phân biệt theo `user_id`. Mặc dù dữ liệu cũ có thể vẫn tồn tại, nhưng ứng dụng sẽ truy vấn tất cả các task mà không lọc theo người dùng.
> - Tính năng "Thống kê" sẽ bị xóa bỏ hoàn toàn khỏi thanh điều hướng phía dưới (Bottom Navigation).

## Các thay đổi đề xuất

### 1. Xóa bỏ Authentication & User Persistence

Loại bỏ các lớp và giao diện liên quan đến người dùng.

#### [DELETE] [LoginActivity.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/auth/LoginActivity.java)
#### [DELETE] [RegisterActivity.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/auth/RegisterActivity.java)
#### [DELETE] [SessionManager.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/auth/SessionManager.java)
#### [DELETE] [User.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/model/User.java)
#### [DELETE] [activity_login.xml](file:///D:/College/Android/TodoList/app/src/main/res/layout/activity_login.xml)
#### [DELETE] [activity_register.xml](file:///D:/College/Android/TodoList/app/src/main/res/layout/activity_register.xml)

#### [MODIFY] [MainActivity.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/MainActivity.java)
- Xóa kiểm tra `sessionManager.isLoggedIn()`.
- Xóa menu Logout.
- Cập nhật tiêu đề Toolbar (không còn "Xin chào [username]").

#### [MODIFY] [AndroidManifest.xml](file:///D:/College/Android/TodoList/app/src/main/AndroidManifest.xml)
- Xóa khai báo `LoginActivity` và `RegisterActivity`.

### 2. Xóa bỏ Thống kê (Statistics)

#### [DELETE] [StatisticsFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/StatisticsFragment.java)
#### [DELETE] [fragment_statistics.xml](file:///D:/College/Android/TodoList/app/src/main/res/layout/fragment_statistics.xml)
#### [DELETE] [ic_statistics.xml](file:///D:/College/Android/TodoList/app/src/main/res/graphics/ic_statistics.xml) (và các tài nguyên liên quan)

#### [MODIFY] [mobile_navigation.xml](file:///D:/College/Android/TodoList/app/src/main/res/navigation/mobile_navigation.xml)
- Xóa destination `fragment_statistics`.

#### [MODIFY] [bottom_nav_menu.xml](file:///D:/College/Android/TodoList/app/src/main/res/menu/bottom_nav_menu.xml)
- Xóa item `fragment_statistics`.

#### [MODIFY] [build.gradle.kts](file:///D:/College/Android/TodoList/app/build.gradle.kts)
- Xóa thư viện `MPAndroidChart`.

### 3. Cập nhật Model và Database

#### [MODIFY] [Task.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/model/Task.java)
- Xóa trường `userId` và các getter/setter liên quan.

#### [MODIFY] [DB.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/db/DB.java)
- Xóa `TABLE_USERS` và các phương thức xử lý User.
- Cập nhật các câu lệnh SQL để không sử dụng `COLUMN_USER_ID`.
- Xóa các phương thức `getStatistics` và `getCategoryStats`.

### 4. Cập nhật UI & Logic truyền dữ liệu

#### [MODIFY] [HomeFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/HomeFragment.java)
- Ngừng lấy `userId` từ `SessionManager`.
- Cập nhật `TaskPagerAdapter` để không nhận `userId`.

#### [MODIFY] [TaskListFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/TaskListFragment.java)
- Xóa hằng số `ARG_USER_ID`.
- Ngừng nhận và sử dụng `userId` trong `loadTasks()`.

#### [MODIFY] [AddTaskFragment.java](file:///D:/College/Android/TodoList/app/src/main/java/com/example/todolist/fragments/AddTaskFragment.java)
- Ngừng truyền `userId` khi tạo Task mới.

## Kế hoạch xác minh

### Automated Tests
- Chạy lệnh `./gradlew assembleDebug` để đảm bảo không có lỗi biên dịch.

### Manual Verification
- Kiểm tra ứng dụng khởi chạy trực tiếp vào màn hình Home mà không qua Login.
- Thêm, sửa, xóa Task để đảm bảo lưu trữ vẫn hoạt động tốt.
- Kiểm tra thanh điều hướng dưới chỉ còn nút Home.
- Kiểm tra Menu không còn nút Logout.

# Phân tích chi tiết TaskListFragment.java

Tài liệu này giải thích các Class và Phương thức trong file `TaskListFragment.java`. Đây là thành phần chịu trách nhiệm hiển thị danh sách các công việc cụ thể cho từng tab (ví dụ: Tab "Chưa xong").

---

## 1. Các Class và Interface được sử dụng

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **TaskListFragment** | **Tự build** | Lớp điều khiển danh sách công việc, kế thừa từ `Fragment` và triển khai `OnTaskActionListener`. |
| **Fragment** | Có sẵn (AndroidX) | Thành phần giao diện đại diện cho một màn hình con bên trong `ViewPager2`. |
| **RecyclerView** | Có sẵn (AndroidX) | Thành phần giao diện tối ưu để hiển thị danh sách dài có thể cuộn. |
| **TaskAdapter** | **Tự build** | Lớp trung gian kết nối dữ liệu `Task` với giao diện `RecyclerView`. |
| **OnTaskActionListener** | **Tự build** | Interface (từ TaskAdapter) để lắng nghe các hành động của người dùng trên từng dòng. |
| **DB** | **Tự build** | Lớp quản lý cơ sở dữ liệu để truy vấn và cập nhật trạng thái công việc. |
| **TaskViewModel** | **Tự build** | Lớp chứa dữ liệu chung (tìm kiếm, sắp xếp) được chia sẻ từ `HomeFragment`. |
| **ItemTouchHelper** | Có sẵn (AndroidX) | Công cụ giúp thực hiện các thao tác chạm/vuốt trên danh sách (ở đây là vuốt để xóa). |
| **LinearLayoutManager** | Có sẵn (AndroidX) | Quản lý cách sắp xếp các phần tử trong danh sách theo hàng dọc. |
| **Snackbar** | Có sẵn (Material) | Thanh thông báo nhỏ hiện ở dưới màn hình, có nút "Hoàn tác" (Undo) khi xóa task. |
| **Navigation** | Có sẵn (Navigation) | Dùng để chuyển sang màn hình Chỉnh sửa khi người dùng nhấn vào một task. |

---

## 2. Các phương thức quan trọng

### Class: `TaskListFragment` (Tự build)
*   **`newInstance(int filter)`** (Static):
    *   *Ý nghĩa:* Cách an toàn để tạo một bản sao mới của Fragment với tham số (filter).
*   **`onCreate(...)`**: Lấy tham số `filter` từ Bundle truyền vào để biết Fragment này cần hiển thị loại task nào.
*   **`onViewCreated(...)`**:
    *   *Ý nghĩa:* Thiết lập danh sách sau khi giao diện đã sẵn sàng.
    *   *Sử dụng:* Khởi tạo Adapter, gắn vào RecyclerView, và đặc biệt là **đăng ký lắng nghe (Observe)** sự thay đổi từ `TaskViewModel`.
*   **`loadTasks()`**:
    *   *Ý nghĩa:* Lấy dữ liệu mới nhất từ Database và cập nhật vào Adapter.
    *   *Sử dụng:* Gọi hàm `db.getTasksSortedFiltered(...)` với các tiêu chí tìm kiếm và sắp xếp hiện tại.
*   **`setupSwipeToDelete()`**:
    *   *Ý nghĩa:* Thiết lập tính năng vuốt sang trái/phải để xóa công việc.
    *   *Sử dụng:* Khi vuốt, nó sẽ xóa task trong DB, cập nhật giao diện và hiện `Snackbar` cho phép người dùng bấm "Hoàn tác" nếu lỡ tay xóa nhầm.
*   **`onStatusChanged(Task task, boolean isDone)`**:
    *   *Ý nghĩa:* Được gọi khi người dùng tích/bỏ tích vào CheckBox.
    *   *Sử dụng:* Cập nhật trạng thái mới vào Database và tải lại danh sách.
*   **`onTaskClick(Task task)`**:
    *   *Ý nghĩa:* Được gọi khi người dùng nhấn vào một dòng công việc.
    *   *Sử dụng:* Chuyển sang màn hình `AddTaskFragment` và truyền kèm `taskId` để chỉnh sửa.

### Các phương thức từ thư viện
*   **`viewModel.getSearchQuery().observe(...)`**: Theo dõi biến tìm kiếm. Khi bạn gõ ở Home, hàm này sẽ tự động chạy và gọi `loadTasks()`.
*   **`ItemTouchHelper.SimpleCallback`**: Một lớp trừu tượng để định nghĩa hành động khi người dùng vuốt trên màn hình.
*   **`Snackbar.make(...).setAction(...)`**: Tạo thông báo có kèm theo nút bấm hành động (Undo).

---

## 3. Cơ chế hoạt động đặc biệt: "Lắng nghe dữ liệu"

Điểm hay nhất của file này là việc sử dụng **LiveData** thông qua ViewModel:
1.  **HomeFragment** thay đổi từ khóa tìm kiếm trong `ViewModel`.
2.  **TaskListFragment** đang "ngồi chờ" (Observe) sự thay đổi đó.
3.  Ngay khi từ khóa thay đổi, `TaskListFragment` tự động biết và gọi `loadTasks()` để cập nhật danh sách mà không cần ai gọi trực tiếp đến nó.

> [!TIP]
> **Tính năng xóa cực hay:** Việc kết hợp `db.deleteTask` và `Snackbar` tạo ra một trải nghiệm chuyên nghiệp: Người dùng xóa nhanh bằng cách vuốt, nhưng vẫn có 3-4 giây để "cứu" lại dữ liệu nếu xóa nhầm nhờ nút **Hoàn tác**.

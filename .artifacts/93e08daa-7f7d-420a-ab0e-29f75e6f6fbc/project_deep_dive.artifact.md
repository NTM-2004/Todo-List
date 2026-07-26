# Phân tích Chi tiết Cấu trúc & Luồng hoạt động Dự án

Tài liệu này giải thích mối quan hệ giữa Giao diện (Layout) và Logic (Java), cùng thứ tự thực thi của các tính năng.

---

## 1. Mối quan hệ Layout <-> Java

| Layout XML | Component chính (ID) | Class Java điều khiển | Chức năng của Class |
| :--- | :--- | :--- | :--- |
| `activity_main.xml` | `bottom_nav`, `fab` | `MainActivity.java` | "Thân máy" - Quản lý thanh điều hướng dưới và nút thêm nhanh. |
| `fragment_home.xml` | `etSearch`, `tabLayout` | `HomeFragment.java` | "Vỏ bọc" - Chứa thanh tìm kiếm và chia các Tab (Tất cả, Chưa xong...). |
| `fragment_task_list.xml`| `rvTasks` (RecyclerView) | `TaskListFragment.java` | "Trang danh sách" - Nơi thực sự hiển thị các công việc. |
| `item_task.xml` | `tvTitle`, `cbStatus` | `TaskAdapter.java` | "Công nhân" - Vẽ từng dòng công việc dựa trên mẫu này. |
| `fragment_add_task.xml` | `etTitle`, `btnSave` | `AddTaskFragment.java` | "Trang nhập liệu" - Xử lý việc tạo mới hoặc sửa Task. |
| `activity_login.xml` | `etUsername`, `btnLogin` | `LoginActivity.java` | "Trang bảo mật" - Kiểm tra tài khoản người dùng. |

---

## 2. Giải mã các thành phần "ẩn"

### Thanh tìm kiếm (Search)
- **Component**: `EditText` (ID: `etSearch`) trong `fragment_home.xml`.
- **Tại sao dùng EditText?**: Để tùy biến giao diện đẹp hơn (background bo góc, màu sắc chìm).
- **Luồng gọi**:
  1. `HomeFragment.java` (Lấy chữ)
  2. `TaskViewModel.java` (Giữ chữ)
  3. `TaskListFragment.java` (Nhận thông báo chữ thay đổi)
  4. `DB.java` (Truy vấn theo từ khóa).

### Hiệu ứng (Animation)
- **Loại**: Animation hệ thống (`android.R.anim`).
- **Vị trí**: Nằm trong `TaskAdapter.java` (hàm `onBindViewHolder`).
- **Cách dùng**: Khi một dòng Task chuẩn bị hiện ra, lệnh `startAnimation` sẽ được gọi để dòng đó trượt từ trái sang.

---

## 3. Thứ tự thực hiện một tính năng (Ví dụ: Thêm Task mới)

Dưới đây là các bước "giao tiếp" giữa các file khi bạn thêm một công việc:

1. **Người dùng**: Nhấn nút `fab` trong `MainActivity.java`.
2. **Navigation**: File `mobile_navigation.xml` chỉ đường cho app mở màn hình `AddTaskFragment`.
3. **Người dùng**: Nhập tiêu đề vào `etTitle` và nhấn `btnSave` trong `AddTaskFragment.java`.
4. **Logic**: `AddTaskFragment` gom các chữ đã nhập vào một đối tượng `Task.java`.
5. **Database**: Đối tượng này được gửi sang `DB.java`. Tại đây, lệnh SQL `INSERT` được thực thi để lưu vào file `todolist.db`.
6. **Thông báo**: `AddTaskFragment` gọi `DeadlineNotificationManager.java` để hẹn giờ với hệ thống Android.
7. **Kết thúc**: `AddTaskFragment` gọi `navigateUp()` để đóng màn hình và quay lại danh sách.

---

## 4. Tại sao cấu trúc lại chia nhỏ như vậy?

Bạn có thể thấy hơi rắc rối vì có nhiều file, nhưng đây là lý do:
- **Tách biệt giao diện và dữ liệu**: Nếu bạn muốn đổi giao diện (từ danh sách sang ô lưới), bạn chỉ cần sửa XML và Adapter, không cần động vào file Database.
- **Tái sử dụng**: File `TaskListFragment` được dùng lại 3 lần (cho tab Tất cả, Chưa xong, Hoàn thành), chỉ khác nhau cái "bộ lọc" truyền vào. Điều này giúp bạn viết code ít hơn.

> [!TIP]
> Để tìm xem một Component trong XML được dùng ở đâu trong Java, bạn hãy nhấn giữ phím **Ctrl** và Click chuột vào **ID** của nó (ví dụ `R.id.etSearch`) trong file Java. IDE sẽ dẫn bạn đến đúng chỗ.

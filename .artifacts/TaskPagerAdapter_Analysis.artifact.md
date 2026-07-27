# Phân tích chi tiết TaskPagerAdapter.java

Tài liệu này giải thích các Class và Phương thức trong file `TaskPagerAdapter.java`. Đây là lớp quản lý các tab (trang) trong ứng dụng bằng `ViewPager2`.

---

## 1. Các Class được sử dụng trong file

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **TaskPagerAdapter** | **Tự build** | Lớp chính quản lý việc hiển thị các Fragment trong ViewPager2, kế thừa từ `FragmentStateAdapter`. |
| **FragmentStateAdapter** | Có sẵn (ViewPager2) | Lớp cơ sở hiện đại nhất của Android để quản lý danh sách các Fragment có thể vuốt qua lại. |
| **Fragment** | Có sẵn (AndroidX) | Đại diện cho một phần giao diện người dùng (màn hình nhỏ) có thể tái sử dụng. |
| **DB** | **Tự build** | Lớp quản lý cơ sở dữ liệu (được truyền vào qua constructor nhưng chưa dùng trực tiếp ở đây). |
| **TaskListFragment** | **Tự build** | Lớp hiển thị danh sách các công việc. |

---

## 2. Các phương thức quan trọng

### Class: `TaskPagerAdapter` (Tự build)
*   **`TaskPagerAdapter(@NonNull Fragment fragment, DB db, int[] filters, String[] titles)`**:
    *   *Ý nghĩa:* Hàm khởi tạo.
    *   *Sử dụng:* Nhận vào đối tượng Fragment cha, database, mảng các bộ lọc (tất cả, chưa xong, đã xong) và tiêu đề của các tab.
*   **`createFragment(int position)`**:
    *   *Ý nghĩa:* Tạo ra Fragment tương ứng với vị trí của tab đang hiển thị.
    *   *Sử dụng:* Gọi `TaskListFragment.newInstance(filters[position])` để tạo ra một màn hình danh sách với bộ lọc tương ứng (ví dụ: Tab 1 hiển thị "Chưa xong", Tab 2 hiển thị "Đã xong").
*   **`getItemCount()`**:
    *   *Ý nghĩa:* Cho ViewPager2 biết có tổng cộng bao nhiêu tab để hiển thị.
    *   *Sử dụng:* Trả về độ dài của mảng `filters`.

### Các phương thức từ thư viện/class khác
*   **`super(fragment)`**: Gọi hàm khởi tạo của lớp cha (`FragmentStateAdapter`) để thiết lập các thông số cần thiết cho việc quản lý Fragment.
*   **`TaskListFragment.newInstance(int filter)`**:
    *   *Ý nghĩa:* Một phương thức tĩnh (static) để tạo một bản sao mới của `TaskListFragment` với một tham số truyền vào (filter). Đây là cách làm chuẩn để truyền dữ liệu vào Fragment trong Android.

---

## 3. Cơ chế hoạt động của PagerAdapter

Lớp này đóng vai trò như một **"Người quản lý thư viện"**:
1.  **`getItemCount`**: Trả lời câu hỏi "Thư viện này có bao nhiêu ngăn sách?".
2.  **`createFragment`**: Khi người dùng lật sang một ngăn sách (vuốt tab), người quản lý này sẽ "lấy đúng cuốn sách" (`TaskListFragment`) với "nhãn dán" phù hợp (`filter`) để đưa cho người dùng xem.

> [!NOTE]
> **Tại sao dùng ViewPager2?**
> ViewPager2 thay thế cho ViewPager cũ, nó mượt mà hơn, hỗ trợ cuộn theo chiều dọc và hoạt động cực kỳ ổn định với các Fragment hiện đại của Android.

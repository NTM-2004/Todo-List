# Phân tích chi tiết TaskViewModel.java

Tài liệu này giải thích các Class và Phương thức trong file `TaskViewModel.java`. Đây là lớp quản lý dữ liệu và trạng thái giao diện, giúp kết nối các màn hình khác nhau một cách mượt mà.

---

## 1. Các Class được sử dụng trong file

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **TaskViewModel** | **Tự build** | Lớp chính chứa dữ liệu cần chia sẻ giữa các Fragment (như tìm kiếm, sắp xếp). |
| **ViewModel** | Có sẵn (Android Lifecycle) | Lớp cơ sở giúp dữ liệu không bị mất khi màn hình xoay (xoay điện thoại) và quản lý vòng đời an toàn. |
| **MutableLiveData** | Có sẵn (Android Lifecycle) | Một loại "thùng chứa dữ liệu" có thể thay đổi được (Mutable). Nó cho phép chúng ta "phát" (emit) dữ liệu mới ra ngoài. |
| **LiveData** | Có sẵn (Android Lifecycle) | Một loại "thùng chứa dữ liệu" chỉ đọc. Nó giúp các Fragment khác có thể "lắng nghe" sự thay đổi dữ liệu mà không làm hỏng dữ liệu gốc. |
| **DB** | **Tự build** | Dùng để lấy các hằng số sắp xếp mặc định (ví dụ: `DB.SORT_BY_CREATED`). |

---

## 2. Các phương thức quan trọng

### Class: `TaskViewModel` (Tự build)
*   **`setSearchQuery(String query)`**:
    *   *Ý nghĩa:* Cập nhật từ khóa tìm kiếm mới.
    *   *Sử dụng:* Được gọi từ `HomeFragment` khi người dùng gõ vào ô tìm kiếm.
*   **`getSearchQuery()`**:
    *   *Ý nghĩa:* Trả về đối tượng `LiveData` để các màn hình khác có thể đăng ký lắng nghe.
    *   *Sử dụng:* `TaskListFragment` gọi hàm này để biết khi nào cần lọc lại danh sách.
*   **`setSortOrder(String order)`**:
    *   *Ý nghĩa:* Cập nhật tiêu chí sắp xếp mới (ví dụ: theo Hạn chót hoặc Ngày tạo).
    *   *Sử dụng:* Được gọi khi người dùng chọn sắp xếp trong `FilterSortBottomSheet`.
*   **`getSortOrder()`**:
    *   *Ý nghĩa:* Trả về đối tượng `LiveData` chứa tiêu chí sắp xếp hiện tại.

### Các phương thức từ thư viện
*   **`setValue(T value)`**:
    *   *Ý nghĩa:* Gán giá trị mới cho `MutableLiveData`. Ngay khi gọi hàm này, tất cả những "người nghe" (Observers) sẽ nhận được thông báo ngay lập tức.

---

## 3. Cơ chế "Phát thanh" dữ liệu

Hãy tưởng tượng `TaskViewModel` như một **"Đài phát thanh"**:
1.  **Phóng viên (`HomeFragment`):** Khi có tin tức mới (người dùng gõ tìm kiếm), phóng viên dùng `setSearchQuery` để gửi tin về đài.
2.  **Trạm phát (`MutableLiveData`):** Đài nhận tin và phát sóng đi khắp nơi.
3.  **Thính giả (`TaskListFragment`):** Các phòng ban khác nhau đang mở radio (`observe`). Ngay khi nghe thấy tin mới, họ tự động cập nhật lại sổ sách (tải lại danh sách công việc).

> [!IMPORTANT]
> **Tại sao không dùng biến thường?**
> Nếu dùng biến `String` bình thường, khi bạn gõ ở Home, các Tab bên dưới sẽ không hề biết để cập nhật theo. `LiveData` chính là "sợi dây liên lạc" tự động giúp app của bạn phản ứng tức thì với hành động của người dùng.

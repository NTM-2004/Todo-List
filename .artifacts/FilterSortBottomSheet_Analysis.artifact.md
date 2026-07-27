# Phân tích chi tiết FilterSortBottomSheet.java

Tài liệu này giải thích các Class và Phương thức trong file `FilterSortBottomSheet.java`. Đây là một "Bottom Sheet" (bảng từ dưới hiện lên) cho phép người dùng chọn cách sắp xếp danh sách công việc.

---

## 1. Các Class và UI Component được sử dụng

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **FilterSortBottomSheet** | **Tự build** | Lớp điều khiển bảng chọn sắp xếp, kế thừa từ `BottomSheetDialogFragment`. |
| **BottomSheetDialogFragment** | Có sẵn (Material) | Một loại Fragment đặc biệt hiển thị như một bảng trượt từ dưới màn hình lên. |
| **OnApplyListener** | **Tự build** | Interface để báo kết quả người dùng đã chọn về cho Fragment gọi nó. |
| **Chip / ChipGroup** | Có sẵn (Material) | Các nút lựa chọn dạng "viên thuốc", dùng để chọn tiêu chí (Ngày tạo, Hạn chót...) và hướng (Tăng/Giảm). |
| **Button** | Có sẵn (Android SDK) | Nút "Áp dụng" để xác nhận lựa chọn. |
| **DB** | **Tự build** | Lấy các tên cột (COLUMN_DEADLINE, COLUMN_PRIORITY...) để xác định tiêu chí sắp xếp. |

---

## 2. Các phương thức quan trọng

### Class: `FilterSortBottomSheet` (Tự build)
*   **`FilterSortBottomSheet(String sortColumn, boolean isAscending, OnApplyListener listener)`**:
    *   *Ý nghĩa:* Hàm khởi tạo.
    *   *Sử dụng:* Nhận vào trạng thái sắp xếp hiện tại (cột nào, tăng hay giảm) để hiển thị đúng lựa chọn cũ cho người dùng.
*   **`onCreateView(...)`**:
    *   *Ý nghĩa:* Nạp giao diện XML (`bottom_sheet_filter_sort.xml`) cho bảng trượt.
*   **`onViewCreated(...)`**:
    *   *Ý nghĩa:* Thiết lập logic sau khi giao diện đã hiện ra.
    *   *Sử dụng:* Tìm các Chip, khôi phục lại dấu tích (checked) dựa trên `currentSort` và `isAscending` đã truyền vào.
*   **`btnApply.setOnClickListener(...)`**:
    *   *Ý nghĩa:* Xử lý khi người dùng nhấn nút "Áp dụng".
    *   *Sử dụng:* Kiểm tra xem Chip nào đang được tích, xác định tên cột tương ứng trong DB và hướng sắp xếp, sau đó gọi `listener.onApply(...)` và đóng bảng.

### Các phương thức từ thư viện
*   **`getCheckedChipId()`**: Lấy ID của Chip đang được chọn trong một `ChipGroup`.
*   **`dismiss()`**: Đóng bảng Bottom Sheet và quay lại màn hình chính.

---

## 3. Ý nghĩa thực tế của file này

File này giải quyết bài toán **trải nghiệm người dùng (UX)**:
1.  **Không choáng màn hình:** Thay vì chuyển sang một màn hình mới chỉ để chọn sắp xếp, Bottom Sheet chỉ hiện lên một nửa, giúp người dùng vẫn thấy được ngữ cảnh của danh sách bên dưới.
2.  **Đồng bộ dữ liệu:** Nhờ có `OnApplyListener`, khi người dùng chọn xong, thông tin sẽ được truyền ngay lập tức về `TaskListFragment` để tải lại danh sách theo đúng yêu cầu.
3.  **Giao diện Material:** Sử dụng `Chip` giúp việc chọn lựa rất thuận tiện bằng ngón cái trên thiết bị di động.

> [!TIP]
> **Học cách dùng Interface:** Đây là ví dụ điển hình về việc dùng Interface để "nói chuyện" ngược lại với nơi đã gọi mình (Callback). Rất hữu ích khi viết các hộp thoại hoặc màn hình chọn lựa.

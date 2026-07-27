# Phân tích chi tiết AddTaskFragment.java

Tài liệu này giải thích các Class và Phương thức trong file `AddTaskFragment.java`. Đây là màn hình cho phép người dùng thêm mới hoặc chỉnh sửa một công việc (Task).

---

## 1. Các Class và UI Component được sử dụng

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **AddTaskFragment** | **Tự build** | Lớp chính điều khiển logic của màn hình thêm/sửa task. |
| **Fragment** | Có sẵn (AndroidX) | Thành phần giao diện độc lập, có vòng đời riêng. |
| **DB** | **Tự build** | Lớp quản lý cơ sở dữ liệu để lưu hoặc cập nhật task. |
| **Task** | **Tự build** | Lớp Model chứa thông tin công việc. |
| **DeadlineNotificationManager** | **Tự build** | Lớp quản lý đặt lịch thông báo khi đến hạn. |
| **TextInputEditText** | Có sẵn (Material) | Ô nhập văn bản nâng cao (cho Tiêu đề, Nội dung). |
| **AutoCompleteTextView** | Có sẵn (Android SDK) | Ô nhập văn bản có gợi ý (dùng làm dropdown chọn Category). |
| **Chip / ChipGroup** | Có sẵn (Material) | Các nút bấm nhỏ để chọn độ ưu tiên (Low, Medium, High). |
| **SwitchCompat** | Có sẵn (AndroidX) | Nút gạt bật/tắt tính năng thông báo. |
| **DatePickerDialog / TimePickerDialog** | Có sẵn (Android SDK) | Các hộp thoại chọn ngày và giờ. |
| **Calendar** | Có sẵn (Java) | Lớp xử lý logic về thời gian và ngày tháng. |
| **Navigation** | Có sẵn (Navigation) | Dùng để điều hướng quay lại màn hình trước sau khi xong việc. |

---

## 2. Các phương thức quan trọng

### Class: `AddTaskFragment` (Tự build)
*   **`onCreateView(...)`**:
    *   *Ý nghĩa:* Nạp giao diện XML (`fragment_add_task.xml`) vào Fragment.
*   **`onViewCreated(...)`**:
    *   *Ý nghĩa:* Được gọi sau khi giao diện đã sẵn sàng.
    *   *Sử dụng:* Khởi tạo các View, cài đặt Adapter cho Category, và kiểm tra xem là đang "Thêm mới" hay "Chỉnh sửa" (dựa vào `taskId`).
*   **`loadTaskData()`**:
    *   *Ý nghĩa:* Lấy dữ liệu từ database và hiển thị lên các ô nhập khi người dùng muốn sửa một task cũ.
*   **`showDateTimePicker()`**:
    *   *Ý nghĩa:* Hiển thị liên tiếp hộp thoại chọn Ngày rồi đến Giờ để đặt Deadline.
*   **`saveTask()`**:
    *   *Ý nghĩa:* Thu thập dữ liệu từ tất cả các ô nhập, kiểm tra tính hợp lệ (tiêu đề không được trống), sau đó gọi database để lưu (`addTask`) hoặc cập nhật (`updateTask`).
    *   *Sử dụng:* Đồng thời gọi `DeadlineNotificationManager` để đặt lịch thông báo nếu người dùng yêu cầu.
*   **`deleteTask()`**:
    *   *Ý nghĩa:* Xóa task hiện tại khỏi database và hủy thông báo đã đặt trước đó.
*   **`getSelectedPriority()`**:
    *   *Ý nghĩa:* Kiểm tra xem Chip nào đang được chọn để trả về giá trị độ ưu tiên tương ứng.

### Các phương thức từ thư viện có sẵn
*   **`Navigation.findNavController(view).navigateUp()`**: Lệnh để đóng màn hình hiện tại và quay lại màn hình danh sách.
*   **`calendar.getTimeInMillis()`**: Chuyển đổi ngày giờ đã chọn sang dạng số long (miligiây) để dễ dàng lưu vào database.
*   **`Toast.makeText(...).show()`**: Hiển thị thông báo nhỏ ở dưới màn hình (ví dụ: "Task đã thêm!").
*   **`etTitle.setError(...)`**: Hiển thị cảnh báo màu đỏ ngay tại ô nhập nếu dữ liệu không hợp lệ.

---

## 3. Logic xử lý thông minh trong file

1.  **Đa năng (Add vs Edit):** File này dùng chung cho cả hai tác vụ. Nếu `taskId` được truyền vào, nó biến thành màn hình "Chỉnh sửa", ngược lại là "Thêm mới".
2.  **Chọn thời gian 2 bước:** Kết hợp `DatePicker` và `TimePicker` giúp người dùng chọn deadline chính xác chỉ trong một luồng thao tác.
3.  **Tích hợp thông báo:** Ngay khi lưu task, code tự động tính toán và đặt lịch nhắc nhở với hệ thống Android thông qua `DeadlineNotificationManager`.

> [!TIP]
> **Điểm cần học:** Cách sử dụng `ArrayAdapter` với `AutoCompleteTextView` để tạo ra một menu chọn Category đơn giản nhưng chuyên nghiệp mà không cần dùng `Spinner` kiểu cũ.

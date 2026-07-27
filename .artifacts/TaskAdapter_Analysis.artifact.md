# Phân tích chi tiết TaskAdapter.java

Tài liệu này giải thích các Class và Phương thức trong file `TaskAdapter.java`. Đây là lớp trung gian giúp hiển thị danh sách công việc (`Task`) lên màn hình bằng `RecyclerView`.

---

## 1. Các Class và Interface được sử dụng

| Tên Class / Interface | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **TaskAdapter** | **Tự build** | Lớp chính quản lý việc đổ dữ liệu vào danh sách, kế thừa từ `RecyclerView.Adapter`. |
| **TaskViewHolder** | **Tự build** | Lớp con (inner class) dùng để giữ các tham chiếu đến các view (TextView, CheckBox...) trong mỗi dòng của danh sách. |
| **OnTaskActionListener** | **Tự build** | Một Interface dùng để truyền sự kiện (click, check) từ Adapter về Fragment/Activity. |
| **RecyclerView.Adapter** | Có sẵn (AndroidX) | Lớp cơ sở để tạo một Adapter cho danh sách cuộn mượt mà. |
| **RecyclerView.ViewHolder** | Có sẵn (AndroidX) | Lớp cơ sở giúp tối ưu hiệu năng bằng cách tránh gọi `findViewById` quá nhiều lần. |
| **LayoutInflater** | Có sẵn (Android SDK) | Dùng để chuyển đổi file giao diện XML (`item_task.xml`) thành đối tượng View trong code Java. |
| **AnimationUtils** | Có sẵn (Android SDK) | Dùng để tải các hiệu ứng chuyển động (như lướt từ trái sang) cho các phần tử trong danh sách. |
| **Paint** | Có sẵn (Android SDK) | Dùng để vẽ các hiệu ứng chữ, ở đây là gạch ngang chữ (`STRIKE_THRU_TEXT_FLAG`). |
| **Task** | **Tự build** | Lớp Model chứa dữ liệu của một công việc. |
| **SimpleDateFormat** | Có sẵn (Java) | Dùng để định dạng ngày tháng hiển thị (vd: "dd/MM/yyyy"). |

---

## 2. Các phương thức quan trọng

### Class: `TaskAdapter` (Tự build)
*   **`TaskAdapter(List<Task> taskList, OnTaskActionListener listener)`**:
    *   *Ý nghĩa:* Hàm khởi tạo. Nhận danh sách Task và đối tượng lắng nghe sự kiện.
*   **`onCreateViewHolder(ViewGroup parent, int viewType)`**:
    *   *Ý nghĩa:* Tạo ra một "ngôi nhà" (ViewHolder) mới cho một dòng trong danh sách.
    *   *Sử dụng:* Nạp layout `R.layout.item_task` và trả về một `TaskViewHolder`.
*   **`onBindViewHolder(TaskViewHolder holder, int position)`**:
    *   *Ý nghĩa:* "Đổ" dữ liệu từ một Task cụ thể vào các View trong ViewHolder.
    *   *Sử dụng:* Cài đặt tiêu đề, nội dung, màu sắc theo độ ưu tiên, định dạng ngày tháng và xử lý logic gạch ngang chữ khi đã hoàn thành.
*   **`getItemCount()`**:
    *   *Ý nghĩa:* Cho RecyclerView biết danh sách có bao nhiêu phần tử để vẽ.
*   **`updateData(List<Task> newList)`**:
    *   *Ý nghĩa:* Cập nhật danh sách mới và thông báo cho giao diện vẽ lại.

### Class: `TaskViewHolder` (Tự build - Inner Class)
*   **`TaskViewHolder(View itemView)`**:
    *   *Ý nghĩa:* Khởi tạo các View bằng `findViewById`. Phương thức này chỉ chạy một vài lần (khi cần tạo mới view), giúp app mượt hơn.

### Các phương thức từ thư viện có sẵn
*   **`holder.itemView.startAnimation(...)`**: Tạo hiệu ứng bay vào cho từng dòng khi cuộn danh sách.
*   **`tvTitle.setPaintFlags(...)`**: Thêm hiệu ứng gạch ngang (`STRIKE_THRU`) vào tiêu đề khi công việc đã xong.
*   **`holder.cbStatus.setOnCheckedChangeListener(...)`**: Lắng nghe khi người dùng nhấn vào ô CheckBox để đánh dấu hoàn thành.
*   **`holder.itemView.setOnClickListener(...)`**: Lắng nghe khi người dùng nhấn vào cả dòng công việc để xem chi tiết hoặc chỉnh sửa.

---

## 3. Cơ chế hoạt động của Adapter

Hãy tưởng tượng **RecyclerView** giống như một dây chuyền sản xuất:
1.  **`onCreateViewHolder`**: Tạo ra các "khung xe" (view trống).
2.  **`onBindViewHolder`**: Lấy phụ tùng (dữ liệu từ `Task`) lắp vào khung xe đó.
3.  **`TaskViewHolder`**: Là người giữ các bộ phận (vô lăng, bánh xe) để lắp cho nhanh, không phải đi tìm lại từ đầu.
4.  **`OnTaskActionListener`**: Là kênh liên lạc để báo cáo về "văn phòng chính" (Fragment/Activity) mỗi khi có biến cố xảy ra (nhấn nút, chọn xong).

> [!TIP]
> **Điểm hay trong code này:**
> - Có sử dụng **Animation** giúp danh sách hiện ra sinh động hơn.
> - Xử lý **Priority** (độ ưu tiên) bằng màu sắc trực quan (Đỏ/Vàng/Xanh).
> - Tự động định dạng lại ngày tháng từ số Long sang định dạng dễ đọc.

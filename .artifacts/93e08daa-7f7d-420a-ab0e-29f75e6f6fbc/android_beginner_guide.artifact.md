# Hướng dẫn Tự học & Hiểu Mã nguồn cho Người mới

Chào bạn! Đừng quá lo lắng nếu thấy code khó hiểu. Tài liệu này sẽ giúp bạn bóc tách từng phần của ứng dụng theo cách đơn giản nhất.

---

## Bước 1: Cách Giao diện (XML) nói chuyện với Logic (Java)

Hãy nhớ công thức thần thánh này: **XML vẽ ra -> Java điều khiển.**

1.  **Trong XML**: Bạn đặt ID cho thành phần.
    *   *Ví dụ*: `<Button android:id="@+id/btnSave" ... />`
2.  **Trong Java**: Bạn tạo một biến và "ánh xạ" nó tới XML.
    *   *Ví dụ*: `Button nutLuu = findViewById(R.id.btnSave);`
3.  **Lắng nghe**: Bạn ra lệnh cho Java phải làm gì khi người dùng chạm vào nút đó.
    *   *Ví dụ*: `nutLuu.setOnClickListener(v -> { /* Làm gì đó */ });`

---

## Bước 2: Hiểu về luồng Tìm kiếm (Search)

Bạn thắc mắc tại sao không thấy `SearchView`? Đó là vì tôi muốn dạy bạn cách làm "thủ công" để bạn hiểu bản chất.

**Thứ tự diễn ra**:
1.  **Nhập liệu**: Bạn gõ chữ vào ô `etSearch` (thực chất là một cái `EditText` bình thường).
2.  **Lắng nghe (HomeFragment.java)**: Có một hàm tên là `addTextChangedListener`. Nó giống như một người bảo vệ đứng canh cái ô nhập liệu, bạn cứ gõ một chữ là nó báo ngay: *"Có chữ mới rồi nè!"*.
3.  **Truyền tin (ViewModel)**: `HomeFragment` gửi cái chữ đó vào một cái "máy nhắn tin" tên là `TaskViewModel`.
4.  **Nhận tin (TaskListFragment.java)**: Các tab danh sách đang "đăng ký" nhận tin từ cái máy nhắn tin này. Khi thấy có chữ mới, nó sẽ gọi Database: *"Này DB, tìm cho tôi các task có tên giống như thế này"*.
5.  **Vẽ lại**: Danh sách được vẽ lại ngay lập tức.

> [!TIP]
> Bạn thấy `etSearch` giống `TextView` là vì tôi dùng thuộc tính `background` để trang trí cho nó đẹp hơn, làm mất đi cái gạch chân xấu xí mặc định của Android.

---

## Bước 3: Hiểu về Thông báo (Notification)

Đây là phần khó nhất vì nó liên quan đến hệ điều hành. Hãy tưởng tượng như sau:

1.  **Lên lịch (`NotificationHelper.java`)**: Khi bạn lưu task, app gửi cho Android một cái "phiếu hẹn giờ" (Alarm). Bạn nói với Android: *"Lúc 8 giờ tối nay hãy gọi tên Receiver của tôi dậy nhé"*.
2.  **Đi ngủ**: App của bạn có thể tắt hẳn để tiết kiệm pin.
3.  **Thức dậy (`NotificationReceiver.java`)**: Đến đúng 8 giờ, Android sẽ cầm cái phiếu hẹn đó và đập cửa gọi `NotificationReceiver` dậy.
4.  **Hành động**: `NotificationReceiver` tỉnh dậy trong 1 giây, nó chạy lệnh `notify()` để đẩy cái thông báo lên màn hình, sau đó nó lại đi ngủ tiếp.

---

## Bước 4: Giải mã các file quan trọng nhất

### 1. `DB.java` (Cơ sở dữ liệu)
Hãy coi đây là một cuốn sổ tay. Các hàm `addTask`, `deleteTask` thực chất là các lệnh: Viết thêm một dòng, hoặc gạch đi một dòng trong cuốn sổ đó.

### 2. `TaskAdapter.java` (Bộ máy danh sách)
Đây là file quan trọng nhất để hiển thị. Nó có 2 việc:
-   `onCreateViewHolder`: "Xây" cái khung cho một dòng Task (dựa trên file `item_task.xml`).
-   `onBindViewHolder`: "Điền" nội dung (tiêu đề, ngày tháng) vào cái khung vừa xây.

### 3. `mobile_navigation.xml` (Bản đồ)
Thay vì viết code chuyển màn hình phức tạp, file này cho bạn cái nhìn tổng quan: Màn hình A nối với màn hình B bằng mũi tên nào.

---

## Lời khuyên để bạn tự làm được:

1.  **Đừng đọc hết code cùng một lúc**: Hãy chọn 1 tính năng (ví dụ: Thêm Task) và đi theo dấu vết của nó. Nhấn **Ctrl + Click** vào các tên hàm trong Android Studio để xem nó dẫn đi đâu.
2.  **Thử xóa và sửa**: Hãy thử đổi màu cái nút trong XML, hoặc đổi dòng chữ thông báo trong Java. Khi bạn thấy thay đổi đó hiện lên điện thoại, bạn sẽ bắt đầu hiểu nó hoạt động thế nào.
3.  **Tự gõ lại**: Đừng copy-paste. Hãy nhìn code của tôi và tự gõ lại từng dòng. Khi gõ, bạn sẽ thấy Android Studio gợi ý code, đó là cách học nhanh nhất.

Bạn muốn tôi giải thích sâu hơn về **dòng code cụ thể nào** không? Đừng ngại hỏi nhé!

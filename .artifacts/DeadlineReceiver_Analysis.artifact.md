# Phân tích chi tiết DeadlineReceiver.java

Tài liệu này giải thích các Class và Phương thức trong file `DeadlineReceiver.java`. Đây là thành phần "thực thi" việc hiển thị thông báo ra màn hình khi báo thức kêu.

---

## 1. Các Class được sử dụng trong file

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **DeadlineReceiver** | **Tự build** | Lớp kế thừa từ `BroadcastReceiver`, đóng vai trò là nơi đón nhận tín hiệu từ hệ thống. |
| **BroadcastReceiver** | Có sẵn (Android SDK) | Lớp nền tảng để nhận các thông điệp gửi từ hệ thống hoặc các ứng dụng khác. |
| **Context** | Có sẵn (Android SDK) | Dùng để truy cập dịch vụ hệ thống (như dịch vụ thông báo) và tạo Intent. |
| **Intent** | Có sẵn (Android SDK) | Chứa dữ liệu về Task được gửi từ báo thức và dùng để định nghĩa hành động mở App. |
| **PendingIntent** | Có sẵn (Android SDK) | Gói gọn một Intent để hệ thống có thể kích hoạt nó khi người dùng nhấn vào thông báo. |
| **NotificationCompat.Builder** | Có sẵn (AndroidX) | Công cụ giúp thiết kế nội dung và hình thức của thông báo (icon, tiêu đề, nội dung...). |
| **NotificationManager** | Có sẵn (Android SDK) | Dịch vụ chịu trách nhiệm đẩy thông báo lên thanh trạng thái của điện thoại. |
| **MainActivity** | **Tự build** | Màn hình chính của ứng dụng, sẽ được mở ra khi người dùng nhấn vào thông báo. |
| **DeadlineNotificationManager** | **Tự build** | Dùng để lấy mã ID của kênh thông báo (`CHANNEL_ID`). |

---

## 2. Các phương thức quan trọng

### Class: `DeadlineReceiver` (Tự build)
*   **`onReceive(Context context, Intent intent)`**:
    *   *Ý nghĩa:* Đây là phương thức duy nhất và quan trọng nhất. Nó sẽ tự động được hệ thống gọi khi đến giờ báo thức đã hẹn.
    *   *Sử dụng:* Lấy ra ID và tiêu đề của Task, sau đó chuẩn bị nội dung để hiển thị thông báo.

### Các phương thức từ thư viện hệ thống
*   **`intent.getIntExtra(...) / getStringExtra(...)`**:
    *   *Ý nghĩa:* Lấy lại các thông tin đã được "gửi kèm" từ lúc đặt lịch (như ID task).
*   **`mainIntent.setFlags(...)`**:
    *   *Ý nghĩa:* Cấu hình cách ứng dụng mở ra. Ở đây là xóa các tác vụ cũ và mở ứng dụng như mới để tránh bị lỗi chồng màn hình.
*   **`PendingIntent.getActivity(...)`**:
    *   *Ý nghĩa:* Tạo ra một hành động chờ sẵn. Khi người dùng chạm vào thông báo, hệ thống sẽ thực thi hành động này để mở `MainActivity`.
*   **Các hàm của `Builder` (`setSmallIcon`, `setContentTitle`, `setPriority`...)**:
    *   *Ý nghĩa:* Thiết lập "bộ mặt" của thông báo: hình icon nhỏ, tiêu đề "⏰ Sắp đến hạn!", mức độ ưu tiên cao để hiện lên ngay lập tức.
    *   *Sử dụng:* `setAutoCancel(true)` để thông báo tự biến mất sau khi người dùng nhấn vào.
*   **`nm.notify(int id, Notification notification)`**:
    *   *Ý nghĩa:* Lệnh cuối cùng để "xuất bản" thông báo ra màn hình.
    *   *Sử dụng:* Dùng `taskId` làm ID thông báo để nếu có nhiều task cùng đến hạn, chúng sẽ hiện thành các thông báo riêng biệt thay vì đè lên nhau.

---

## 3. Quy trình "Đánh thức và Báo động"

Hãy tưởng tượng `DeadlineReceiver` như một **"Lính gác"**:
1.  **Đứng chờ:** Lính gác này bình thường không chạy, nó chỉ xuất hiện khi hệ thống Android "gọi tên" nó (thông qua báo thức từ `AlarmManager`).
2.  **Nhận tin:** Khi được gọi, nó mở phong bì (`Intent`) để xem "Hôm nay báo tin cho Task nào?".
3.  **Chuẩn bị:** Nó soạn một tờ thông báo đẹp đẽ với đầy đủ icon, rung và chuông.
4.  **Báo động:** Nó đẩy thông báo lên thanh trạng thái. Nếu người dùng nhấn vào, nó sẽ dẫn họ về thẳng ứng dụng (`MainActivity`).

> [!TIP]
> **Điểm hay:** Code có sử dụng `BigTextStyle`, giúp nội dung thông báo có thể mở rộng ra để đọc được nhiều chữ hơn nếu tiêu đề Task quá dài.

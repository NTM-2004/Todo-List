# Phân tích chi tiết DeadlineNotificationManager.java

Tài liệu này giải thích các Class và Phương thức trong file `DeadlineNotificationManager.java`. Đây là lớp chịu trách nhiệm quản lý hệ thống nhắc nhở thông báo khi các công việc sắp đến hạn.

---

## 1. Các Class được sử dụng trong file

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **DeadlineNotificationManager** | **Tự build** | Lớp chính chứa các phương thức tĩnh (static) để điều khiển thông báo. |
| **AlarmManager** | Có sẵn (Android SDK) | Dịch vụ hệ thống giúp đặt lịch thực hiện một hành động vào một thời điểm chính xác trong tương lai. |
| **NotificationChannel** | Có sẵn (Android SDK) | Phân loại thông báo (bắt buộc từ Android 8.0+). Giúp người dùng có thể bật/tắt từng loại thông báo riêng biệt. |
| **NotificationManager** | Có sẵn (Android SDK) | Dịch vụ hệ thống dùng để hiển thị hoặc quản lý các thông báo trên thanh trạng thái. |
| **PendingIntent** | Có sẵn (Android SDK) | Một loại Intent "đặc biệt" được giao cho hệ thống, để hệ thống thay mặt ứng dụng thực thi hành động vào một thời điểm nào đó. |
| **Intent** | Có sẵn (Android SDK) | Đối tượng dùng để mô tả một hành động (như mở một lớp nhận thông báo `DeadlineReceiver`). |
| **Context** | Có sẵn (Android SDK) | Môi trường của ứng dụng, dùng để truy cập các dịch vụ hệ thống và tài nguyên. |
| **Build** | Có sẵn (Android SDK) | Dùng để kiểm tra phiên bản Android (SDK_INT) nhằm đảm bảo code chạy đúng trên cả máy cũ và máy mới. |
| **Task** | **Tự build** | Lớp Model chứa thông tin về hạn chót (deadline) và cài đặt thông báo của công việc. |
| **DeadlineReceiver** | **Tự build** | Lớp sẽ nhận tín hiệu từ hệ thống khi đến giờ và thực hiện việc hiển thị thông báo ra màn hình. |

---

## 2. Các phương thức quan trọng

### Class: `DeadlineNotificationManager` (Tự build)
*   **`createNotificationChannel(Context context)`**:
    *   *Ý nghĩa:* Đăng ký một kênh thông báo với hệ thống Android.
    *   *Sử dụng:* Được gọi một lần khi app khởi chạy (`MainActivity`) để đảm bảo hệ thống chấp nhận các thông báo từ app. Nó thiết lập tên kênh, độ ưu tiên (High) và chế độ rung.
*   **`scheduleDeadlineNotification(Context context, Task task)`**:
    *   *Ý nghĩa:* Đặt lịch nhắc nhở cho một công việc cụ thể.
    *   *Sử dụng:* Tính toán thời gian cần báo thức (trước deadline 1 giờ). Nếu thời gian đó chưa trôi qua, nó sẽ gửi một `PendingIntent` tới `AlarmManager` để hẹn giờ.
*   **`cancelNotification(Context context, int taskId)`**:
    *   *Ý nghĩa:* Hủy bỏ một lịch nhắc nhở đã đặt trước đó.
    *   *Sử dụng:* Thường được gọi khi người dùng xóa task hoặc tắt tính năng thông báo của task đó.

### Các phương thức từ thư viện hệ thống
*   **`context.getSystemService(...)`**: Lấy ra các công cụ của hệ thống Android như `AlarmManager` hoặc `NotificationManager`.
*   **`alarmManager.setExactAndAllowWhileIdle(...)`**:
    *   *Ý nghĩa:* Đặt báo thức với độ chính xác tuyệt đối, ngay cả khi điện thoại đang ở chế độ tiết kiệm pin (Doze mode).
*   **`PendingIntent.getBroadcast(...)`**: Tạo ra một tín hiệu gửi đến một BroadcastReceiver (`DeadlineReceiver`) khi báo thức kêu.
*   **`intent.putExtra(...)`**: Đính kèm dữ liệu (như ID và Tiêu đề của task) vào Intent để khi thông báo hiện lên, nó biết đang nhắc về việc gì.

---

## 3. Cơ chế hoạt động của hệ thống nhắc nhở

Hệ thống này hoạt động giống như một **"Dịch vụ hẹn giờ thuê"**:
1.  **Đăng ký kênh:** App nói với Android: "Tôi có một loại thông báo tên là 'Nhắc nhở deadline', hãy cho phép tôi gửi nó nhé".
2.  **Đặt lịch:** Khi bạn lưu task, app tính toán: "Hạn là 10h, vậy 9h phải báo". App đưa một "mảnh giấy hẹn" (`PendingIntent`) cho Android (`AlarmManager`) và bảo: "Đúng 9h hãy kích hoạt mảnh giấy này giúp tôi".
3.  **Kích hoạt:** Đúng 9h, dù app của bạn có đang đóng, Android vẫn sẽ mở mảnh giấy đó ra và gọi đến `DeadlineReceiver`.
4.  **Hiển thị:** `DeadlineReceiver` sẽ lấy thông tin từ mảnh giấy và đẩy thông báo thực sự lên màn hình cho bạn thấy.

> [!WARNING]
> **Lưu ý về độ chính xác:**
> Từ Android 12 trở lên, việc sử dụng "Báo thức chính xác" (`Exact Alarm`) yêu cầu quyền đặc biệt. Code hiện tại đang sử dụng các hàm chuẩn để đảm bảo thông báo hiện lên đúng lúc nhất có thể.

# Phân tích chi tiết MainActivity.java

Tài liệu này giải thích các Class và Phương thức được sử dụng trong file `MainActivity.java` để giúp bạn hiểu cách ứng dụng hoạt động.

---

## 1. Các Class được sử dụng trong file

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **MainActivity** | **Tự build** | Lớp chính khởi chạy ứng dụng, kế thừa từ `AppCompatActivity`. |
| **AppCompatActivity** | Có sẵn (AndroidX) | Lớp nền tảng để tạo một Activity hỗ trợ các tính năng mới trên phiên bản Android cũ. |
| **AppBarConfiguration** | Có sẵn (Navigation) | Dùng để cấu hình cách thanh tiêu đề (AppBar) hoạt động với hệ thống Navigation. |
| **NavController** | Có sẵn (Navigation) | Đối tượng điều phối việc chuyển đổi giữa các Fragment trong ứng dụng. |
| **Bundle** | Có sẵn (Android SDK) | Một tập bản đồ (map) dùng để truyền dữ liệu giữa các Activity hoặc lưu trạng thái. |
| **Toolbar** | Có sẵn (AndroidX) | Thành phần giao diện thay thế cho ActionBar truyền thống, linh hoạt hơn. |
| **NavHostFragment** | Có sẵn (Navigation) | Một Fragment đặc biệt đóng vai trò là "vùng chứa" (container) cho các Fragment khác khi điều hướng. |
| **FloatingActionButton** | Có sẵn (Material) | Nút tròn lơ lửng trên giao diện (thường dùng cho các hành động chính như Thêm). |
| **DeadlineNotificationManager** | **Tự build** | Lớp tùy chỉnh quản lý việc tạo kênh thông báo và đặt lịch thông báo deadline. |
| **Build** | Có sẵn (Android SDK) | Chứa thông tin về thiết bị và phiên bản Android đang chạy. |
| **Manifest** | Có sẵn (Android SDK) | Chứa danh sách các quyền (permissions) của hệ thống. |
| **ContextCompat** | Có sẵn (AndroidX) | Tiện ích giúp kiểm tra quyền hoặc lấy tài nguyên một cách an toàn trên các phiên bản Android. |
| **PackageManager** | Có sẵn (Android SDK) | Cung cấp thông tin về các gói ứng dụng và quyền đã được cấp. |
| **ActivityCompat** | Có sẵn (AndroidX) | Hỗ trợ thực hiện các tác vụ liên quan đến Activity (như yêu cầu quyền) tương thích nhiều phiên bản. |
| **Menu / MenuInflater** | Có sẵn (Android SDK) | Dùng để tạo và quản lý menu (dấu 3 chấm hoặc nút trên thanh công cụ). |
| **NavigationUI** | Có sẵn (Navigation) | Kết nối các thành phần giao diện (như Toolbar) với NavController để tự động cập nhật tiêu đề/nút quay lại. |

---

## 2. Các phương thức của từng Class được dùng trong file

### Class: `MainActivity` (Tự build)
*   **`onCreate(Bundle savedInstanceState)`**:
    *   *Ý nghĩa:* Đây là "điểm khởi đầu" của Activity. Nó được hệ thống gọi khi Activity lần đầu được tạo ra.
    *   *Sử dụng:* Dùng để thiết lập layout, khởi tạo các thành phần giao diện và logic ban đầu.
*   **`onCreateOptionsMenu(Menu menu)`**:
    *   *Ý nghĩa:* Được gọi để khởi tạo menu tùy chọn (thường ở góc phải Toolbar).
    *   *Sử dụng:* Dùng để nạp (inflate) file XML menu vào thanh công cụ.
*   **`onSupportNavigateUp()`**:
    *   *Ý nghĩa:* Xử lý khi người dùng nhấn vào nút "Back" hoặc mũi tên quay lại trên Toolbar.
    *   *Sử dụng:* Điều phối việc quay lại màn hình trước đó thông qua `NavController`.

### Class: `AppCompatActivity` (Có sẵn)
*   **`setContentView(int layoutResID)`**:
    *   *Ý nghĩa:* Gắn giao diện XML vào Activity.
    *   *Sử dụng:* `setContentView(R.layout.activity_main)` để hiển thị giao diện chính.
*   **`findViewById(int id)`**:
    *   *Ý nghĩa:* Tìm một View (nút, text, toolbar...) dựa trên ID đã đặt trong XML.
*   **`setSupportActionBar(Toolbar toolbar)`**:
    *   *Ý nghĩa:* Biến một đối tượng `Toolbar` bình thường thành thanh tiêu đề chính thức của Activity.
*   **`getSupportFragmentManager()`**:
    *   *Ý nghĩa:* Lấy trình quản lý Fragment để thao tác với các Fragment bên trong Activity.

### Class: `NavHostFragment` (Có sẵn)
*   **`findFragmentById(int id)`**:
    *   *Ý nghĩa:* Tìm Fragment đóng vai trò là "NavHost" (vùng chứa điều hướng) trong layout.
*   **`getNavController()`**:
    *   *Ý nghĩa:* Lấy đối tượng điều khiển điều hướng (`NavController`) từ vùng chứa này.

### Class: `NavController` (Có sẵn)
*   **`navigate(int resId)`**:
    *   *Ý nghĩa:* Thực hiện chuyển màn hình đến một đích đến (destination) được định nghĩa trong file `nav_graph.xml`.
    *   *Sử dụng:* Chuyển sang màn hình thêm task khi nhấn FAB.

### Class: `FloatingActionButton` (Có sẵn)
*   **`setOnClickListener(View.OnClickListener l)`**:
    *   *Ý nghĩa:* Gắn sự kiện lắng nghe khi người dùng nhấn vào nút.

### Class: `DeadlineNotificationManager` (Tự build)
*   **`createNotificationChannel(Context context)`**:
    *   *Ý nghĩa:* Tạo một "Kênh thông báo" (Notification Channel). Từ Android 8.0 trở lên, mọi thông báo đều phải thuộc về một kênh nào đó.

### Class: `ContextCompat` & `ActivityCompat` (Có sẵn)
*   **`checkSelfPermission(...)`**: Kiểm tra xem ứng dụng đã có quyền (ví dụ: gửi thông báo) hay chưa.
*   **`requestPermissions(...)`**: Hiển thị hộp thoại yêu cầu người dùng cấp quyền nếu chưa có.

### Class: `MenuInflater` (Có sẵn)
*   **`inflate(int menuRes, Menu menu)`**: Đọc file XML menu và hiển thị các icon/chữ lên thanh công cụ.

### Class: `NavigationUI` (Có sẵn)
*   **`navigateUp(NavController, AppBarConfiguration)`**: Tự động tính toán xem nút quay lại nên làm gì (quay lại fragment trước hay đóng app).

---

> [!TIP]
> **Mẹo học nhanh:**
> - Các class bắt đầu bằng `AppCompat`, `Nav`, `Material` thường là thư viện có sẵn từ Google.
> - Các phương thức `@Override` là những phương thức "mượn" từ lớp cha để tùy chỉnh lại theo ý mình.

# Phân tích chi tiết HomeFragment.java

Tài liệu này giải thích các Class và Phương thức trong file `HomeFragment.java`. Đây là màn hình chính của ứng dụng, chứa các Tab danh sách công việc và thanh tìm kiếm.

---

## 1. Các Class được sử dụng trong file

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **HomeFragment** | **Tự build** | Lớp chính điều khiển màn hình Trang chủ, kế thừa từ `Fragment`. |
| **Fragment** | Có sẵn (AndroidX) | Thành phần giao diện độc lập đại diện cho một phần màn hình. |
| **TabLayout** | Có sẵn (Material) | Thanh điều hướng chứa các Tab ("Tất cả", "Chưa xong", "Hoàn thành"). |
| **ViewPager2** | Có sẵn (AndroidX) | Vùng chứa cho phép vuốt qua lại giữa các màn hình danh sách tương ứng với Tab. |
| **EditText** | Có sẵn (Android SDK) | Ô nhập văn bản dùng để thực hiện chức năng tìm kiếm (Search). |
| **ImageButton** | Có sẵn (Android SDK) | Nút bấm dạng hình ảnh dùng cho nút Sắp xếp (Sort). |
| **DB** | **Tự build** | Lớp quản lý cơ sở dữ liệu để lấy các hằng số bộ lọc và tên cột. |
| **TaskViewModel** | **Tự build** | Lớp quản lý dữ liệu và trạng thái (tìm kiếm, sắp xếp) để chia sẻ giữa các Fragment. |
| **TaskPagerAdapter** | **Tự build** | Lớp điều phối việc hiển thị đúng màn hình danh sách cho mỗi Tab. |
| **TabLayoutMediator** | Có sẵn (Material) | Công cụ giúp kết nối `TabLayout` với `ViewPager2` để chúng hoạt động đồng bộ. |
| **ViewModelProvider** | Có sẵn (Lifecycle) | Dùng để khởi tạo hoặc lấy ra một đối tượng `ViewModel`. |
| **TextWatcher** | Có sẵn (Android SDK) | Interface lắng nghe sự thay đổi của văn bản trong ô tìm kiếm. |
| **FilterSortBottomSheet** | **Tự build** | Bảng điều khiển sắp xếp hiện ra khi nhấn nút Sort. |

---

## 2. Các phương thức quan trọng

### Class: `HomeFragment` (Tự build)
*   **`onCreateView(...)`**:
    *   *Ý nghĩa:* Nạp giao diện XML (`fragment_home.xml`) vào Fragment.
*   **`onViewCreated(...)`**:
    *   *Ý nghĩa:* Khởi tạo các đối tượng sau khi view đã được tạo xong.
    *   *Sử dụng:* Khởi tạo Database, kết nối với `ViewModel` và gọi các hàm thiết lập giao diện.
*   **`setupTabs()`**:
    *   *Ý nghĩa:* Thiết lập các Tab và kết nối chúng với ViewPager2.
    *   *Sử dụng:* Tạo `TaskPagerAdapter` và dùng `TabLayoutMediator` để gán tên cho các tab.
*   **`setupSearch()`**:
    *   *Ý nghĩa:* Thiết lập logic tìm kiếm.
    *   *Sử dụng:* Gắn một `TextWatcher` vào ô `etSearch`. Mỗi khi người dùng gõ chữ, nó sẽ báo cho `ViewModel` để lọc lại danh sách ở tất cả các tab.
*   **`setupSortFilter()`**:
    *   *Ý nghĩa:* Thiết lập nút sắp xếp.
    *   *Sử dụng:* Khi nhấn nút Sort, nó hiển thị `FilterSortBottomSheet`. Khi người dùng chọn xong, nó cập nhật lại tiêu chí sắp xếp vào `ViewModel`.

### Các phương thức từ thư viện và class khác
*   **`viewModel.setSearchQuery(...)`**: Gửi từ khóa tìm kiếm vào ViewModel để các màn hình con (TaskListFragment) biết và cập nhật dữ liệu.
*   **`viewModel.setSortOrder(...)`**: Gửi yêu cầu sắp xếp mới (ví dụ: "priority DESC") vào ViewModel.
*   **`sheet.show(...)`**: Hiển thị bảng chọn sắp xếp trượt từ dưới lên.
*   **`attach()`**: Phương thức của `TabLayoutMediator` để chính thức kích hoạt việc kết nối giữa Tab và ViewPager.

---

## 3. Vai trò "Trạm điều phối" của HomeFragment

Nếu coi ứng dụng là một ngôi nhà:
- **HomeFragment** chính là **Sảnh chính**. Nó không trực tiếp cầm danh sách công việc, nhưng nó giữ **Thanh tìm kiếm** và **Bộ lọc**.
- Khi người dùng gõ tìm kiếm ở sảnh chính, nó dùng **ViewModel** để "hét" lên cho tất cả các phòng (Tab) bên trong biết: "Này, chỉ hiện những việc có chữ này thôi nhé!".
- **TaskPagerAdapter** đóng vai trò là các cánh cửa dẫn vào các phòng khác nhau tùy theo lựa chọn của người dùng trên **TabLayout**.

> [!IMPORTANT]
> **Điểm cần lưu ý về ViewModel:**
> Code sử dụng `new ViewModelProvider(requireActivity())`. Việc dùng `requireActivity()` giúp `HomeFragment` và các `TaskListFragment` bên trong dùng chung **duy nhất một bản ghi dữ liệu**. Nhờ đó, khi bạn gõ tìm kiếm ở Home, danh sách ở các Tab sẽ tự động thay đổi theo.

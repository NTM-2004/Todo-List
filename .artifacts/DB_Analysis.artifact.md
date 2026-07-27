# Phân tích chi tiết DB.java

Tài liệu này giải thích các Class và Phương thức được sử dụng trong file `DB.java`, lớp quản lý cơ sở dữ liệu SQLite cho ứng dụng Todo List.

---

## 1. Các Class được sử dụng trong file

| Tên Class | Loại | Ý nghĩa / Vai trò |
| :--- | :--- | :--- |
| **DB** | **Tự build** | Lớp chính quản lý database, kế thừa từ `SQLiteOpenHelper`. |
| **SQLiteOpenHelper** | Có sẵn (Android SDK) | Lớp hỗ trợ quản lý việc tạo database và quản lý phiên bản (versioning). |
| **SQLiteDatabase** | Có sẵn (Android SDK) | Lớp cung cấp các phương thức để tương tác trực tiếp với database (thêm, sửa, xóa, truy vấn). |
| **ContentValues** | Có sẵn (Android SDK) | Dùng để chứa một bộ dữ liệu (cặp key-value) để chèn hoặc cập nhật vào bảng. |
| **Cursor** | Có sẵn (Android SDK) | Đối tượng dùng để duyệt qua kết quả trả về từ một câu lệnh truy vấn (SELECT). |
| **Task** | **Tự build** | Lớp Model đại diện cho một công việc (Task) trong ứng dụng. |
| **List / ArrayList** | Có sẵn (Java) | Các cấu trúc dữ liệu dùng để chứa danh sách các đối tượng Task. |

---

## 2. Các phương thức của từng Class được dùng trong file

### Class: `DB` (Tự build)
*   **`DB(Context context)`**:
    *   *Ý nghĩa:* Hàm khởi tạo (Constructor).
    *   *Sử dụng:* Thiết lập tên database (`todolist.db`) và phiên bản hiện tại.
*   **`onCreate(SQLiteDatabase db)`**:
    *   *Ý nghĩa:* Được gọi khi database được tạo lần đầu tiên.
    *   *Sử dụng:* Thực thi câu lệnh SQL `CREATE TABLE` để tạo bảng `tasks`.
*   **`onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)`**:
    *   *Ý nghĩa:* Được gọi khi phiên bản database thay đổi (ví dụ bạn thêm cột mới).
    *   *Sử dụng:* Dùng câu lệnh `ALTER TABLE` để cập nhật cấu trúc bảng mà không làm mất dữ liệu cũ.
*   **`addTask(Task task)`**:
    *   *Ý nghĩa:* Thêm một công việc mới vào database.
    *   *Sử dụng:* Chuyển đối tượng `Task` thành `ContentValues` rồi gọi `db.insert()`. Trả về ID của hàng vừa chèn.
*   **`updateTask(Task task)`**:
    *   *Ý nghĩa:* Cập nhật thông tin của một công việc đã tồn tại.
    *   *Sử dụng:* Dùng `db.update()` dựa trên `id` của Task.
*   **`deleteTask(int id)`**:
    *   *Ý nghĩa:* Xóa một công việc khỏi database.
    *   *Sử dụng:* Dùng `db.delete()` dựa trên `id`.
*   **`getTask(int id)`**:
    *   *Ý nghĩa:* Lấy thông tin chi tiết của 1 công việc theo ID.
*   **`getAllTasks()`**:
    *   *Ý nghĩa:* Lấy toàn bộ danh sách công việc hiện có.
*   **`getTasksSortedFiltered(...)`**:
    *   *Ý nghĩa:* Truy vấn danh sách công việc có bộ lọc (hoàn thành, chưa hoàn thành, quá hạn) và sắp xếp (theo deadline, ngày tạo...).
*   **`queryTasks(...)` (private)**:
    *   *Ý nghĩa:* Hàm bổ trợ chứa logic xây dựng câu lệnh SQL `SELECT` phức tạp dựa trên các tham số lọc và tìm kiếm.
*   **`taskToValues(Task task)` (private)**:
    *   *Ý nghĩa:* Chuyển đổi dữ liệu từ đối tượng Java (`Task`) sang định dạng mà database hiểu được (`ContentValues`).
*   **`cursorToTask(Cursor cursor)` (private)**:
    *   *Ý nghĩa:* Chuyển đổi kết quả từ database (`Cursor`) ngược lại thành đối tượng Java (`Task`).

### Class: `SQLiteDatabase` (Có sẵn)
*   **`getWritableDatabase()`**: Mở database để có quyền ghi (thêm, sửa, xóa).
*   **`getReadableDatabase()`**: Mở database chỉ để đọc dữ liệu.
*   **`execSQL(String sql)`**: Thực thi một câu lệnh SQL trực tiếp (thường dùng cho tạo bảng hoặc alter).
*   **`insert(...) / update(...) / delete(...)`**: Các hàm tiện ích để thao tác dữ liệu mà không cần viết SQL thuần.
*   **`rawQuery(String sql, String[] args)`**: Chạy một câu lệnh SQL truy vấn và trả về một `Cursor`.

### Class: `Cursor` (Có sẵn)
*   **`moveToFirst()`**: Di chuyển con trỏ đến hàng đầu tiên của kết quả.
*   **`moveToNext()`**: Di chuyển con trỏ đến hàng tiếp theo.
*   **`getColumnIndexOrThrow(String name)`**: Lấy vị trí (index) của cột dựa trên tên cột.
*   **`getString() / getInt() / getLong()`**: Lấy dữ liệu tại cột hiện tại theo kiểu dữ liệu tương ứng.
*   **`close()`**: Đóng con trỏ sau khi dùng xong để giải phóng bộ nhớ.

---

> [!IMPORTANT]
> **Lưu ý về Luồng Dữ Liệu:**
> 1. Khi lưu: `Task` (Java) -> `taskToValues` -> `SQLiteDatabase.insert`.
> 2. Khi đọc: `SQLiteDatabase.rawQuery` -> `Cursor` -> `cursorToTask` -> `Task` (Java).

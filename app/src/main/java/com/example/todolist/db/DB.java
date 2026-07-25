package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.model.Task;
import com.example.todolist.model.User;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 2;

    // Tasks table
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_CREATED_TIME = "created_time";
    public static final String COLUMN_DEADLINE = "deadline";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NOTIFY = "notify_enabled";

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD_HASH = "password_hash";
    public static final String COLUMN_EMAIL = "email";

    // Sort options
    public static final String SORT_BY_DEADLINE = "deadline ASC";
    public static final String SORT_BY_CREATED = "created_time DESC";
    public static final String SORT_BY_PRIORITY = "priority DESC";

    // Filter options
    public static final int FILTER_ALL = 0;
    public static final int FILTER_INCOMPLETE = 1;
    public static final int FILTER_COMPLETE = 2;
    public static final int FILTER_OVERDUE = 3;

    private static final String TABLE_TASKS_CREATE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_CREATED_TIME + " INTEGER, " +
                    COLUMN_DEADLINE + " INTEGER, " +
                    COLUMN_STATUS + " INTEGER DEFAULT 0, " +
                    COLUMN_PRIORITY + " INTEGER DEFAULT 0, " +
                    COLUMN_USER_ID + " INTEGER DEFAULT 0, " +
                    COLUMN_NOTIFY + " INTEGER DEFAULT 0" +
                    ");";

    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD_HASH + " TEXT, " +
                    COLUMN_EMAIL + " TEXT" +
                    ");";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_TASKS_CREATE);
        db.execSQL(TABLE_USERS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Migrate v1 → v2: add new columns to tasks, create users table
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_PRIORITY + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_USER_ID + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_NOTIFY + " INTEGER DEFAULT 0");
            db.execSQL(TABLE_USERS_CREATE);
        }
    }

    // ===================== USER METHODS =====================

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD_HASH, hashPassword(user.getPasswordHash()));
        values.put(COLUMN_EMAIL, user.getEmail());
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean usernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public User validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashed = hashPassword(password);
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD_HASH + "=?",
                new String[]{username, hashed}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = cursorToUser(cursor);
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
        user.setPasswordHash(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_HASH)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
        return user;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return password; // fallback
        }
    }

    // ===================== TASK METHODS =====================

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = taskToValues(task);
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = taskToValues(task);
        int rows = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return rows;
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Task task = cursorToTask(cursor);
            cursor.close();
            db.close();
            return task;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public List<Task> getAllTasks(int userId) {
        return queryTasks(userId, FILTER_ALL, SORT_BY_CREATED, null);
    }

    public List<Task> getTasksSortedFiltered(int userId, int filter, String sortBy, String search) {
        return queryTasks(userId, filter, sortBy, search);
    }

    private List<Task> queryTasks(int userId, int filter, String sortBy, String search) {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Validate sortBy parameter to prevent SQL injection and null errors
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = SORT_BY_CREATED;
        }

        StringBuilder where = new StringBuilder(COLUMN_USER_ID + " = ?");
        List<String> args = new ArrayList<>();
        args.add(String.valueOf(userId));
        
        long now = System.currentTimeMillis();

        switch (filter) {
            case FILTER_INCOMPLETE:
                where.append(" AND ").append(COLUMN_STATUS).append(" = 0");
                break;
            case FILTER_COMPLETE:
                where.append(" AND ").append(COLUMN_STATUS).append(" = 1");
                break;
            case FILTER_OVERDUE:
                where.append(" AND ").append(COLUMN_STATUS).append(" = 0 AND ")
                        .append(COLUMN_DEADLINE).append(" > 0 AND ")
                        .append(COLUMN_DEADLINE).append(" < ").append(now);
                break;
        }

        if (search != null && !search.isEmpty()) {
            where.append(" AND (").append(COLUMN_TITLE).append(" LIKE ? OR ")
                    .append(COLUMN_CONTENT).append(" LIKE ?)");
            String searchPattern = "%" + search + "%";
            args.add(searchPattern);
            args.add(searchPattern);
        }

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_TASKS + " WHERE " + where + " ORDER BY " + sortBy,
                    args.toArray(new String[0]));

            if (cursor.moveToFirst()) {
                do {
                    list.add(cursorToTask(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return list;
    }

    public List<Task> getTasksWithDeadlineNotification() {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        long now = System.currentTimeMillis();
        long oneHour = 60 * 60 * 1000L;
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_TASKS +
                        " WHERE " + COLUMN_STATUS + " = 0 AND " +
                        COLUMN_NOTIFY + " = 1 AND " +
                        COLUMN_DEADLINE + " > " + now + " AND " +
                        COLUMN_DEADLINE + " < " + (now + oneHour * 24), null);
        if (cursor.moveToFirst()) {
            do { list.add(cursorToTask(cursor)); } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int[] getStatistics(int userId) {
        // returns [total, complete, incomplete, overdue]
        SQLiteDatabase db = this.getReadableDatabase();
        long now = System.currentTimeMillis();

        int total = countQuery(db, "SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " + COLUMN_USER_ID + "=" + userId);
        int complete = countQuery(db, "SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " + COLUMN_USER_ID + "=" + userId + " AND " + COLUMN_STATUS + "=1");
        int incomplete = countQuery(db, "SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " + COLUMN_USER_ID + "=" + userId + " AND " + COLUMN_STATUS + "=0");
        int overdue = countQuery(db, "SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " + COLUMN_USER_ID + "=" + userId + " AND " + COLUMN_STATUS + "=0 AND " + COLUMN_DEADLINE + ">0 AND " + COLUMN_DEADLINE + "<" + now);

        db.close();
        return new int[]{total, complete, incomplete, overdue};
    }

    public int[] getCategoryStats(int userId) {
        // returns count per category: [Study, Work, Personal, Health, Finance, Other]
        String[] categories = {"Study", "Work", "Personal", "Health", "Finance", "Other"};
        int[] counts = new int[categories.length];
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < categories.length; i++) {
            counts[i] = countQuery(db, "SELECT COUNT(*) FROM " + TABLE_TASKS +
                    " WHERE " + COLUMN_USER_ID + "=" + userId +
                    " AND " + COLUMN_CATEGORY + "='" + categories[i] + "'");
        }
        db.close();
        return counts;
    }

    private int countQuery(SQLiteDatabase db, String query) {
        Cursor c = db.rawQuery(query, null);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    private ContentValues taskToValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_CONTENT, task.getContent());
        values.put(COLUMN_CATEGORY, task.getCategory());
        values.put(COLUMN_CREATED_TIME, task.getCreatedTime());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_STATUS, task.getStatus());
        values.put(COLUMN_PRIORITY, task.getPriority());
        values.put(COLUMN_USER_ID, task.getUserId());
        values.put(COLUMN_NOTIFY, task.isNotifyEnabled() ? 1 : 0);
        return values;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
        task.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
        task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
        task.setCreatedTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_TIME)));
        task.setDeadline(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE)));
        task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
        task.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY)));
        task.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
        task.setNotifyEnabled(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFY)) == 1);
        return task;
    }
}

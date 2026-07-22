package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_CREATED_TIME = "created_time";
    public static final String COLUMN_DEADLINE = "deadline";
    public static final String COLUMN_STATUS = "status";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_CREATED_TIME + " INTEGER, " +
                    COLUMN_DEADLINE + " INTEGER, " +
                    COLUMN_STATUS + " INTEGER" +
                    ");";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_CONTENT, task.getContent());
        values.put(COLUMN_CATEGORY, task.getCategory());
        values.put(COLUMN_CREATED_TIME, task.getCreatedTime());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_STATUS, task.getStatus());

        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + COLUMN_CREATED_TIME + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                task.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                task.setCreatedTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_TIME)));
                task.setDeadline(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE)));
                task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public List<Task> getUnfinishedTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_STATUS + " = 0 ORDER BY " + COLUMN_DEADLINE + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                task.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                task.setCreatedTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_TIME)));
                task.setDeadline(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE)));
                task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_CONTENT, task.getContent());
        values.put(COLUMN_CATEGORY, task.getCategory());
        values.put(COLUMN_CREATED_TIME, task.getCreatedTime());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_STATUS, task.getStatus());

        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            Task task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            task.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
            task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
            task.setCreatedTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_TIME)));
            task.setDeadline(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE)));
            task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
            cursor.close();
            return task;
        }
        return null;
    }
}

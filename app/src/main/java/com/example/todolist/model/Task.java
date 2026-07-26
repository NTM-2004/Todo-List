package com.example.todolist.model;

public class Task {
    // Priority constants
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_HIGH = 2;

    // Status constants
    public static final int STATUS_INCOMPLETE = 0;
    public static final int STATUS_COMPLETE = 1;

    private int id;
    private String title;
    private String content;
    private String category;
    private Long createdTime;
    private Long deadline;
    private Integer status;
    private int priority;
    private boolean notifyEnabled;

    public Task() {}

    public Task(String title, String content, String category, Long createdTime,
                Long deadline, Integer status, int priority, boolean notifyEnabled) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdTime = createdTime;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.notifyEnabled = notifyEnabled;
    }

    public Task(int id, String title, String content, String category, Long createdTime,
                Long deadline, Integer status, int priority, int userId, boolean notifyEnabled) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdTime = createdTime;
        this.deadline = deadline;
        this.status = status;
        this.priority = priority;
        this.notifyEnabled = notifyEnabled;
    }

    public boolean isOverdue() {
        return status == STATUS_INCOMPLETE
                && deadline != null
                && deadline > 0
                && deadline < System.currentTimeMillis();
    }

    public boolean isCompleted() {
        return status == STATUS_COMPLETE;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Long getCreatedTime() { return createdTime; }
    public void setCreatedTime(Long createdTime) { this.createdTime = createdTime; }

    public Long getDeadline() { return deadline; }
    public void setDeadline(Long deadline) { this.deadline = deadline; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public boolean isNotifyEnabled() { return notifyEnabled; }
    public void setNotifyEnabled(boolean notifyEnabled) { this.notifyEnabled = notifyEnabled; }
}

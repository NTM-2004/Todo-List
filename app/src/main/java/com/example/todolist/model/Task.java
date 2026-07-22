package com.example.todolist.model;

public class Task {
    private int id;
    private String title;
    private String content;
    private String category;
    private Long createdTime;
    private Long deadline;
    private Integer status;

    public Task() {
    }

    public Task(String title, String content, String category, Long createdTime, Long deadline, Integer status) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdTime = createdTime;
        this.deadline = deadline;
        this.status = status;
    }

    public Task(int id, String title, String content, String category, Long createdTime, Long deadline, Integer status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdTime = createdTime;
        this.deadline = deadline;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

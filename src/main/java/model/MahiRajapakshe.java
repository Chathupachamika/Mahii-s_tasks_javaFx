package model;

import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
public class MahiRajapakshe {
    private Integer id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;


    public MahiRajapakshe(Integer id, String title, String description, LocalDate dueDate, boolean isCompleted, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }
    public MahiRajapakshe() {
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return (this.title != null && !this.title.isEmpty()) ? this.title : "Untitled Task";
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

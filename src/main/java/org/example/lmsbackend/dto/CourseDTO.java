package org.example.lmsbackend.dto;

public class CourseDTO {
    private String title;
    private String description;
    private int categoryId;
    private int instructorId;
    private String status;
    private double price;
    private String thumbnailUrl;

    public CourseDTO() {
        // ✅ Constructor mặc định cần thiết cho Jackson
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public int getInstructorId() { return instructorId; }
    public String getStatus() { return status; }
    public double getPrice() { return price; }
    public String getThumbnailUrl() { return thumbnailUrl; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public void setStatus(String status) { this.status = status; }
    public void setPrice(double price) { this.price = price; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
}

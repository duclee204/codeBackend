package org.example.lmsbackend.dto;

import java.time.LocalDateTime;

public class AssignmentSubmissionDTO {
    private Integer submissionId;
    private Integer assignmentId;
    private Integer studentId;
    private String submissionText;
    private String fileUrl;
    private LocalDateTime submittedAt;
    private LocalDateTime gradedAt; // ✅ Thêm nếu cần dùng setGradedAt

    // Getters and Setters
    public Integer getSubmissionId() { return submissionId; }
    public void setSubmissionId(Integer submissionId) { this.submissionId = submissionId; }

    public Integer getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Integer assignmentId) { this.assignmentId = assignmentId; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getSubmissionText() { return submissionText; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }

    @Override
    public String toString() {
        return "AssignmentSubmissionDTO{" +
                "submissionId=" + submissionId +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                ", submissionText='" + submissionText + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", submittedAt=" + submittedAt +
                ", gradedAt=" + gradedAt +
                '}';
    }
}

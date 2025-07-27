package org.example.lmsbackend.model;

import java.time.LocalDateTime;

public class AssignmentSubmission {
    private Integer submissionId;
    private Integer assignmentId;
    private Integer studentId;
    private String submissionText;
    private String fileUrl;
    private LocalDateTime submittedAt;
    private Integer pointsEarned;
    private String feedback;
    private LocalDateTime gradedAt;
    
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
    
    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }
    
    @Override
    public String toString() {
        return "AssignmentSubmission{" +
                "submissionId=" + submissionId +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                ", submissionText='" + submissionText + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", submittedAt=" + submittedAt +
                ", pointsEarned=" + pointsEarned +
                ", feedback='" + feedback + '\'' +
                ", gradedAt=" + gradedAt +
                '}';
    }
}
   
package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.AssignmentSubmissionDTO;
import org.example.lmsbackend.model.AssignmentSubmission;
import org.example.lmsbackend.repository.AssignmentSubmissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AssignmentSubmissionService {
    
    @Autowired
    private AssignmentSubmissionMapper submissionMapper;
    
    public boolean submitAssignment(AssignmentSubmissionDTO dto, MultipartFile file) {
        try {
            System.out.println("🔍 Submitting assignment: " + dto);
            
            // Validate required fields
            if (dto.getAssignmentId() == null || dto.getStudentId() == null) {
                System.err.println("❌ Missing required fields - AssignmentId: " + dto.getAssignmentId() + ", StudentId: " + dto.getStudentId());
                return false;
            }
            
            // Check if student already submitted
            try {
                AssignmentSubmission existing = submissionMapper.findByAssignmentAndStudent(
                    dto.getAssignmentId(), dto.getStudentId());
                if (existing != null) {
                    System.out.println("❌ Student has already submitted this assignment");
                    throw new RuntimeException("Student has already submitted this assignment");
                }
            } catch (Exception e) {
                System.err.println("❌ Error checking existing submission: " + e.getMessage());
                // Continue anyway - maybe the table doesn't exist yet
            }
            
            AssignmentSubmission submission = new AssignmentSubmission();
            submission.setAssignmentId(dto.getAssignmentId());
            submission.setStudentId(dto.getStudentId());
            submission.setSubmissionText(dto.getSubmissionText());
            submission.setSubmittedAt(LocalDateTime.now());
            
            // Handle file upload if provided
            if (file != null && !file.isEmpty()) {
                try {
                    String fileUrl = saveSubmissionFile(file);
                    submission.setFileUrl(fileUrl);
                    System.out.println("🔍 File saved: " + fileUrl);
                } catch (Exception e) {
                    System.err.println("❌ Error saving file: " + e.getMessage());
                    // Continue without file if save fails
                }
            }
            
            System.out.println("🔍 Attempting to insert submission: " + submission);
            int result = submissionMapper.insertSubmission(submission);
            System.out.println("🔍 Insert submission result: " + result);
            return result > 0;
            
        } catch (Exception e) {
            System.err.println("❌ Error submitting assignment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<AssignmentSubmission> getSubmissionsByAssignmentId(Integer assignmentId) {
        return submissionMapper.findByAssignmentId(assignmentId);
    }
    
    public List<AssignmentSubmission> getSubmissionsByStudentId(Integer studentId) {
        return submissionMapper.findByStudentId(studentId);
    }
    
    public AssignmentSubmission getSubmissionByAssignmentAndStudent(Integer assignmentId, Integer studentId) {
        return submissionMapper.findByAssignmentAndStudent(assignmentId, studentId);
    }
    
    public boolean gradeSubmission(Integer submissionId, Integer points, String feedback) {
        try {
            AssignmentSubmission submission = new AssignmentSubmission();
            submission.setSubmissionId(submissionId);
            submission.setPointsEarned(points);
            submission.setFeedback(feedback);
            submission.setGradedAt(LocalDateTime.now());
            
            return submissionMapper.gradeSubmission(submission) > 0;
        } catch (Exception e) {
            System.err.println("❌ Error grading submission: " + e.getMessage());
            return false;
        }
    }
    
    private String saveSubmissionFile(MultipartFile file) {
        try {
            String uploadDir = "uploads/assignments";
            Path uploadPath = Paths.get(uploadDir);
            
            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String originalFilename = file.getOriginalFilename();
            String cleanedFilename = originalFilename != null ? 
                originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_") : "submission.pdf";
            String filename = UUID.randomUUID() + "_" + cleanedFilename;
            
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("🔍 File saved to: " + filePath.toString());
            return filename; // Trả về chỉ tên file
        } catch (IOException e) {
            System.err.println("❌ Error saving submission file: " + e.getMessage());
            throw new RuntimeException("Error saving submission file", e);
        }
    }
}

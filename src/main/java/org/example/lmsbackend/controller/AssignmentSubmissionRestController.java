package org.example.lmsbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lmsbackend.dto.AssignmentSubmissionDTO;
import org.example.lmsbackend.model.AssignmentSubmission;
import org.example.lmsbackend.security.CustomUserDetails;
import org.example.lmsbackend.service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignment-submissions")
public class AssignmentSubmissionRestController {

    @Autowired
    private AssignmentSubmissionService submissionService;

    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('admin', 'instructor', 'student')")
    public ResponseEntity<String> submitAssignment(@RequestParam("assignmentId") Integer assignmentId,
                                                  @RequestParam(value = "submissionText", required = false) String submissionText,
                                                  @RequestPart(value = "file", required = false) MultipartFile file,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🔍 Assignment submission from user: " + userDetails.getUsername() + " (authorities: " + userDetails.getAuthorities() + ")");
            System.out.println("🔍 AssignmentId: " + assignmentId + ", SubmissionText: " + submissionText);
            
            // Only allow students to submit
            if (!userDetails.hasRole("student")) {
                return ResponseEntity.status(403).body("Chỉ sinh viên mới có thể nộp bài tập");
            }
            
            // Create DTO
            AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
            dto.setAssignmentId(assignmentId);
            dto.setStudentId(userDetails.getUserId());
            dto.setSubmissionText(submissionText);
            
            System.out.println("🔍 Created DTO: " + dto);
            
            boolean submitted = submissionService.submitAssignment(dto, file);
            if (submitted) {
                System.out.println("✅ Assignment submitted successfully");
                return ResponseEntity.ok("Assignment submitted successfully");
            } else {
                System.out.println("❌ Failed to submit assignment");
                return ResponseEntity.badRequest().body("Failed to submit assignment");
            }
        } catch (Exception e) {
            System.err.println("❌ Exception in submitAssignment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-submissions")
    @PreAuthorize("hasAnyRole('admin', 'instructor', 'student')")
    public ResponseEntity<List<AssignmentSubmission>> getMySubmissions(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🔍 Getting submissions for user: " + userDetails.getUsername() + " (ID: " + userDetails.getUserId() + ", authorities: " + userDetails.getAuthorities() + ")");
            
            // Only students can get their own submissions, but allow the endpoint for all roles
            if (!userDetails.hasRole("student")) {
                System.out.println("❌ User is not a student, returning empty list");
                return ResponseEntity.ok(List.of()); // Return empty list for non-students
            }
            
            List<AssignmentSubmission> submissions = submissionService.getSubmissionsByStudentId(userDetails.getUserId());
            System.out.println("🔍 Found " + submissions.size() + " submissions for student " + userDetails.getUserId());
            
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            System.err.println("❌ Exception in getMySubmissions: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }
    
    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<List<AssignmentSubmission>> getSubmissionsByAssignment(@PathVariable Integer assignmentId,
                                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🔍 Getting submissions for assignment: " + assignmentId + " by user: " + userDetails.getUsername());
            
            List<AssignmentSubmission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId);
            System.out.println("🔍 Found " + submissions.size() + " submissions for assignment " + assignmentId);
            
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            System.err.println("❌ Exception in getSubmissionsByAssignment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }
    
    @PostMapping("/grade/{submissionId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<String> gradeSubmission(@PathVariable Integer submissionId,
                                                 @RequestBody Map<String, Object> gradeData,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Integer points = (Integer) gradeData.get("points");
            String feedback = (String) gradeData.get("feedback");
            
            System.out.println("🔍 Grading submission " + submissionId + " with points: " + points + " and feedback: " + feedback);
            
            boolean graded = submissionService.gradeSubmission(submissionId, points, feedback);
            if (graded) {
                return ResponseEntity.ok("Submission graded successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to grade submission");
            }
        } catch (Exception e) {
            System.err.println("❌ Exception in gradeSubmission: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
}
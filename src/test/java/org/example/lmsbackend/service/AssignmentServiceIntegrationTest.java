package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.AssignmentDTO;
import org.example.lmsbackend.dto.AssignmentSubmissionDTO;
import org.example.lmsbackend.model.Assignment;
import org.example.lmsbackend.model.AssignmentSubmission;
import org.example.lmsbackend.repository.AssignmentMapper;
import org.example.lmsbackend.repository.AssignmentSubmissionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AssignmentServiceIntegrationTest {
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private AssignmentSubmissionService submissionService;
    
    @Autowired
    private AssignmentMapper assignmentMapper;
    
    @Autowired
    private AssignmentSubmissionMapper submissionMapper;
    
    private Integer testAssignmentId;
    private Integer testContentId = 1; // Assuming content with ID 1 exists
    
    @BeforeEach
    void setup() {
        // Create test assignment
        Assignment assignment = new Assignment();
        assignment.setContentId(testContentId);
        assignment.setTitle("Integration Test Assignment");
        assignment.setDescription("Test assignment for integration testing");
        assignment.setDueDate(LocalDateTime.now().plusDays(7));
        assignment.setMaxPoints(100);
        assignment.setCreatedAt(LocalDateTime.now());
        
        assignmentMapper.insertAssignment(assignment);
        testAssignmentId = assignment.getAssignmentId();
        
        assertNotNull(testAssignmentId);
    }
    
    @Test
    void createAssignment_Success() {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setContentId(testContentId);
        dto.setTitle("New Test Assignment");
        dto.setDescription("New assignment for testing");
        dto.setDueDate(LocalDateTime.now().plusDays(5));
        dto.setMaxPoints(80);
        
        boolean created = assignmentService.createAssignment(dto);
        assertTrue(created);
        
        List<Assignment> assignments = assignmentService.getAssignmentsByContentId(testContentId);
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("New Test Assignment")));
    }
    
    @Test
    void getAssignmentById_ReturnsCorrectData() {
        Assignment assignment = assignmentService.getAssignmentById(testAssignmentId);
        assertNotNull(assignment);
        assertEquals("Integration Test Assignment", assignment.getTitle());
        assertEquals(testContentId, assignment.getContentId());
    }
    
    @Test
    void updateAssignment_Success() {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setAssignmentId(testAssignmentId);
        dto.setTitle("Updated Assignment Title");
        dto.setDescription("Updated description");
        dto.setDueDate(LocalDateTime.now().plusDays(10));
        dto.setMaxPoints(120);
        
        boolean updated = assignmentService.updateAssignment(dto);
        assertTrue(updated);
        
        Assignment updatedAssignment = assignmentService.getAssignmentById(testAssignmentId);
        assertEquals("Updated Assignment Title", updatedAssignment.getTitle());
        assertEquals(120, updatedAssignment.getMaxPoints());
    }
    
    @Test
    void deleteAssignment_Success() {
        boolean deleted = assignmentService.deleteAssignment(testAssignmentId);
        assertTrue(deleted);
        
        Assignment deletedAssignment = assignmentService.getAssignmentById(testAssignmentId);
        assertNull(deletedAssignment);
    }
    
    @Test
    void submitAssignment_Success() {
        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
        dto.setAssignmentId(testAssignmentId);
        dto.setStudentId(1); // Assuming student with ID 1 exists
        dto.setSubmissionText("This is my assignment submission");
        
        boolean submitted = submissionService.submitAssignment(dto, null);
        assertTrue(submitted);
        
        List<AssignmentSubmission> submissions = submissionService.getSubmissionsByAssignmentId(testAssignmentId);
        assertFalse(submissions.isEmpty());
        assertEquals("This is my assignment submission", submissions.get(0).getSubmissionText());
    }
    
    @Test
    void submitAssignment_DuplicateSubmission_ShouldFail() {
        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
        dto.setAssignmentId(testAssignmentId);
        dto.setStudentId(1);
        dto.setSubmissionText("First submission");
        
        // First submission should succeed
        assertTrue(submissionService.submitAssignment(dto, null));
        
        // Second submission should fail
        dto.setSubmissionText("Second submission");
        assertFalse(submissionService.submitAssignment(dto, null));
    }
    
    @Test
    void gradeSubmission_Success() {
        // First create a submission
        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
        dto.setAssignmentId(testAssignmentId);
        dto.setStudentId(1);
        dto.setSubmissionText("Submission to be graded");
        
        submissionService.submitAssignment(dto, null);
        
        // Get the submission
        AssignmentSubmission submission = submissionService.getSubmissionByAssignmentAndStudent(testAssignmentId, 1);
        assertNotNull(submission);
        
        // Grade the submission
        boolean graded = submissionService.gradeSubmission(submission.getSubmissionId(), 85, "Good work!");
        assertTrue(graded);
        
        // Verify grading
        AssignmentSubmission gradedSubmission = submissionService.getSubmissionByAssignmentAndStudent(testAssignmentId, 1);
        assertEquals(85, gradedSubmission.getPointsEarned());
        assertEquals("Good work!", gradedSubmission.getFeedback());
        assertNotNull(gradedSubmission.getGradedAt());
    }
    
    @Test
    void getSubmissionsByStudentId_ReturnsCorrectData() {
        // Create multiple submissions for different assignments
        AssignmentSubmissionDTO dto1 = new AssignmentSubmissionDTO();
        dto1.setAssignmentId(testAssignmentId);
        dto1.setStudentId(1);
        dto1.setSubmissionText("First submission");
        
        submissionService.submitAssignment(dto1, null);
        
        List<AssignmentSubmission> submissions = submissionService.getSubmissionsByStudentId(1);
        assertFalse(submissions.isEmpty());
        assertTrue(submissions.stream().anyMatch(s -> s.getSubmissionText().equals("First submission")));
    }
}

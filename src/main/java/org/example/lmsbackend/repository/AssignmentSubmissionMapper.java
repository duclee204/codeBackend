package org.example.lmsbackend.repository;

import org.apache.ibatis.annotations.*;
import org.example.lmsbackend.model.AssignmentSubmission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface AssignmentSubmissionMapper {
    
    @Insert("""
        INSERT INTO assignment_submissions (assignment_id, student_id, submission_text, file_url, submitted_at)
        VALUES (#{assignmentId}, #{studentId}, #{submissionText}, #{fileUrl}, #{submittedAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "submissionId")
    int insertSubmission(AssignmentSubmission submission);
    
    @Select("""
        SELECT submission_id, assignment_id, student_id, submission_text, file_url, 
               submitted_at, points_earned, feedback, graded_at
        FROM assignment_submissions WHERE assignment_id = #{assignmentId}
    """)
    List<AssignmentSubmission> findByAssignmentId(@Param("assignmentId") Integer assignmentId);
    
    @Select("""
        SELECT submission_id, assignment_id, student_id, submission_text, file_url, 
               submitted_at, points_earned, feedback, graded_at
        FROM assignment_submissions WHERE student_id = #{studentId}
    """)
    List<AssignmentSubmission> findByStudentId(@Param("studentId") Integer studentId);
    
    @Select("""
        SELECT submission_id, assignment_id, student_id, submission_text, file_url, 
               submitted_at, points_earned, feedback, graded_at
        FROM assignment_submissions WHERE assignment_id = #{assignmentId} AND student_id = #{studentId}
    """)
    AssignmentSubmission findByAssignmentAndStudent(@Param("assignmentId") Integer assignmentId, 
                                                   @Param("studentId") Integer studentId);
    
    @Update("""
        UPDATE assignment_submissions SET points_earned = #{pointsEarned}, 
        feedback = #{feedback}, graded_at = #{gradedAt}
        WHERE submission_id = #{submissionId}
    """)
    int gradeSubmission(AssignmentSubmission submission);
    
    @Delete("DELETE FROM assignment_submissions WHERE submission_id = #{submissionId}")
    int deleteSubmission(@Param("submissionId") Integer submissionId);
}

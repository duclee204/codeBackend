package org.example.lmsbackend.repository;

import org.apache.ibatis.annotations.*;
import org.example.lmsbackend.model.Assignment;
import java.util.List;

@Mapper
public interface AssignmentMapper {
    
    @Insert("""
        INSERT INTO assignments (content_id, title, description, due_date, max_points, created_at)
        VALUES (#{contentId}, #{title}, #{description}, #{dueDate}, #{maxPoints}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "assignmentId")
    int insertAssignment(Assignment assignment);
    
    @Select("""
        SELECT assignment_id, content_id, title, description, due_date, max_points, created_at
        FROM assignments WHERE content_id = #{contentId}
    """)
    List<Assignment> findByContentId(@Param("contentId") Integer contentId);
    
    @Select("""
        SELECT assignment_id, content_id, title, description, due_date, max_points, created_at
        FROM assignments WHERE assignment_id = #{assignmentId}
    """)
    Assignment findById(@Param("assignmentId") Integer assignmentId);
    
    @Update("""
        UPDATE assignments SET title = #{title}, description = #{description}, 
        due_date = #{dueDate}, max_points = #{maxPoints}
        WHERE assignment_id = #{assignmentId}
    """)
    int updateAssignment(Assignment assignment);
    
    @Delete("DELETE FROM assignments WHERE assignment_id = #{assignmentId}")
    int deleteAssignment(@Param("assignmentId") Integer assignmentId);
}

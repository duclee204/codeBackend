package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.AssignmentDTO;
import org.example.lmsbackend.model.Assignment;
import org.example.lmsbackend.repository.AssignmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {
    
    @Autowired
    private AssignmentMapper assignmentMapper;
    
    public boolean createAssignment(AssignmentDTO dto) {
        try {
            System.out.println("🔍 Creating assignment with DTO: " + dto);
            
            Assignment assignment = new Assignment();
            assignment.setContentId(dto.getContentId());
            assignment.setTitle(dto.getTitle());
            assignment.setDescription(dto.getDescription());
            assignment.setDueDate(dto.getDueDate());
            assignment.setMaxPoints(dto.getMaxPoints() != null ? dto.getMaxPoints() : 100);
            
            System.out.println("🔍 Assignment object: " + assignment);
            
            int result = assignmentMapper.insertAssignment(assignment);
            System.out.println("🔍 Insert result: " + result);
            
            return result > 0;
        } catch (Exception e) {
            System.err.println("❌ Error creating assignment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Assignment> getAssignmentsByContentId(Integer contentId) {
        return assignmentMapper.findByContentId(contentId);
    }
    
    public Assignment getAssignmentById(Integer assignmentId) {
        try {
            return assignmentMapper.findById(assignmentId);
        } catch (Exception e) {
            System.err.println("❌ Error getting assignment by ID " + assignmentId + ": " + e.getMessage());
            return null;
        }
    }
    
    public boolean updateAssignment(AssignmentDTO dto) {
        try {
            Assignment assignment = new Assignment();
            assignment.setAssignmentId(dto.getAssignmentId());
            assignment.setTitle(dto.getTitle());
            assignment.setDescription(dto.getDescription());
            assignment.setDueDate(dto.getDueDate());
            assignment.setMaxPoints(dto.getMaxPoints());
            
            return assignmentMapper.updateAssignment(assignment) > 0;
        } catch (Exception e) {
            System.err.println("❌ Error updating assignment: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteAssignment(Integer assignmentId) {
        try {
            return assignmentMapper.deleteAssignment(assignmentId) > 0;
        } catch (Exception e) {
            System.err.println("❌ Error deleting assignment: " + e.getMessage());
            return false;
        }
    }
}

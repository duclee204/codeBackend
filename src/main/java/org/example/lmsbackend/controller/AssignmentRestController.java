package org.example.lmsbackend.controller;

import org.example.lmsbackend.dto.AssignmentDTO;
import org.example.lmsbackend.model.Assignment;
import org.example.lmsbackend.security.CustomUserDetails;
import org.example.lmsbackend.service.AssignmentService;
import org.example.lmsbackend.service.ContentsService;
import org.example.lmsbackend.service.CourseService;
import org.example.lmsbackend.service.EnrollmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentRestController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private ContentsService contentsService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private EnrollmentsService enrollmentsService;
    
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<String> createAssignment(@RequestBody AssignmentDTO dto,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🔍 Assignment creation request from user: " + userDetails.getUsername() + " (role: " + userDetails.getAuthorities() + ")");
            System.out.println("🔍 Assignment DTO: " + dto);
            
            if (dto.getContentId() == null) {
                System.out.println("❌ ContentId is null");
                return ResponseEntity.badRequest().body("ContentId không được để trống");
            }
            
            if (userDetails.hasRole("instructor")) {
                Integer courseId = contentsService.getCourseIdByContentId(dto.getContentId());
                System.out.println("🔍 CourseId for contentId " + dto.getContentId() + ": " + courseId);
                
                if (courseId == null) {
                    System.out.println("❌ CourseId not found for contentId: " + dto.getContentId());
                    return ResponseEntity.badRequest().body("Nội dung không tồn tại hoặc không thuộc khóa học nào");
                }
                
                boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
                System.out.println("🔍 Is instructor " + userDetails.getUserId() + " owner of course " + courseId + ": " + isOwner);
                
                if (!isOwner) {
                    System.out.println("❌ Instructor does not own course");
                    return ResponseEntity.status(403).body("Bạn không có quyền tạo bài tập cho nội dung này");
                }
            }
            
            boolean created = assignmentService.createAssignment(dto);
            if (created) {
                System.out.println("✅ Assignment created successfully");
                return ResponseEntity.ok("Assignment created successfully");
            } else {
                System.out.println("❌ Failed to create assignment");
                return ResponseEntity.badRequest().body("Failed to create assignment");
            }
        } catch (Exception e) {
            System.err.println("❌ Exception in createAssignment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
    
    @GetMapping("/content/{contentId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor', 'student')")
    public ResponseEntity<?> getAssignmentsByContent(@PathVariable Integer contentId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.hasRole("student")) {
            Integer courseId = contentsService.getCourseIdByContentId(contentId);
            boolean isEnrolled = enrollmentsService.isStudentEnrolled(userDetails.getUserId(), courseId);
            if (!isEnrolled) {
                return ResponseEntity.status(403).body("Bạn chưa đăng ký khóa học này");
            }
        }
        
        if (userDetails.hasRole("instructor")) {
            Integer courseId = contentsService.getCourseIdByContentId(contentId);
            boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
            if (!isOwner) {
                return ResponseEntity.status(403).body("Bạn không có quyền xem bài tập này");
            }
        }
        
        List<Assignment> assignments = assignmentService.getAssignmentsByContentId(contentId);
        return ResponseEntity.ok(assignments);
    }
    
    @PutMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<String> updateAssignment(@PathVariable Integer assignmentId,
                                                  @RequestBody AssignmentDTO dto,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        dto.setAssignmentId(assignmentId);
        
        if (userDetails.hasRole("instructor")) {
            Integer courseId = contentsService.getCourseIdByContentId(dto.getContentId());
            boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
            if (!isOwner) {
                return ResponseEntity.status(403).body("Bạn không có quyền sửa bài tập này");
            }
        }
        
        boolean updated = assignmentService.updateAssignment(dto);
        if (updated) {
            return ResponseEntity.ok("Assignment updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update assignment");
        }
    }
    
    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<String> deleteAssignment(@PathVariable Integer assignmentId,
                                                  @RequestParam Integer contentId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.hasRole("instructor")) {
            Integer courseId = contentsService.getCourseIdByContentId(contentId);
            boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
            if (!isOwner) {
                return ResponseEntity.status(403).body("Bạn không có quyền xóa bài tập này");
            }
        }
        
        boolean deleted = assignmentService.deleteAssignment(assignmentId);
        if (deleted) {
            return ResponseEntity.ok("Assignment deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete assignment");
        }
    }
    
    // Temporary endpoint for testing - creates assignment with courseId directly
    @PostMapping("/create-by-course")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<String> createAssignmentByCourse(@RequestBody AssignmentDTO dto,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🔍 Creating assignment by course. DTO: " + dto);
            
            // Validate required fields
            if (dto.getCourseId() == null) {
                System.out.println("❌ CourseId is null");
                return ResponseEntity.badRequest().body("CourseId không được để trống");
            }
            
            // Set contentId to courseId for now (temporary solution)
            dto.setContentId(dto.getCourseId());
            
            if (userDetails.hasRole("instructor")) {
                boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), dto.getCourseId());
                System.out.println("🔍 Is instructor " + userDetails.getUserId() + " owner of course " + dto.getCourseId() + ": " + isOwner);
                
                if (!isOwner) {
                    return ResponseEntity.status(403).body("Bạn không có quyền tạo bài tập cho khóa học này");
                }
            }
            
            boolean created = assignmentService.createAssignment(dto);
            if (created) {
                return ResponseEntity.ok("Assignment created successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to create assignment");
            }
        } catch (Exception e) {
            System.err.println("❌ Exception in createAssignmentByCourse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor', 'student')")
    public ResponseEntity<?> getAssignmentsByCourse(@PathVariable Integer courseId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🔍 Getting assignments for courseId: " + courseId + " by user: " + userDetails.getUsername() + " (role: " + userDetails.getAuthorities() + ")");
            
            if (userDetails.hasRole("student")) {
                boolean isEnrolled = enrollmentsService.isStudentEnrolled(userDetails.getUserId(), courseId);
                System.out.println("🔍 Is student " + userDetails.getUserId() + " enrolled in course " + courseId + ": " + isEnrolled);
                
                if (!isEnrolled) {
                    return ResponseEntity.status(403).body("Bạn chưa đăng ký khóa học này");
                }
            }
            
            if (userDetails.hasRole("instructor")) {
                boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
                System.out.println("🔍 Is instructor " + userDetails.getUserId() + " owner of course " + courseId + ": " + isOwner);
                
                if (!isOwner) {
                    return ResponseEntity.status(403).body("Bạn không có quyền xem bài tập này");
                }
            }
            
            // Get assignments where contentId equals courseId (temporary solution)
            List<Assignment> assignments = assignmentService.getAssignmentsByContentId(courseId);
            System.out.println("🔍 Found " + assignments.size() + " assignments for course " + courseId);
            return ResponseEntity.ok(assignments);
            
        } catch (Exception e) {
            System.err.println("❌ Exception in getAssignmentsByCourse: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
}

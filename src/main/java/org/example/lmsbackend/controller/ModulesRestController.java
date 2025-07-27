package org.example.lmsbackend.controller;

import org.example.lmsbackend.service.CourseService;
import org.example.lmsbackend.model.Enrollment;
import org.example.lmsbackend.service.EnrollmentsService;
import org.example.lmsbackend.dto.ModulesDTO;
import org.example.lmsbackend.service.ModulesService;
import org.example.lmsbackend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true") // Thêm CORS
public class ModulesRestController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private EnrollmentsService enrollmentsService;

    @Autowired
    private ModulesService moduleService;

    @PostMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<?> createModule(@PathVariable int courseId,
                                          @RequestBody ModulesDTO request,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            System.out.println("🚀 CREATE MODULE Request received:");
            System.out.println("   CourseId: " + courseId);
            System.out.println("   Request body: " + request);
            System.out.println("   User: " + userDetails.getUsername());
            System.out.println("   Role: " + userDetails.getAuthorities());
            
            // Gán courseId từ URL vào request
            request.setCourseId(courseId);

            // Kiểm tra instructor có dạy khóa này không
            if (userDetails.hasRole("instructor")) {
                boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
                if (!isOwner) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Bạn không có quyền tạo module cho khóa học này");
                    return ResponseEntity.status(403).body(errorResponse);
                }
            }

            boolean created = moduleService.createModule(request);
            if (created) {
                System.out.println("✅ Module created successfully, returning response");
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "Module created successfully");
                return ResponseEntity.ok(successResponse);
            } else {
                System.err.println("❌ Module creation failed in service");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Không thể tạo module");
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (Exception e) {
            System.err.println("❌ Error creating module: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Lỗi tạo module: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @GetMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor', 'student')")
    public ResponseEntity<?> getModulesByCourse(@PathVariable int courseId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            int userId = userDetails.getUserId();

            if (userDetails.hasRole("instructor")) {
                boolean isOwner = courseService.isInstructorOfCourse(userId, courseId);
                if (!isOwner) {
                    return ResponseEntity.status(403).body("Bạn không dạy khóa học này");
                }
            }

            if (userDetails.hasRole("student")) {
                boolean isEnrolled = enrollmentsService.isStudentEnrolled(userId, courseId);
                if (!isEnrolled) {
                    return ResponseEntity.status(403).body("Bạn chưa đăng ký khóa học này");
                }
            }

            return ResponseEntity.ok(moduleService.getModulesByCourseId(courseId));
        } catch (Exception e) {
            System.err.println("❌ Error getting modules: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi lấy danh sách module: " + e.getMessage());
        }
    }
    @PutMapping("/{moduleId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<String> updateModule(@PathVariable int moduleId,
                                               @RequestBody ModulesDTO request,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            request.setModuleId(moduleId); // Gán moduleId vào DTO
            int courseId = request.getCourseId(); // Đảm bảo frontend gửi courseId

            // Instructor chỉ được sửa module của khóa học mình dạy
            if (userDetails.hasRole("instructor")) {
                boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
                if (!isOwner) {
                    return ResponseEntity.status(403).body("Bạn không có quyền sửa module này");
                }
            }

            boolean updated = moduleService.updateModule(request);
            if (updated) {
                return ResponseEntity.ok("Module updated successfully");
            } else {
                return ResponseEntity.status(404).body("Module không tìm thấy hoặc không thể cập nhật");
            }
        } catch (Exception e) {
            System.err.println("❌ Error updating module: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi cập nhật module: " + e.getMessage());
        }
    }
    @DeleteMapping("/{moduleId}")
    @PreAuthorize("hasAnyRole('admin', 'instructor')")
    public ResponseEntity<String> deleteModule(@PathVariable int moduleId,
                                               @RequestParam int courseId, // gửi kèm courseId để check quyền
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            if (userDetails.hasRole("instructor")) {
                boolean isOwner = courseService.isInstructorOfCourse(userDetails.getUserId(), courseId);
                if (!isOwner) {
                    return ResponseEntity.status(403).body("Bạn không có quyền xóa module này");
                }
            }

            boolean deleted = moduleService.deleteModule(moduleId);
            if (deleted) {
                return ResponseEntity.ok("Module deleted successfully");
            } else {
                return ResponseEntity.status(404).body("Module không tìm thấy hoặc không thể xóa");
            }
        } catch (Exception e) {
            System.err.println("❌ Error deleting module: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi xóa module: " + e.getMessage());
        }
    }


}

package org.example.lmsbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lmsbackend.dto.EnrollmentRequest;
import org.example.lmsbackend.dto.EnrollmentsDTO;
import org.example.lmsbackend.dto.UserDTO;
import org.example.lmsbackend.security.CustomUserDetails;
import org.example.lmsbackend.service.CourseService;
import org.example.lmsbackend.service.EnrollmentsService;
import org.example.lmsbackend.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(EnrollmentsRestController.class)
class EnrollmentsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private EnrollmentsService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private CustomUserDetails createUser(int id, String role) {
        return new CustomUserDetails(
                id,
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }

    @Test
    void registerCourse_Success() throws Exception {
        EnrollmentRequest req = new EnrollmentRequest();
        req.setCourseId(1);

        when(enrollmentService.enrollUserInCourse(anyInt(), anyInt())).thenReturn(true);

        mockMvc.perform(post("/api/enrollments/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(10, "STUDENT"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Đăng ký thành công"));
    }

    @Test
    void registerCourse_AlreadyRegistered() throws Exception {
        EnrollmentRequest req = new EnrollmentRequest();
        req.setCourseId(1);

        when(enrollmentService.enrollUserInCourse(anyInt(), anyInt())).thenReturn(false);

        mockMvc.perform(post("/api/enrollments/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(10, "STUDENT"))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Người dùng đã đăng ký khóa học này rồi"));
    }

    @Test
    void getMyCourses_ReturnsList() throws Exception {
        when(enrollmentService.getEnrolledCourses(anyInt()))
                .thenReturn(List.of(new EnrollmentsDTO(), new EnrollmentsDTO()));

        mockMvc.perform(get("/api/enrollments/my-courses")
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(10, "STUDENT"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void unenrollCourse_Success() throws Exception {
        when(enrollmentService.deleteEnrollment(anyInt(), anyInt())).thenReturn(1);

        mockMvc.perform(delete("/api/enrollments/unenroll")
                        .param("courseId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(10, "STUDENT"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Unenroll successful"));
    }

    @Test
    void unenrollCourse_NotFound() throws Exception {
        when(enrollmentService.deleteEnrollment(anyInt(), anyInt())).thenReturn(0);

        mockMvc.perform(delete("/api/enrollments/unenroll")
                        .param("courseId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(10, "STUDENT"))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Enrollment not found"));
    }

    @Test
    void getEnrollmentsByCourse_Success() throws Exception {
        when(courseService.isInstructorOfCourse(10, 1)).thenReturn(true);
        when(enrollmentService.getEnrolledUsersByCourse(1)).thenReturn(Collections.singletonList(new UserDTO()));

        mockMvc.perform(get("/api/enrollments/course/1/enrollments")
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(10, "INSTRUCTOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void adminUnenrollUserFromCourse_Success() throws Exception {
        when(enrollmentService.deleteEnrollment(10, 1)).thenReturn(1);

        mockMvc.perform(delete("/api/enrollments/admin/unenroll")
                        .param("userId", "10")
                        .param("courseId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(999, "ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin unenroll successful"));
    }

    @Test
    void adminUnenrollUserFromCourse_NotFound() throws Exception {
        when(enrollmentService.deleteEnrollment(10, 1)).thenReturn(0);

        mockMvc.perform(delete("/api/enrollments/admin/unenroll")
                        .param("userId", "10")
                        .param("courseId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(createUser(999, "ADMIN"))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Enrollment not found"));
    }
}

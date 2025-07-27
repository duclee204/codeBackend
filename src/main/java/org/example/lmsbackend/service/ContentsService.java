package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.ContentsDTO;
import org.example.lmsbackend.repository.ContentsMapper;
import org.example.lmsbackend.repository.ModulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentsService {

    @Autowired
    private ContentsMapper contentsMapper;

    @Autowired
    private ModulesMapper modulesMapper;

    public void createContent(ContentsDTO content) {
        contentsMapper.insertContent(content);
    }

    public List<ContentsDTO> getContentsByCourseId(int courseId) {
        return contentsMapper.getContentsByCourseId(courseId);
    }

    public void updateContent(ContentsDTO content) {
        contentsMapper.updateContent(content);
    }

    public void deleteContent(int contentId) {
        contentsMapper.deleteContent(contentId);
    }

    public Integer getModuleIdByContentId(int contentId) {
        return contentsMapper.getModuleIdByContentId(contentId);
    }

    public Integer getCourseIdByContentId(int contentId) {
        Integer moduleId = contentsMapper.getModuleIdByContentId(contentId);
        if (moduleId == null) return null;
        return modulesMapper.getCourseIdByModuleId(moduleId);
    }

    public Integer getCourseIdByContentId(Integer contentId) {
        try {
            System.out.println("🔍 Getting courseId for contentId: " + contentId);
            Integer courseId = contentsMapper.getCourseIdByContentId(contentId);
            System.out.println("🔍 Found courseId: " + courseId);
            return courseId;
        } catch (Exception e) {
            System.err.println("❌ Error getting courseId for contentId " + contentId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Thêm phương thức này để lưu file PDF
//    public String saveFile(MultipartFile file) {
//        String uploadDir = "uploads/";
//        try {
//            // ✅ Tạo tên file an toàn và không trùng lặp
//            String originalName = file.getOriginalFilename();
//            String fileName = System.currentTimeMillis() + "_" + (originalName != null ? originalName.replaceAll("[^a-zA-Z0-9.]", "_") : "file.pdf");
//
//            // ✅ Tạo thư mục nếu chưa có
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            // ✅ Ghi file vào thư mục
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(file.getInputStream(), filePath);
//
//            // ✅ Trả về đường dẫn hoặc tên file để lưu vào DB
//            return fileName;
//
//        } catch (IOException e) {
//            throw new RuntimeException("Lỗi khi lưu file PDF: " + e.getMessage(), e);
//        }
//    }

}

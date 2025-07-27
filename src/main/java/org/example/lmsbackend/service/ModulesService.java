package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.ModulesDTO;
import org.example.lmsbackend.repository.ModulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModulesService {

    @Autowired
    private ModulesMapper modulesMapper;

    // Thêm module — dùng ContentsDTO thay vì ModuleRequest
    public boolean createModule(ModulesDTO module) {
        int result = modulesMapper.insertModule(module);
        return result > 0;
    }

    // Lấy danh sách modules theo courseId
    public List<ModulesDTO> getModulesByCourseId(int courseId) {
        return modulesMapper.getModulesByCourseId(courseId);
    }

    // Cập nhật module
    public boolean updateModule(ModulesDTO module) {
        int result = modulesMapper.updateModule(module);
        return result > 0;
    }

    // Xóa module
    public boolean deleteModule(int moduleId) {
        int result = modulesMapper.deleteModule(moduleId);
        return result > 0;
    }
    public int getCourseIdByModuleId(int moduleId) {
        return modulesMapper.getCourseIdByModuleId(moduleId);
    }



}

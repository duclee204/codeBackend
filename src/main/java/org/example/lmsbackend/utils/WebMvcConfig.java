package org.example.lmsbackend.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();

        // ✅ Truy cập ảnh
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);

        // ✅ Truy cập file CV
        registry.addResourceHandler("/cvs/**")
                .addResourceLocations(uploadPath + "cvs/");
    }
}

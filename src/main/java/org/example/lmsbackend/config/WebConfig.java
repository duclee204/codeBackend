package org.example.lmsbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handler for assignment files
        registry.addResourceHandler("/uploads/assignments/**")
                .addResourceLocations("file:uploads/assignments/");
                
        // Handler for avatar images
        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations("file:uploads/avatars/");
                
        // Handler for course images
        registry.addResourceHandler("/images/courses/**")
                .addResourceLocations("file:uploads/imagescourse/");
    }
}

package org.example.lmsbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/avatars")
    public ResponseEntity<?> listAvatars() {
        try {
            Path avatarDir = Paths.get("uploads", "avatars");
            if (!Files.exists(avatarDir)) {
                return ResponseEntity.ok(Map.of("message", "Avatar directory does not exist"));
            }
            
            var avatarFiles = Files.list(avatarDir)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("avatarDirectory", avatarDir.toAbsolutePath().toString());
            response.put("files", avatarFiles);
            response.put("count", avatarFiles.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

package vn.iot.star.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public Path getRootLocation() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) return null;

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null) {
            int index = originalName.lastIndexOf('.');
            if (index >= 0) extension = originalName.substring(index);
        }
        String fileName = System.currentTimeMillis() + extension;

        try {
            Path targetDir = getRootLocation().resolve(subDir);
            Files.createDirectories(targetDir);
            file.transferTo(targetDir.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + fileName, e);
        }
        return subDir + "/" + fileName;
    }

    public File resolve(String relativePath) {
        return getRootLocation().resolve(relativePath).toFile();
    }
}
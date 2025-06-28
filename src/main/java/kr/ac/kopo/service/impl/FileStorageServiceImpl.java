package kr.ac.kopo.service.impl;

import kr.ac.kopo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    @Override
    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path target = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    @Override
    public byte[] loadFileAsBytes(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }
}
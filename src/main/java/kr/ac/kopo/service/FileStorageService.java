// 9. File Upload Handling (Images)
package kr.ac.kopo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    byte[] loadFileAsBytes(String filename);
}

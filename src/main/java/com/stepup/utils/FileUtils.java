package com.stepup.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUtils {
    private static String UPLOADS_FOLDER = "D:\\uploads";
    public static void deleteFile(String fileName) throws Exception {
        // Đường dẫn đến thư mục chứa file
        Path uploadDir = Paths.get(UPLOADS_FOLDER + fileName);
        // Đường dẫn đầy đủ đến file cần xóa
        Path filePath = uploadDir.resolve(fileName);

        // Kiểm tra xem file tồn tại hay không
        if(Files.exists(filePath)) {
            // xóa file
            Files.delete(filePath);
        }
    }

    public static String storeFile(MultipartFile file) throws Exception {
        if(file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(fileName);
        // Thêm UUID và extension và fileName để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + System.nanoTime()+"." + extension;

        // Đường dẫn đến thưu mục muốn lưu file
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Kiểm tra và tạo thư mục nếu không tồn tại
        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
}

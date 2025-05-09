package com.stepup.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    //    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
//        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//
//        Map<String, String> result = new HashMap<>();
//        result.put("url", uploadResult.get("secure_url").toString());
//        result.put("public_id", uploadResult.get("public_id").toString());
//
//        return result;
//    }

    public Map<String, String> uploadFile(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            Map<String, String> result = new HashMap<>();
            result.put("url", uploadResult.get("secure_url").toString());
            result.put("publicId", uploadResult.get("public_id").toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể tải lên file");
        }
    }

    /**
     * Upload nhiều file cùng lúc
     * @param files Danh sách các file cần upload
     * @return Map chứa tên file gốc và URL của file sau khi upload
     */
    public List<Map<String, String>> uploadMultipleFiles(MultipartFile[] files) {
        List<Map<String, String>> uploadResults = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.emptyMap()
                    );

                    String secureUrl = (String) uploadResult.get("secure_url");
                    String actualPublicId = (String) uploadResult.get("public_id");

                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("originalFilename", file.getOriginalFilename());
                    resultMap.put("url", secureUrl);
                    resultMap.put("publicId", actualPublicId);

                    uploadResults.add(resultMap);

                } catch (IOException e) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("originalFilename", file.getOriginalFilename());
                    errorMap.put("error", "Lỗi khi upload: " + e.getMessage());
                    uploadResults.add(errorMap);
                }
            }
        }

        return uploadResults;
    }

    /**
     * Upload nhiều file đồng thời (sử dụng CompletableFuture để tăng hiệu suất)
     * Phương thức này phù hợp khi cần upload số lượng lớn file
     */
    public List<Map<String, String>> uploadMultipleFilesAsync(MultipartFile[] files) {
        List<CompletableFuture<Map<String, String>>> futures = Arrays.stream(files)
                .filter(file -> !file.isEmpty())
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                                file.getBytes(),
                                ObjectUtils.emptyMap()
                        );

                        String secureUrl = (String) uploadResult.get("secure_url");
                        String actualPublicId = (String) uploadResult.get("public_id");

                        Map<String, String> resultMap = new HashMap<>();
                        resultMap.put("originalFilename", file.getOriginalFilename());
                        resultMap.put("url", secureUrl);
                        resultMap.put("publicId", actualPublicId);

                        return resultMap;
                    } catch (IOException e) {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put("originalFilename", file.getOriginalFilename());
                        errorMap.put("error", "Lỗi khi upload: " + e.getMessage());
                        return errorMap;
                    }
                }))
                .collect(Collectors.toList());

        // Chờ tất cả các task hoàn thành
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        // Lấy kết quả của tất cả các task
        CompletableFuture<List<Map<String, String>>> allResults = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        try {
            return allResults.get(); // Đợi kết quả và trả về
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi upload nhiều file", e);
        }
    }

    public void deleteFile(String publicId) {
        try {
            // Xóa file từ Cloudinary theo publicId
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa file từ Cloudinary", e);
        }
    }
}

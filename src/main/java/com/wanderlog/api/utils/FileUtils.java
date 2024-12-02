package com.wanderlog.api.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    private static final String DEFAULT_UPLOAD_DIR = "uploads/photos"; // 기본 업로드 디렉토리

    /**
     * 파일을 서버 디렉토리에 저장하고 저장 경로를 반환합니다.
     *
     * @param file      업로드된 MultipartFile
     * @param uploadDir 파일 저장 디렉토리 (null일 경우 기본 디렉토리 사용)
     * @return 저장된 파일 경로
     * @throws IOException 파일 저장 중 오류가 발생한 경우
     */
    public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
        // 저장 디렉토리가 지정되지 않으면 기본 디렉토리 사용
        String directory = (uploadDir != null && !uploadDir.isBlank()) ? uploadDir : DEFAULT_UPLOAD_DIR;

        // 디렉토리 경로 생성
        Path uploadPath = Path.of(directory);
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 저장할 파일 이름 생성 (중복 방지를 위해 타임스탬프 사용)
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("File name is invalid");
        }
        String fileName = System.currentTimeMillis() + "_" + originalFileName;

        // 파일 저장 경로
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING); // 기존 파일 덮어쓰기
        }

        // 저장된 파일의 절대 경로 반환
        return filePath.toString();
    }
}
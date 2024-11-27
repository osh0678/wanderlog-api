package com.wanderlog.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotoResponse {
    private Long id;             // 사진 ID
    private String filePath;     // 사진 파일 경로
    private String title;        // 사진 제목
    private String description;  // 사진 설명
    private LocalDateTime takenAt; // 사진 촬영 시간
    private LocalDateTime createdAt; // 업로드 시간
    private LocalDateTime updatedAt; // 수정 시간
}
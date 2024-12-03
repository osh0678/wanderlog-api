package com.wanderlog.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TimelineResponse {
    private Long id;             // 사진 ID
    private Long albumId;        // 앨범 ID
    private String albumTitle;   // 앨범 제목
    private String albumDescription; // 앨범 설명
    private String filePath;     // 사진 파일 경로
    private String title;        // 사진 제목
    private String description;  // 사진 설명
    private List<String> tags;   // 태그 목록
    private LocalDateTime takenAt; // 사진 촬영 시간
    private LocalDateTime createdAt; // 업로드 시간
    private LocalDateTime updatedAt; // 수정 시간
}

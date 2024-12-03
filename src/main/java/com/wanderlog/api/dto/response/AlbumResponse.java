package com.wanderlog.api.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlbumResponse {
    private Long id;          // 앨범 ID
    private String title;     // 앨범 제목
    private String description; // 앨범 설명
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일
    private int photoCount;      // 연관된 사진 개수
}
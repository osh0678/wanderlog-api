package com.wanderlog.api.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class UpdatePhotoRequest {
    private String title;        // 사진 제목
    private String description;  // 사진 설명
    private List<String> tags;   // 태그 목록
}

package com.wanderlog.api.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AlbumRequest {
    private String title;
    private String description;
    private Long userId;

    private MultipartFile coverImage;
}
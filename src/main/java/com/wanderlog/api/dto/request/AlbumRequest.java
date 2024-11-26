package com.wanderlog.api.dto.request;

import lombok.Data;

@Data
public class AlbumRequest {
    private String title;
    private String description;
    private Long userId;
}
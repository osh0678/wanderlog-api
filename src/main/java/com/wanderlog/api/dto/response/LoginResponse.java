package com.wanderlog.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long userId;     // 사용자 ID
    private String message;  // 성공/실패 메시지
}
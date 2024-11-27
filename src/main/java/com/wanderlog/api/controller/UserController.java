package com.wanderlog.api.controller;

import com.wanderlog.api.dto.request.LoginRequest;
import com.wanderlog.api.dto.response.LoginResponse;
import com.wanderlog.api.entity.User;
import com.wanderlog.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    // 사용자 등록
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser); // 200 OK와 생성된 사용자 반환
    }

    // 사용자 정보 조회
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            // 성공적인 로그인 응답
            LoginResponse response = new LoginResponse(user.getId(), "로그인 성공");
            return ResponseEntity.status(200).body(response);
        } else {
            // 로그인 실패 응답
            LoginResponse response = new LoginResponse(null, "아이디 또는 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.status(401).body(response); // 401 Unauthorized
        }
    }

    // 사용자 정보 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser); // 200 OK와 업데이트된 사용자 반환
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
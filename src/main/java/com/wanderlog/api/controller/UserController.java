package com.wanderlog.api.controller;

import com.wanderlog.api.dto.request.LoginRequest;
import com.wanderlog.api.dto.request.UpdatePasswordRequest;
import com.wanderlog.api.dto.response.LoginResponse;
import com.wanderlog.api.dto.response.UserInfoResponse;
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

    // 로그인
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

    // 사용자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user != null) {
            UserInfoResponse userInfoResponse = new UserInfoResponse(user.getUsername(), user.getEmail());
            return ResponseEntity.ok(userInfoResponse); // 200 OK와 사용자 정보 반환
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // 비밀번호 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        boolean updatedUser = userService.updatePassword(id, updatePasswordRequest);

        if (updatedUser) {
            return ResponseEntity.ok().build(); // 200 OK
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
package com.wanderlog.api.controller;

import com.wanderlog.api.dto.request.LoginRequest;
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
    public ResponseEntity<User> getUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user); // 200 OK와 사용자 정보 반환
        } else {
            //message = "Invalid email or password"
            return ResponseEntity.notFound().build(); // 404 Not Found
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
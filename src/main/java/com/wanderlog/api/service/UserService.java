package com.wanderlog.api.service;

import com.wanderlog.api.entity.User;
import com.wanderlog.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 사용자 등록
    public User registerUser(User user) {
        // 비밀번호 암호화 로직 추가 필요
        return userRepository.save(user);
    }

    // 이메일로 사용자 조회
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 사용자 정보 업데이트
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // 사용자 삭제
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
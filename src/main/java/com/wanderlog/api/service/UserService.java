package com.wanderlog.api.service;

import com.wanderlog.api.dto.request.UpdatePasswordRequest;
import com.wanderlog.api.entity.User;
import com.wanderlog.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 사용자 등록
    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        //비밀번호 정책
        // 1. 8자 이상
        // 2. 영문 대소문자, 숫자, 특수문자 중 3가지 이상 조합
        // 3. 공백, 사용자 아이디 포함 X
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (!user.getPassword().matches(".*[a-zA-Z].*") || !user.getPassword().matches(".*[0-9].*") || !user.getPassword().matches(".*[!@#$%^&*()].*")) {
            throw new IllegalArgumentException("비밀번호는 영문 대소문자, 숫자, 특수문자 중 3가지 이상을 조합해야 합니다.");
        }

        return userRepository.save(user);
    }

    // 이메일과 비밀번호로 사용자 조회
    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    // 사용자 ID로 사용자 조회
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // 비밀번호 업데이트
    @Transactional
    public boolean updatePassword(Long userId, UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (!user.getPassword().equals(updatePasswordRequest.getPassword())) {
                user.setPassword(updatePasswordRequest.getPassword());
                userRepository.save(user);
                return true;
            } else {
                throw new IllegalArgumentException("기존 비밀번호랑 일치합니다.");
            }
        }
        return false;
    }

    // 사용자 삭제
    public boolean deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return false;
    }
}
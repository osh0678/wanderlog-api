package com.wanderlog.api.repository;

import com.wanderlog.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자 검색
    User findByEmail(String email);
}
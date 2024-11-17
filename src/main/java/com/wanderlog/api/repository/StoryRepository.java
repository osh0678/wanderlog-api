package com.wanderlog.api.repository;


import com.wanderlog.api.entity.Story;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
    // 특정 사용자의 스토리 목록 조회
    List<Story> findByUserId(Long userId);
}

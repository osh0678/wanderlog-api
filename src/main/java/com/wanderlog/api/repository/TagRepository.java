package com.wanderlog.api.repository;


import com.wanderlog.api.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // 태그 이름으로 검색
    Optional<Tag> findByName(String name); // 태그 이름으로 조회
}
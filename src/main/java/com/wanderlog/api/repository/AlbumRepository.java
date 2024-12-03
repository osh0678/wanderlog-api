package com.wanderlog.api.repository;

import com.wanderlog.api.entity.Album;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    // 특정 사용자의 앨범 목록 조회
     List<Album> findByUserIdOrderByCreatedAtDesc(Long userId);
}
package com.wanderlog.api.repository;

import com.wanderlog.api.entity.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // 특정 앨범의 사진 목록 조회
    List<Photo> findByAlbumId(Long albumId);

    // 특정 사용자가 업로드한 사진 목록 조회
    List<Photo> findByUserId(Long userId);
}
package com.wanderlog.api.repository;

import com.wanderlog.api.entity.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    // 특정 앨범의 사진 목록 조회
    @Query("SELECT p FROM Photo p LEFT JOIN FETCH p.tags t WHERE p.album.id = :albumId AND p.user.id = :userId")
    List<Photo> findByUserIdAndAlbumId(@Param("userId") Long userId, @Param("albumId") Long albumId);

    // 특정 사용자가 업로드한 사진 목록 조회
    List<Photo> findByUserId(Long userId);
}
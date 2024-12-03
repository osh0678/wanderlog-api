package com.wanderlog.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "PHOTOS")
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사진 고유 ID

    @Column(nullable = false, length = 255)
    private String filePath;  // 사진 파일 경로

    @Column(length = 100)
    private String title;  // 사진 제목

    @Column(columnDefinition = "TEXT")
    private String description;  // 사진 설명

    private LocalDateTime takenAt;  // 사진 촬영 시간

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 사진 업로드 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 사진 정보 수정 시간

    // 연관 관계 매핑 (사진이 속한 앨범)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    // 연관 관계 매핑 (사진 업로드한 사용자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 연관 관계 매핑 (태그와의 N:M 관계)
    @ManyToMany
    @JoinTable(
        name = "PHOTO_TAGS",
        joinColumns = @JoinColumn(name = "photo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
}
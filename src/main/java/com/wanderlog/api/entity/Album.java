package com.wanderlog.api.entity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ALBUMS")
@Data
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 앨범 고유 ID

    @Column(nullable = false, length = 100)
    private String title;  // 앨범 제목

    @Column(columnDefinition = "TEXT")
    private String description;  // 앨범 설명

    @Column(length = 255)
    private String coverImage;  // 앨범 표지 이미지 경로

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 앨범 생성 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 앨범 수정 시간

    // 연관 관계 매핑 (앨범 소유자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 연관 관계 매핑 (앨범에 속한 사진들)
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;
}
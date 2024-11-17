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
@Table(name = "STORIES")
@Data
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 스토리 고유 ID

    @Column(nullable = false, length = 100)
    private String title;  // 스토리 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;  // 스토리 내용

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 스토리 작성 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 스토리 수정 시간

    // 연관 관계 매핑 (스토리를 작성한 사용자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 연관 관계 매핑 (스토리에 포함된 사진들)
    @ManyToMany
    @JoinTable(
        name = "STORY_PHOTOS",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private List<Photo> photos;

    // 연관 관계 매핑 (태그와의 N:M 관계)
    @ManyToMany
    @JoinTable(
        name = "STORY_TAGS",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
}
package com.wanderlog.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "TAGS")
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 태그 고유 ID

    @Column(nullable = false, unique = true, length = 50)
    private String name;  // 태그 이름

    // 연관 관계 매핑 (사진과의 N:M 관계)
    @ManyToMany(mappedBy = "tags")
    private List<Photo> photos;

    // 연관 관계 매핑 (스토리와의 N:M 관계)
    @ManyToMany(mappedBy = "tags")
    private List<Story> stories;
}
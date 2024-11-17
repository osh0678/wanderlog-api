package com.wanderlog.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "LOCATIONS")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 위치 고유 ID

    @Column(length = 100)
    private String name;  // 장소 이름

    @Column(length = 255)
    private String address;  // 주소

    private Double latitude;  // 위도

    private Double longitude;  // 경도

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 위치 정보 생성 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 위치 정보 수정 시간

    // 연관 관계 매핑 (이 위치에서 촬영된 사진들)
    @OneToMany(mappedBy = "location")
    private List<Photo> photos;

    // 연관 관계 매핑 (앨범과의 N:M 관계)
    @ManyToMany(mappedBy = "locations")
    private List<Album> albums;
}
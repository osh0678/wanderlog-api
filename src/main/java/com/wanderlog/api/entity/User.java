package com.wanderlog.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USERS")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사용자 고유 ID

    @Column(nullable = false, length = 50)
    private String username;  // 사용자 이름

    @Column(nullable = false, unique = true, length = 100)
    private String email;  // 이메일 주소 (고유값)

    @Column(nullable = false, length = 255)
    private String password;  // 비밀번호 (해시된 값)

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 계정 생성 시간

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 계정 정보 수정 시간

    // 연관 관계 매핑 (User가 생성한 앨범들)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums;

    // 연관 관계 매핑 (User가 업로드한 사진들)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;
}
package com.wanderlog.api.service;

import com.wanderlog.api.dto.request.AlbumRequest;
import com.wanderlog.api.dto.response.AlbumResponse;
import com.wanderlog.api.entity.Album;
import com.wanderlog.api.entity.User;
import com.wanderlog.api.repository.AlbumRepository;
import com.wanderlog.api.repository.UserRepository;
import com.wanderlog.api.utils.FileUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AlbumService {


    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    // 앨범 생성

    public Album createAlbum(AlbumRequest albumRequest, MultipartFile coverPhoto) {

        // 유저 확인
        User user = userRepository.findById(albumRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Album album = new Album();
        album.setTitle(albumRequest.getTitle());
        album.setDescription(albumRequest.getDescription());
        album.setUser(user);
        album.setCreatedAt(LocalDateTime.now());
        album.setUpdatedAt(LocalDateTime.now());

        // 커버 사진 파일 저장
        if (coverPhoto != null && !coverPhoto.isEmpty()) {
            String filePath = saveCoverPhoto(coverPhoto);
            album.setCoverImage(filePath);
        }

        return albumRepository.save(album);
    }

        // 커버 사진 저장 메서드
    private String saveCoverPhoto(MultipartFile coverPhoto) {
        try {
            return FileUtils.saveFile(coverPhoto, "uploads/album_covers/");
        } catch (IOException e) {
            throw new RuntimeException("커버 사진 저장 중 오류 발생: " + e.getMessage());
        }
    }


    // 특정 사용자의 앨범 목록 조회
    public List<AlbumResponse> getAlbumsByUserId(Long userId) {
        List<Album> albums = albumRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // Album -> AlbumResponse로 변환
        return albums.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Album -> AlbumResponse 매핑 함수
    public AlbumResponse toResponse(Album album) {
        AlbumResponse response = new AlbumResponse();
        response.setId(album.getId());
        response.setTitle(album.getTitle());
        response.setDescription(album.getDescription());
        response.setCoverPhotoUrl(album.getCoverImage());
        response.setPhotoCount(album.getPhotos().size());
        response.setCreatedAt(album.getCreatedAt());
        response.setUpdatedAt(album.getUpdatedAt());
        return response;
    }

    // 앨범 정보 업데이트
    public Album updateAlbum(Album album) {
        return albumRepository.save(album);
    }

    // 앨범 삭제
    public void deleteAlbum(Long albumId) {
        albumRepository.deleteById(albumId);
    }
}
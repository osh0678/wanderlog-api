package com.wanderlog.api.service;

import com.wanderlog.api.dto.request.AlbumRequest;
import com.wanderlog.api.dto.response.AlbumResponse;
import com.wanderlog.api.entity.Album;
import com.wanderlog.api.entity.User;
import com.wanderlog.api.repository.AlbumRepository;
import com.wanderlog.api.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlbumService {


    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    // 앨범 생성
    public Album createAlbum(AlbumRequest albumRequest) {
        // user_id로 User 객체 조회
        User user = userRepository.findById(albumRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // Album 엔티티 생성 및 매핑
        Album album = new Album();
        album.setTitle(albumRequest.getTitle());
        album.setDescription(albumRequest.getDescription());
        album.setUser(user);

        return albumRepository.save(album);
    }

    // 특정 사용자의 앨범 목록 조회
    public List<AlbumResponse> getAlbumsByUserId(Long userId) {
        List<Album> albums = albumRepository.findByUserId(userId);

        // Album -> AlbumResponse로 변환
        return albums.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Album -> AlbumResponse 매핑 함수
    private AlbumResponse toResponse(Album album) {
        AlbumResponse response = new AlbumResponse();
        response.setId(album.getId());
        response.setTitle(album.getTitle());
        response.setDescription(album.getDescription());
        response.setCreatedAt(album.getCreatedAt());
        response.setUpdatedAt(album.getUpdatedAt());
        response.setPhotoCount(album.getPhotos() != null ? album.getPhotos().size() : 0);
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
package com.wanderlog.api.service;

import com.wanderlog.api.entity.Album;
import com.wanderlog.api.repository.AlbumRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlbumService {


    private final AlbumRepository albumRepository;

    // 앨범 생성
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    // 특정 사용자의 앨범 목록 조회
    public List<Album> getAlbumsByUserId(Long userId) {
        return albumRepository.findByUserId(userId);
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
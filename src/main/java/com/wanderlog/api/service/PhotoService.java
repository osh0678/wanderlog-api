package com.wanderlog.api.service;

import com.wanderlog.api.entity.Photo;
import com.wanderlog.api.repository.PhotoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    // 사진 업로드
    public Photo uploadPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    // 특정 앨범의 사진 목록 조회
    public List<Photo> getPhotosByAlbumId(Long albumId) {
        return photoRepository.findByAlbumId(albumId);
    }

    // 사진 정보 업데이트
    public Photo updatePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    // 사진 삭제
    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }
}
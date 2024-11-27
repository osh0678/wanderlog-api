package com.wanderlog.api.service;

import com.wanderlog.api.dto.response.PhotoResponse;
import com.wanderlog.api.entity.Album;
import com.wanderlog.api.entity.Photo;
import com.wanderlog.api.entity.Tag;
import com.wanderlog.api.entity.User;
import com.wanderlog.api.repository.AlbumRepository;
import com.wanderlog.api.repository.PhotoRepository;
import com.wanderlog.api.repository.TagRepository;
import com.wanderlog.api.repository.UserRepository;
import com.wanderlog.api.utils.FileUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PhotoService {

    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final TagRepository tagRepository;

    // 사진 업로드
    public Photo uploadPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    // 특정 앨범의 사진 목록 조회
    public List<PhotoResponse> getPhotosByUserAndAlbumId(Long userId, Long albumId) {
        List<Photo> photos = photoRepository.findByUserIdAndAlbumId(userId, albumId);

        // Photo -> PhotoResponse 변환
        return photos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PhotoResponse toResponse(Photo photo) {
        PhotoResponse response = new PhotoResponse();
        response.setId(photo.getId());
        response.setFilePath(photo.getFilePath());
        response.setTitle(photo.getTitle());
        response.setDescription(photo.getDescription());
        response.setTakenAt(photo.getTakenAt());
        response.setCreatedAt(photo.getCreatedAt());
        response.setUpdatedAt(photo.getUpdatedAt());
        return response;
    }

    public List<Photo> uploadPhotos(Long userId, Long albumId, MultipartFile[] files,
                                    List<String> titles, List<String> descriptions, List<String> takenAtList,
                                    List<List<String>> tagLists) throws IOException {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 앨범 확인
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("앨범을 찾을 수 없습니다."));

        // 앨범과 유저가 연결되어 있는지 확인
        if (!album.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 올바르지 않습니다.");
        }

        // 파일 저장 및 Photo 엔티티 생성
        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];

            // 파일 저장
            String filePath = FileUtils.saveFile(file, null); // 기본 디렉토리 사용

            // 태그 처리
            List<String> tagNames = (tagLists != null && i < tagLists.size()) ? tagLists.get(i) : new ArrayList<>();
            List<Tag> tags = processTags(tagNames);

            // Photo 엔티티 생성
            Photo photo = new Photo();
            photo.setFilePath(filePath);
            photo.setTitle((titles != null && i < titles.size()) ? titles.get(i) : file.getOriginalFilename());
            photo.setDescription((descriptions != null && i < descriptions.size()) ? descriptions.get(i) : null);
            photo.setTakenAt(
                    (takenAtList != null && i < takenAtList.size()) ? LocalDateTime.parse(takenAtList.get(i)) : null);
            photo.setCreatedAt(LocalDateTime.now());
            photo.setUpdatedAt(LocalDateTime.now());
            photo.setAlbum(album);
            photo.setUser(user);
            photo.setTags(tags); // 태그 설정

            photos.add(photo);
        }

        // 데이터베이스에 저장
        return photoRepository.saveAll(photos);
    }

    public List<Tag> processTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            // 태그 이름으로 검색하거나 새로 생성
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag); // 새 태그 저장
                    });
            tags.add(tag);
        }
        return tags;
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
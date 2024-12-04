package com.wanderlog.api.service;

import com.wanderlog.api.dto.request.UpdatePhotoRequest;
import com.wanderlog.api.dto.response.PhotoResponse;
import com.wanderlog.api.dto.response.TimelineResponse;
import com.wanderlog.api.entity.Album;
import com.wanderlog.api.entity.Photo;
import com.wanderlog.api.entity.Tag;
import com.wanderlog.api.entity.User;
import com.wanderlog.api.repository.AlbumRepository;
import com.wanderlog.api.repository.PhotoRepository;
import com.wanderlog.api.repository.TagRepository;
import com.wanderlog.api.repository.UserRepository;
import com.wanderlog.api.utils.FileUtils;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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


    // 특정 사용자의 모든 사진 조회
    public List<TimelineResponse> getPhotosByUserId(Long userId) {
        List<Photo> photos = photoRepository.findByUserId(userId);

        // Photo -> TimelineResponse 변환
        return photos.stream()
                .map(this::toTimelineResponse)
                .collect(Collectors.toList());
    }

    private TimelineResponse toTimelineResponse(Photo photo) {
        TimelineResponse response = new TimelineResponse();
        response.setId(photo.getId());
        response.setAlbumId(photo.getAlbum().getId()); // 앨범 ID 설정
        response.setAlbumTitle(photo.getAlbum().getTitle()); // 앨범 제목 설정
        response.setAlbumDescription(photo.getAlbum().getDescription()); // 앨범 설명 설정
        response.setFilePath(photo.getFilePath());
        response.setTitle(photo.getTitle());
        response.setDescription(photo.getDescription());
        response.setTags(photo.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
        response.setTakenAt(photo.getTakenAt());
        response.setCreatedAt(photo.getCreatedAt());
        response.setUpdatedAt(photo.getUpdatedAt());
        return response;
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

        List<String> tagNames = photo.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        response.setTags(tagNames);
        return response;
    }

    @Transactional
    public void uploadPhoto(Long userId, Long albumId, MultipartFile file, String title,
                            String description, String takenAt, List<String> tags) throws IOException {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 앨범 확인
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("앨범을 찾을 수 없습니다."));

        // 앨범과 유저 연결 확인
        if (!album.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 파일 저장
        String filePath = FileUtils.saveFile(file, "uploads/photos/");

        // 태그 처리
        List<Tag> tagEntities = processTags(tags);

        // 촬영 시간 처리
        LocalDateTime takenAtDateTime = null;
        if (takenAt != null) {
            try {
                // 먼저 날짜와 시간 형식을 시도
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                takenAtDateTime = LocalDateTime.parse(takenAt, dateTimeFormatter);
            } catch (DateTimeParseException e1) {
                try {
                    // 시간 정보 없이 날짜 형식을 시도
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    takenAtDateTime = LocalDate.parse(takenAt, dateFormatter).atStartOfDay();
                } catch (DateTimeParseException e2) {
                    System.err.println("Invalid date format: " + takenAt);
                }
            }
        }

        // Photo 엔티티 생성 및 저장
        Photo photo = new Photo();
        photo.setFilePath(filePath);
        photo.setTitle(title);
        photo.setDescription(description);
        photo.setTakenAt(takenAtDateTime);
        photo.setCreatedAt(LocalDateTime.now());
        photo.setUpdatedAt(LocalDateTime.now());
        photo.setAlbum(album);
        photo.setUser(user);
        photo.setTags(tagEntities);

        photoRepository.save(photo);
    }

    public List<Tag> processTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            // 특수 기호 제거 및 공백 트림
            String cleanTagName = tagName.replaceAll("[^a-zA-Z0-9가-힣\\s]", "").trim();

            // 태그 이름이 비어 있지 않으면 처리
            if (!cleanTagName.isEmpty()) {
                Tag tag = tagRepository.findByName(cleanTagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(cleanTagName);
                            return tagRepository.save(newTag); // 새 태그 저장
                        });
                tags.add(tag);
            }
        }
        return tags;
    }

    // 사진 정보 업데이트
    @Transactional
    public void updatePhoto(Long userId, Long photoId, UpdatePhotoRequest photo) {
        // 사진 확인
        Photo photoEntity = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("사진을 찾을 수 없습니다."));

        // 유저 확인
        if (!photoEntity.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 업데이트
        photoEntity.setTitle(photo.getTitle());
        photoEntity.setDescription(photo.getDescription());
        photoEntity.setTags(processTags(photo.getTags()));
        photoEntity.setUpdatedAt(LocalDateTime.now());
        photoRepository.save(photoEntity);
    }

    // 사진 삭제
    public void deletePhoto(Long userId ,Long photoId) throws IOException {
        // 사진 확인
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("사진을 찾을 수 없습니다."));

        // 유저 확인
        if (!photo.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 파일 삭제
        FileUtils.deleteFile(photo.getFilePath());

        // 사진 삭제
        photoRepository.deleteById(photoId);
    }
}
package com.wanderlog.api.controller;

import com.wanderlog.api.dto.request.AlbumRequest;
import com.wanderlog.api.dto.request.UpdatePhotoRequest;
import com.wanderlog.api.dto.response.AlbumResponse;
import com.wanderlog.api.dto.response.PhotoResponse;
import com.wanderlog.api.dto.response.TimelineResponse;
import com.wanderlog.api.entity.Album;
import com.wanderlog.api.service.AlbumService;
import com.wanderlog.api.service.PhotoService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    // 앨범 생성
    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(
            @RequestPart("album") AlbumRequest albumRequest,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverPhoto) {
        Album createdAlbum = albumService.createAlbum(albumRequest, coverPhoto);
        AlbumResponse response = albumService.toResponse(createdAlbum);
        return ResponseEntity.status(201).body(response); // 201 Created
    }

    //특정 사용자 모든 사진 조회
    @GetMapping("/timeline/{userId}")
    public ResponseEntity<List<TimelineResponse>> getPhotosByUserId(@PathVariable Long userId) {
        List<TimelineResponse> photos = photoService.getPhotosByUserId(userId);
        if (photos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(photos); // 200 OK
    }

    // 특정 사용자의 앨범 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlbumResponse>> getAlbumsByUserId(@PathVariable Long userId) {
        List<AlbumResponse> albums = albumService.getAlbumsByUserId(userId);
        if (albums.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(albums); // 200 OK
    }

    // 특정 사용자의 특정 앨범의 사진 목록 조회
    @GetMapping("/photos/{userId}/{albumId}")
    public ResponseEntity<List<PhotoResponse>> getPhotosByUserAndAlbumId(@PathVariable Long userId,
                                                                         @PathVariable Long albumId) {
        List<PhotoResponse> photos = photoService.getPhotosByUserAndAlbumId(userId, albumId);
        if (photos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(photos); // 200 OK
    }

    // 앨범에 사진 추가
    @PostMapping("/{userId}/{albumId}")
    public ResponseEntity<List<PhotoResponse>> uploadPhotos(
            @PathVariable Long userId,
            @PathVariable Long albumId,
            @RequestParam("file") MultipartFile file,                // 단일 파일
            @RequestParam("title") String title,                    // 단일 제목
            @RequestParam("description") String description,        // 단일 설명
            @RequestParam("takenAt") String takenAt,                // 단일 촬영 시간
            @RequestParam("tags") List<String> tags                // 태그 목록
    ) throws IOException {
        // 태그 데이터를 단순 List로 처리

        // 서비스 호출
        photoService.uploadPhoto(userId, albumId, file, title, description, takenAt, tags);

        // 업로드 후 사진 목록 반환
        List<PhotoResponse> photoResponseList = photoService.getPhotosByUserAndAlbumId(userId, albumId);
        return ResponseEntity.status(201).body(photoResponseList); // 201 Created
    }

    // 사진 정보 업데이트
    @PutMapping("/photos/{userId}/{photoId}")
    public ResponseEntity<String> updatePhoto(@PathVariable Long userId, @PathVariable Long photoId, @RequestBody UpdatePhotoRequest photo) {
        photoService.updatePhoto(userId, photoId, photo);

        return ResponseEntity.ok("성공"); // 200 OK
    }

    // 앨범 정보 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
        album.setId(id);
        Album updatedAlbum = albumService.updateAlbum(album);
        return ResponseEntity.ok(updatedAlbum); // 200 OK
    }

    // 앨범 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
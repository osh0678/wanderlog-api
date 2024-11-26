package com.wanderlog.api.controller;

import com.wanderlog.api.dto.request.AlbumRequest;
import com.wanderlog.api.entity.Album;
import com.wanderlog.api.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    // 앨범 생성
    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody AlbumRequest album) {
        Album createdAlbum = albumService.createAlbum(album);
        return ResponseEntity.status(201).body(createdAlbum); // 201 Created
    }

    // 특정 사용자의 앨범 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Album>> getAlbumsByUserId(@PathVariable Long userId) {
        List<Album> albums = albumService.getAlbumsByUserId(userId);
        if (albums.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(albums); // 200 OK
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
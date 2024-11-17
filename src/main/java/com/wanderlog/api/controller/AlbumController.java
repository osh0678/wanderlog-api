package com.wanderlog.api.controller;

import com.wanderlog.api.entity.Album;
import com.wanderlog.api.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    // 앨범 생성
    @PostMapping
    public Album createAlbum(@RequestBody Album album) {
        return albumService.createAlbum(album);
    }

    // 특정 사용자의 앨범 목록 조회
    @GetMapping("/user/{userId}")
    public List<Album> getAlbumsByUserId(@PathVariable Long userId) {
        return albumService.getAlbumsByUserId(userId);
    }

    // 앨범 정보 업데이트
    @PutMapping("/{id}")
    public Album updateAlbum(@PathVariable Long id, @RequestBody Album album) {
        album.setId(id);
        return albumService.updateAlbum(album);
    }

    // 앨범 삭제
    @DeleteMapping("/{id}")
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }
}
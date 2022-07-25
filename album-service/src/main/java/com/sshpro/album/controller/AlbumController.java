package com.sshpro.album.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sshpro.album.entity.Album;
import com.sshpro.album.service.AlbumService;
import com.sshpro.album.vo.ResponseTemplate;

@RestController
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping("/albums")
    public Album save(@Valid @RequestBody Album album) {
        return albumService.save(album);
    }

    @GetMapping("/albums")
    List<Album> get() {
        return albumService.get();
    }

    @GetMapping("/albums/{id}")
    ResponseTemplate getByIdWithUser(@PathVariable("id") long id) {
        return albumService.getByIdWithUser(id);
    }

    @GetMapping("/albums/userId/{userId}")
    List<Album> getByUserId(@PathVariable("userId") long id) {
        return albumService.getByUserId(id);
    }

    @DeleteMapping("/albums/{id}")
    String deleteAlbumById(@PathVariable("id") Long id) {
        albumService.deleteById(id);
        return "Deleted album: " + id;
    }

    @PutMapping("/albums/{id}")
    Album update(@PathVariable("id") Long id,
            @RequestBody Album album) {
        return albumService.update(id, album);
    }
}

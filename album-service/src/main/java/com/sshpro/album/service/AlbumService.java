package com.sshpro.album.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sshpro.album.entity.Album;
import com.sshpro.album.vo.ResponseTemplate;

@Service
public interface AlbumService {
    Album save(Album album);

    List<Album> get();

    ResponseTemplate getByIdWithUser(long id);

    void deleteById(Long id);

    Album update(Long id, Album album);

    List<Album> getByUserId(long id);

}

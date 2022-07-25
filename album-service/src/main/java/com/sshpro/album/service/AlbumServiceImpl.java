package com.sshpro.album.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sshpro.album.entity.Album;
import com.sshpro.album.repository.AlbumRepository;
import com.sshpro.album.vo.ResponseTemplate;
import com.sshpro.album.vo.User;

@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Album save(Album album) {
        return albumRepository.save(album);
    }

    @Override
    public List<Album> get() {
        return albumRepository.findAll();
    }

    @Override
    public ResponseTemplate getByIdWithUser(long id) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (!albumOptional.isPresent()) {
            throw new EntityNotFoundException("Album is not found for id: " + id);
        }
        Album album = albumOptional.get();
        User user = restTemplate.getForObject("http://USER-SERVICE/users/" + album.getUserId(), User.class);
        return new ResponseTemplate(album, user);
    }

    @Override
    public void deleteById(Long id) {
        albumRepository.deleteById(id);
    }

    @Override
    public Album update(Long id, Album album) {
        ResponseTemplate responseTemplate = getByIdWithUser(id);
        Album albumDb = responseTemplate.getAlbum();
        if (Objects.nonNull(album.getUserId())) {
            albumDb.setUserId(album.getUserId());
        }
        if (Objects.nonNull(album.getTitle()) && !album.getTitle().isEmpty()) {
            albumDb.setTitle(album.getTitle());
        }
        return albumRepository.save(albumDb);
    }

    @Override
    public List<Album> getByUserId(long id) {
        return albumRepository.findAllByUserId(id);
    }
}

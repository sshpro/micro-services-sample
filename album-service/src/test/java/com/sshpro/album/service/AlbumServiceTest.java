package com.sshpro.album.service;

import static com.sshpro.album.util.TestData.getAlbums;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.sshpro.album.entity.Album;
import com.sshpro.album.repository.AlbumRepository;
import com.sshpro.album.vo.ResponseTemplate;

@SpringBootTest
@ActiveProfiles("test")
class AlbumServiceTest {
    private final AlbumService albumService;

    @MockBean
    AlbumRepository albumRepository;

    @MockBean
    RestTemplate restTemplate;

    public AlbumServiceTest(@Autowired AlbumService service){
        this.albumService = service;
    }

    @BeforeEach
    void setUp() {
        ArrayList<Album> albums = getAlbums();
        Mockito.when(albumRepository.save(albums.get(0))).thenReturn(albums.get(0));
        Mockito.when(albumRepository.findAll()).thenReturn(albums);
        Mockito.when(albumRepository.findAllByUserId(1L)).thenReturn(albums);
        Mockito.when(albumRepository.findById(1L)).thenReturn(Optional.of(getAlbums().get(0)));
        Mockito.when(albumRepository.findById(2L)).thenAnswer((Answer<Void>) invocation -> {
            throw new EntityNotFoundException("Album not found");
        });

    }


    @Test
    public void whenValidUserId_thenAlbumsShouldFound() {
        List<Album> foundAlbums = albumService.getByUserId(1);
        assertEquals(foundAlbums.size(), 4);
    }

    @Test
    public void whenValidId_thenAlbumShouldFound() {
        ResponseTemplate found = albumService.getByIdWithUser(1);
        assertEquals(found.getAlbum(), getAlbums().get(0));
    }

    @Test
    public void whenGetAll_thenShouldReturnAllAlbum() {
        List<Album> foundAlbums = albumService.get();
        assertEquals(foundAlbums.size(), 4);
    }

    @Test
    public void whenValidSave_thenShouldReturnUpdatedAlbum() {
        Album album = albumService.save(getAlbums().get(0));
        assertEquals(album, getAlbums().get(0));
    }

    @Test
    @DisplayName("Exception thrown when album for certain id doesn't exist")
    public void whenInvalidId_thenShouldThrow() {
        assertThrows(EntityNotFoundException.class, () -> albumService.getByIdWithUser(2));
    }
}
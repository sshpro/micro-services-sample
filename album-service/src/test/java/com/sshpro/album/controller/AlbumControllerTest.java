package com.sshpro.album.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.sshpro.album.service.AlbumService;
import com.sshpro.album.util.TestData;

@WebMvcTest(AlbumController.class)
@ActiveProfiles("test")
class AlbumControllerTest {
    private final MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    public AlbumControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void saveAlbum() throws Exception {
        Mockito.when(albumService.save(TestData.getAlbum())).thenReturn(TestData.getAlbumWithId());
        mockMvc.perform(MockMvcRequestBuilders.post("/albums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestData.getAlbumJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAlbumById() throws Exception {
        Mockito.when(albumService.getByIdWithUser(1)).thenReturn(TestData.getResponseTemplate());
        mockMvc.perform(MockMvcRequestBuilders.get("/albums/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.album.title")
                        .value(TestData.getAlbum().getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.userName")
                        .value(TestData.getUser().getUserName()));
    }

}
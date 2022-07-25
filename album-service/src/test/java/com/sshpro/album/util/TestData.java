package com.sshpro.album.util;

import java.util.ArrayList;

import com.sshpro.album.entity.Album;
import com.sshpro.album.vo.ResponseTemplate;
import com.sshpro.album.vo.User;

public class TestData {
    public static ArrayList<Album> getAlbums() {
        ArrayList<Album> albums = new ArrayList<>(4);
        for (long i = 1; i < 5; i++) {
            Album album = Album.builder()
                    .id(i)
                    .userId(1L)
                    .title("Title" + i)
                    .build();
            albums.add(album);
        }
        return albums;
    }

    public static Album getAlbum() {
        return Album.builder()
                .userId(1L)
                .title("Title")
                .build();
    }

    public static Album getAlbumWithId() {
        return Album.builder()
                .id(1L)
                .userId(1L)
                .title("Title")
                .build();
    }

    public static User getUser() {
        return new User(1L, "UserName", "FirstName", "LastName", "email@email.com");
    }

    public static ResponseTemplate getResponseTemplate() {
        Album album = Album.builder()
                .id(1L)
                .userId(1L)
                .title("Title")
                .build();

        return new ResponseTemplate(album, getUser());

    }

    public static String getAlbumJson() {
        return "{\n" +
                "  \"id\": 1,\n" +
                "  \"userId\": 1,\n" +
                "  \"title\": \"Title\"\n" +
                "}\n" +
                "{\"id\":1,\"userName\":\"UserName\",\"firstName\":\"FirstName\",\"lastName\":\"LastName\",\"email\":\"email@email.com\"}";
    }

}

package com.sshpro.user.util;

import java.util.ArrayList;
import java.util.List;

import com.sshpro.user.entity.User;

public class TestData {
    public static List<User> getUsers() {
        ArrayList<User> albums = new ArrayList<>(4);
        for (long i = 1; i < 3; i++) {
            User album = User.builder()
                    .id(i)
                    .userName("UserName" + i)
                    .firstName("FirstName" + i)
                    .lastName("LastName" + i)
                    .email("email" + i + "@email.com")
                    .build();
            albums.add(album);
        }
        return albums;
    }

    public static User getUser() {
        return User.builder()
                .userName("UserName")
                .firstName("FirstName")
                .lastName("LastName")
                .email("email@email.com")
                .build();
    }

    public static User getUserWithId() {
        return User.builder()
                .id(1L)
                .userName("UserName")
                .firstName("FirstName")
                .lastName("LastName")
                .email("email@email.com")
                .build();
    }

    public static String getUserJson() {
        return "{\n" +
                "  \"id\": 1,\n" +
                "  \"userName\": \"UserName\",\n" +
                "  \"firstName\": \"FirstName\",\n" +
                "  \"lastName\": \"LastName\",\n" +
                "  \"email\": \"email@email.com\"\n" +
                "}";
    }

    public static String getUsersJson() {
        return "[{\n" +
                "  \"id\": 1,\n" +
                "  \"userName\": \"UserName1\",\n" +
                "  \"firstName\": \"FirstName1\",\n" +
                "  \"lastName\": \"LastName1\",\n" +
                "  \"email\": \"email1@email.com\"\n" +
                "}," +
                "{\n" +
                "  \"id\": 2,\n" +
                "  \"userName\": \"UserName2\",\n" +
                "  \"firstName\": \"FirstName2\",\n" +
                "  \"lastName\": \"LastName2\",\n" +
                "  \"email\": \"email2@email.com\"\n" +
                "}]";
    }

}

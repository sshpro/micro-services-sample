package com.sshpro.user.controller;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.sshpro.user.service.UserService;
import com.sshpro.user.util.TestData;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userservice;

    @Test
    public void shouldSave() throws Exception {
        Mockito.when(userservice.save(TestData.getUser()))
                .thenReturn(TestData.getUserWithId());
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestData.getUserJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGetUserById() throws Exception {
        Mockito.when(userservice.getById(1L))
                .thenReturn(TestData.getUserWithId());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName")
                        .value(TestData.getUser().getUserName()));
    }

    @Test
    public void shouldGetUserByInvalidIdThrow() throws Exception {
        Mockito.when(userservice.getById(2L))
                .thenThrow(new EntityNotFoundException());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldGetUsers() throws Exception {
        Mockito.when(userservice.get())
                .thenReturn(TestData.getUsers());
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("[0].userName")
                        .value(TestData.getUsers().get(0).getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("[1].userName")
                        .value(TestData.getUsers().get(1).getUserName()));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        String deleteMessage = "Deleted User";
        Mockito.when(userservice.deleteById(1L))
                .thenReturn(deleteMessage);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(deleteMessage));

    }

    @Test
    public void shouldUpdateUser() throws Exception {
        Mockito.when(userservice.update(1L, TestData.getUser()))
                .thenReturn(TestData.getUserWithId());

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestData.getUserJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

package com.sshpro.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sshpro.user.entity.User;
import com.sshpro.user.repository.UserRepository;
import com.sshpro.user.util.TestData;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private static List<User> testUsers = TestData.getUsers();

    @Test
    public void givenValidUser_whenSaveUser_thenShouldReturnValidUser() {
        User user = testUsers.get(0);
        when(userRepository.save(user)).thenReturn(user);
        User saved = userService.save(user);
        assertEquals(user, saved);
    }

    @Test
    public void givenValidUserId_whenGetUser_thenShouldReturnValidUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUsers.get(0)));
        User found =  userService.getById(1L);
        assertEquals(TestData.getUsers().get(0), found);
    }

    @Test
    public void givenInvalidUserId_whenGetUser_thenShouldThrow(){
        when(userRepository.findById(3L)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, ()-> userService.getById(3L));
    }

    @Test
    public void givenUsers_whenGetUsers_thenShouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(testUsers);
        List<User> fetchedUsers = userService.get();
        assertEquals(testUsers.size(), fetchedUsers.size());
        assertEquals(testUsers.get(0), fetchedUsers.get(0));
    }

    @Test
    public void givenValidUserId_whenDelete_thenShouldReturn() {
        String message = userService.deleteById(1L);
        assertEquals(message, "User deleted");
    }

    @Test 
    public void givenValidUserId_whenUpdate_thenShouldReturnUpdatedUser(){
        when(userRepository.save(testUsers.get(0))).thenReturn(testUsers.get(0));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUsers.get(0)));
        User updated = userService.update(1L, testUsers.get(0));
        assertEquals(testUsers.get(0), updated);
    }

    @Test 
    public void givenInValidUserId_whenUpdate_thenShouldThrow(){
        when(userRepository.findById(3L)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, ()-> userService.update(3L, testUsers.get(0)));
    }
}

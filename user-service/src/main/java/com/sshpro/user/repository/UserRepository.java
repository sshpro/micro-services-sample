package com.sshpro.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sshpro.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{}

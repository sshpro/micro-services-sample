package com.sshpro.cloud.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @GetMapping("/userServiceFallBack")
    public String userServiceFallBackMethod() {
        return "User Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/albumServiceFallBack")
    public String departmentServiceFallBackMethod() {
        return "Album Service is taking longer than Expected." +
                " Please try again later";
    }
}
package com.unyime.solidID.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class ProtectedTestController {

    @GetMapping(path = "/test")
    public ResponseEntity<String> apiTest(){
        return ResponseEntity.ok("This is test api");
    }
}

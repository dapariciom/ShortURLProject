package com.example.shorturl.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeatController {

    @GetMapping("/api/v1/heartbeat")
    public ResponseEntity<String> heartBeat(){

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>("UP", headers, HttpStatus.OK);
    }
}

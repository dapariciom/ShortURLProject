package com.example.demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeatController {

    @GetMapping("api/v1/heartbeat")
    public ResponseEntity<String> heartBeat(){
        String msg = "UP";
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(msg,headers, HttpStatus.OK);
    }
}

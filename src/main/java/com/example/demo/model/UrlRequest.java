package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlRequest {
    private int id;
    private String url;
    private String expirationDate;
}

package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "url")
public class UrlEntity {
    @Id
    private int id;
    @NotNull
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private Boolean deleted;
}

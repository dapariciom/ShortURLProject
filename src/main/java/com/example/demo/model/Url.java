package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "url")
//TODO: ShortenUrl Logic
public class Url {
    @Id
    private int id;
    @NotNull
    private String url;
}

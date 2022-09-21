package com.example.shorturl.model.url;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "url")
public class UrlEntity {

    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";

    @Id
    private long id;
    @NotNull
    private String originalUrl;
    private String shortUrl;
    private String completeShortUrl;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private Boolean isDeleted;
    private Boolean isExpired;

    public void checkIfHasExpired(){
        if(this.expirationDate.isBefore(LocalDateTime.now()))
            this.isExpired = true;
    }

}

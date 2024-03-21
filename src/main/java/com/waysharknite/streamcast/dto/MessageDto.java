package com.waysharknite.streamcast.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageDto {
    private UUID id;
    private String author;
    private String message;
    private String photoUrl;
    private Integer likes;
    private LocalDateTime created;
}

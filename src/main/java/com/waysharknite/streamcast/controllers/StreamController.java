package com.waysharknite.streamcast.controllers;

import com.waysharknite.streamcast.dto.MessageDto;
import com.waysharknite.streamcast.models.Message;
import com.waysharknite.streamcast.repository.MessagesRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/messages")
public class StreamController {
    private final MessagesRepository repository;
    private final SimpMessagingTemplate template;

    @MessageMapping("/message")
    public void processMessage(@Payload Message message) {
        message.setLikes(0);
        var dbMessage = this.repository.save(message);
        this.template.convertAndSend(
            "/stream/messages/" + message.getRoom(),
            this.messageToDto(dbMessage)
        );
    }

    @CrossOrigin(origins = "http://94.131.14.228:5173")
    @GetMapping()
    public List<MessageDto> getAll(
        @RequestParam final String room
//        @RequestParam final int page
    ) {
//        Pageable pageable = PageRequest.of(page, 10);
        return repository
//            .findByRoomOrderByCreatedDesc(room, pageable)
            .findByRoomOrderByCreatedDesc(room)
            .stream()
            .map(this::messageToDto)
            .toList();
    }

    @CrossOrigin(origins = "http://94.131.14.228:5173")
    @PostMapping("/{id}/like")
    public MessageDto likeMessages(@PathVariable("id") String messageId) {
        var dbMessage = this.repository.findFirstById(UUID.fromString(messageId));

        dbMessage.setLikes(dbMessage.getLikes() + 1);
        this.repository.save(dbMessage);

        this.template.convertAndSend(
            "/stream/messages/" + dbMessage.getRoom(),
            this.messageToDto(dbMessage)
        );
        return messageToDto(dbMessage);
    }

    MessageDto messageToDto(Message message) {
        return MessageDto
            .builder()
            .id(message.getId())
            .author(message.getAuthor())
            .message(message.getMessage())
            .photoUrl(message.getPhotoUrl())
            .likes(message.getLikes())
            .created(message.getCreated())
            .build();
    }
}

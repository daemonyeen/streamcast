package com.waysharknite.streamcast.repository;

import com.waysharknite.streamcast.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessagesRepository extends JpaRepository<Message, UUID> {
//    List<Message> findByRoomOrderByCreatedDesc(String room, Pageable pageable);
    List<Message> findByRoomOrderByCreatedDesc(String room);
    Message findFirstById(UUID id);
}

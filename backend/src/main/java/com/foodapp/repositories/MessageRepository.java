package com.foodapp.repositories;

import com.foodapp.domain.mongo.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByConversationIdOrderByTimestampAsc(String conversationId);

    List<Message> findByReceiverIdAndStatus(String receiverId, Message.MessageStatus status);
}


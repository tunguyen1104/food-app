package com.foodapp.repositories;

import com.foodapp.domain.mongo.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    @Query("{ 'participantIds': { $all: ?0, $size: ?1 } }")
    List<Conversation> findByParticipantIdsAllAndSize(List<String> participantIds, int size);

    List<Conversation> findAllByParticipantIds(String participantId);
}

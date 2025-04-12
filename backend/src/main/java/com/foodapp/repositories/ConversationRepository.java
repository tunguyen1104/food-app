package com.foodapp.repositories;

import com.foodapp.domain.mongo.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    @Query("{ 'participantIds': { $all: ?0 }, 'participantIds': { $size: ?1 } }")
    Optional<Conversation> findByParticipantIdsAllAndSize(List<String> participantIds, int size);

    List<Conversation> findAllByParticipantIds(String participantId);
}

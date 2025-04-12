package com.foodapp.repositories;

import com.foodapp.domain.mongo.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByTimestampDesc(String userId);
    List<Notification> findByUserIdAndReadFalseOrderByTimestampDesc(String userId);
    List<Notification> findByUserIdAndReadIsFalse(String userId);
}

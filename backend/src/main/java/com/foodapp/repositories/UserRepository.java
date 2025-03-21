package com.foodapp.repositories;

import com.foodapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.phone=:phone")
    User findUserByPhone(@Param("phone") String phone);

    boolean existsMyUserByPhone(String phone);
}


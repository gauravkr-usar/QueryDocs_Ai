package com.substring.querydocs.repository;

import com.substring.querydocs.entity.Conversation;
import com.substring.querydocs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, String>
{
    List<Conversation> findByUserOrderByUpdatedAtDesc(User user);
    Optional<Conversation> findByIdAndUser(String id, User user);
    void deleteByIdAndUser(String id, User user);
}

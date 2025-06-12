package com.yaseen.ChatHub.Repository;

import com.yaseen.ChatHub.Model.ChatMessage;
import com.yaseen.ChatHub.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Integer> {

    List<ChatMessage> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimeStampAsc(
            User sender1, User recipient1, User sender2, User recipient2
    );

    List<ChatMessage> findByRecipientAndSenderAndStatus(User currentUser, User contact, ChatMessage.MessageStatus status);
}

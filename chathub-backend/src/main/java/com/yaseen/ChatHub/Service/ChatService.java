package com.yaseen.ChatHub.Service;

import com.yaseen.ChatHub.Model.ChatMessage;
import com.yaseen.ChatHub.Model.User;
import com.yaseen.ChatHub.Repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage sendMessage(User sender,User recipient,String message){
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient);
        chatMessage.setContent(message);
        chatMessage.setStatus(ChatMessage.MessageStatus.DELIVERED);
        chatMessage.setTimeStamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getChatHistory(User currentUser,User contact){
        return chatMessageRepository.findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimeStampAsc(currentUser,contact,currentUser,contact);
    }

    public void markMsgAsRead(User currentUser,User contact){
        List<ChatMessage> unread=chatMessageRepository.findByRecipientAndSenderAndStatus(currentUser,contact, ChatMessage.MessageStatus.DELIVERED);

        for (ChatMessage message:unread){
            message.setStatus(ChatMessage.MessageStatus.READ);
        }
        chatMessageRepository.saveAll(unread);
    }

}

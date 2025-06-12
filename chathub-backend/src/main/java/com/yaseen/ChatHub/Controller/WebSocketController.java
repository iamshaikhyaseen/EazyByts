package com.yaseen.ChatHub.Controller;

import com.yaseen.ChatHub.DTO.ChatMessageDTO;
import com.yaseen.ChatHub.DTO.MsgReadDTO;
import com.yaseen.ChatHub.DTO.ReadReceipt;
import com.yaseen.ChatHub.Model.ChatMessage;
import com.yaseen.ChatHub.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO){
        ChatMessage message=chatService.sendMessage(chatMessageDTO.getSender(),chatMessageDTO.getRecipient(),chatMessageDTO.getContent());
        simpMessagingTemplate.convertAndSendToUser(message.getSender().getId().toString(),"/queue/messages",message);
        simpMessagingTemplate.convertAndSendToUser(message.getRecipient().getId().toString(),"/queue/messages",message);

    }

    @MessageMapping("/chat.read")
    public void markAsRead(@Payload MsgReadDTO msgReadDTO){
        chatService.markMsgAsRead(msgReadDTO.getCurrentUser(),msgReadDTO.getContact());
        simpMessagingTemplate.convertAndSendToUser(msgReadDTO.getContact().getId().toString(),"/queue/read-receipts",new ReadReceipt(msgReadDTO.getContact(), LocalDateTime.now()));
    }

}

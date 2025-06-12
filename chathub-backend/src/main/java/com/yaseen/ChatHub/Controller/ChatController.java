package com.yaseen.ChatHub.Controller;

import com.yaseen.ChatHub.Exception.UserException;
import com.yaseen.ChatHub.Model.ChatMessage;
import com.yaseen.ChatHub.Model.User;
import com.yaseen.ChatHub.Service.ChatService;
import com.yaseen.ChatHub.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestHeader("Authorization") String jwt, @RequestParam Integer recipientId, @RequestBody String content) throws UserException {
        User sender=userService.findUserProfileByJwt(jwt);
        User recipient=userService.findUserById(recipientId);

        ChatMessage chatMessage=chatService.sendMessage(sender,recipient,content);
        return new ResponseEntity<>(chatMessage, HttpStatus.CREATED);
    }

    @GetMapping("/history/{contactId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@RequestHeader("Authorization") String jwt,@PathVariable Integer contactId) throws UserException {

        User user=userService.findUserProfileByJwt(jwt);
        User contact=userService.findUserById(contactId);
        List<ChatMessage> history=chatService.getChatHistory(user,contact);
        return new ResponseEntity<>(history,HttpStatus.OK);

    }

    @PostMapping("/mark-read/{contactId}")
    public ResponseEntity<Void> markAsRead(@RequestHeader("Authorization") String jwt,@PathVariable Integer contactId) throws UserException {
        User user=userService.findUserProfileByJwt(jwt);
        User contact=userService.findUserById(contactId);
        chatService.markMsgAsRead(user,contact);
        return ResponseEntity.ok().build();
    }

}

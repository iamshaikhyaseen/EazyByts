package com.yaseen.ChatHub.DTO;

import com.yaseen.ChatHub.Model.User;
import lombok.Data;

@Data
public class ChatMessageDTO {

    private User sender;
    private User recipient;
    private String content;
}

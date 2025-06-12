package com.yaseen.ChatHub.DTO;

import com.yaseen.ChatHub.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReadReceipt {
    private User contact;
    private LocalDateTime readAt;
}

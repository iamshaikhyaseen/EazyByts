package com.yaseen.ChatHub.Response;

import com.yaseen.ChatHub.Domain.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String message;
    private String jwt;
    private USER_ROLE role;
}

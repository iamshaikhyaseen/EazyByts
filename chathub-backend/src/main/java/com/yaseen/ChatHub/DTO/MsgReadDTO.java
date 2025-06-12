package com.yaseen.ChatHub.DTO;

import com.yaseen.ChatHub.Model.User;
import lombok.Data;

@Data
public class MsgReadDTO {

    private User currentUser;
    private User contact;

}

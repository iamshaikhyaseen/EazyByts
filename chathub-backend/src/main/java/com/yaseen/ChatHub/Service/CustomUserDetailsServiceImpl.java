package com.yaseen.ChatHub.Service;

import com.yaseen.ChatHub.Domain.USER_ROLE;
import com.yaseen.ChatHub.Exception.UserException;
import com.yaseen.ChatHub.Model.User;
import com.yaseen.ChatHub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user= null;
        try {
            user = userService.findUserByEmail(username);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
        if (user==null) throw new UsernameNotFoundException("User not found with email: "+username);

        USER_ROLE role=user.getRole();
        if (role==null) user.setRole(USER_ROLE.ROLE_USER);

        List<GrantedAuthority> authorityList=new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),user.getPassword(),authorityList);

    }
}

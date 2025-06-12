package com.yaseen.ChatHub.Controller;

import com.yaseen.ChatHub.Config.JwtTokenProvider;
import com.yaseen.ChatHub.Domain.USER_ROLE;
import com.yaseen.ChatHub.Exception.UserException;
import com.yaseen.ChatHub.Model.User;
import com.yaseen.ChatHub.Repository.UserRepository;
import com.yaseen.ChatHub.Request.LoginRequest;
import com.yaseen.ChatHub.Response.AuthResponse;
import com.yaseen.ChatHub.Service.CustomUserDetailsServiceImpl;
import com.yaseen.ChatHub.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>createUser(@RequestBody User user) throws UserException {
        String email=user.getEmail();
        String password=user.getPassword();
        String fullName=user.getFullName();
        USER_ROLE role=USER_ROLE.ROLE_USER;

        if (userRepository.findByEmail(email)!=null) throw new UserException("Email already used");

        User createdUser=new User();
        createdUser.setFullName(fullName);
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setRole(role);
        createdUser.setOnline(true);
        User savedUser=userRepository.save(createdUser);

        List<GrantedAuthority> authorityList=new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));

        Authentication authentication=new UsernamePasswordAuthenticationToken(email,password,authorityList);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtTokenProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setRole(savedUser.getRole());
        authResponse.setMessage("Registered Successfully!");

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest){


        String email=loginRequest.getEmail();
        String password=loginRequest.getPassword();
        Authentication authentication=authenticate(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);

        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        String role=authorities.isEmpty()? null:authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(role));
        authResponse.setMessage("Logged In SuccessFull!");
        return new ResponseEntity<>(authResponse,HttpStatus.OK);

    }

    private Authentication authenticate(String username,String password){

        UserDetails userDetails= customUserDetailsService.loadUserByUsername(username);
        if (userDetails==null || !passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Username or Password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

}

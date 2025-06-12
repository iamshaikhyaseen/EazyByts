package com.yaseen.ChatHub.Controller;
import com.yaseen.ChatHub.Exception.UserException;
import com.yaseen.ChatHub.Model.User;
import com.yaseen.ChatHub.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<User>getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
        User user=userService.findUserProfileByJwt(jwt);
        user.setPassword(null);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<User>> getUserContacts(@RequestHeader("Authorization") String jwt) throws UserException {
        User user=userService.findUserProfileByJwt(jwt);
        List<User> contacts=userService.getContacts(user);
        return new ResponseEntity<>(contacts,HttpStatus.OK);
    }

    @GetMapping("/contact")
    public ResponseEntity<List<User>> searchContact(@RequestParam String query) throws UserException {
        List<User> contacts=userService.searchContact(query);
        return new ResponseEntity<>(contacts,HttpStatus.OK);
    }

    @PostMapping("/contacts")
    public ResponseEntity<User> addToContact(@RequestHeader("Authorization") String jwt,@RequestParam String email) throws UserException {
        User user=userService.findUserProfileByJwt(jwt);
        User contact=userService.findUserByEmail(email);
        userService.addToContact(user,contact);
        return new ResponseEntity<>(contact,HttpStatus.CREATED);
    }

    @DeleteMapping("/contacts/delete/{id}")
    public ResponseEntity<String> deleteContact(@RequestHeader("Authorization") String jwt,@PathVariable Integer id) throws UserException {
        User user=userService.findUserProfileByJwt(jwt);
        userService.removeContact(user,id);
        return new ResponseEntity<>("Contact deleted with id "+id,HttpStatus.OK);
    }
}

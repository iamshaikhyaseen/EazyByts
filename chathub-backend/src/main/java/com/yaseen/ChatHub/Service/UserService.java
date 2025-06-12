package com.yaseen.ChatHub.Service;

import com.yaseen.ChatHub.Config.JwtTokenProvider;
import com.yaseen.ChatHub.DTO.ContactDTO;
import com.yaseen.ChatHub.Exception.UserException;
import com.yaseen.ChatHub.Model.User;
import com.yaseen.ChatHub.Repository.UserRepository;
import com.yaseen.ChatHub.Request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    public User findUserById(Integer id)throws UserException{

            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isPresent())
                return optionalUser.get();
            else throw new UserException("User not found with id"+id);
    }

    public List<User>findAllUsers(){
        return userRepository.findAll();
    }


    public User findUserProfileByJwt(String jwt) throws UserException{
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
            return userRepository.findByEmail(email);
        }
        catch (Exception e){
            throw new UserException("User not found");
        }
    }

    public User findUserByEmail(String email) throws UserException {
        User user=userRepository.findByEmail(email);
        if (user!=null) return user;
        throw new UserException("User does not exist with email: "+email);
    }


    public User updateUser(Integer id, UpdateUserRequest updateUserRequest) throws UserException {
        User user=findUserById(id);
        user.setFullName(updateUserRequest.getFullName());
        user.setProfilePic(updateUserRequest.getProfilePic());
        return userRepository.save(user);
    }

    public User addToContact(User user, User contact) throws UserException {
        if (user.getId().equals(contact.getId())){
            throw new UserException("Cannot add yourself");
        }
        if (user.getContactList().contains(contact)){
            throw new UserException("Contact already exist");
        }

        contact.setPassword(null);
        user.getContactList().add(contact);
        return userRepository.save(user);
    }

    public List<User> getContacts(User user) throws UserException {
        if(!user.getContactList().isEmpty()){
            List<User> contactList=user.getContactList().stream().toList();
            for (User contact:contactList){
                contact.setPassword(null);
            }
            return contactList;
        }
        throw new UserException("No Contacts Found");

    }

    public List<User> getOnlineContacts(User user){

        return user.getContactList().stream().filter(User::isOnline).toList();

    }

    public void removeContact(User user,Integer id) throws UserException {
        User contact=findUserById(id);
        if (!user.getContactList().contains(contact)) {
            throw new UserException("Contact not found");
        }
        user.getContactList().remove(contact);
        userRepository.save(user);
    }

    public List<User> searchContact(String query) throws UserException {

        List<User> contacts= userRepository.findByEmailContainingOrFullNameContaining(query,query);
        for(User contact:contacts){
            contact.setPassword(null);
        }
        return contacts;
    }


    public void deleteUser(Integer id) throws UserException {
        User user=findUserById(id);
        userRepository.delete(user);
    }

}

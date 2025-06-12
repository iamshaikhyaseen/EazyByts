package com.yaseen.ChatHub.Repository;

import com.yaseen.ChatHub.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);

    List<User> findByEmailContainingOrFullNameContaining(String email,String fullName);
}

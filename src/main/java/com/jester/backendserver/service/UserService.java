package com.jester.backendserver.service;

import com.jester.backendserver.model.User;
import com.jester.backendserver.repository.UserProcedureRepository;
import com.jester.backendserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserProcedureRepository userProcedureRepository;
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("User with id '" + id + "' not found.");
        }
        return user.get();
    }

    public User getUserByUsername(String username) {
        User user;
        try {
            user = userRepository.getUserByUsername(username).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("User with username '" + username + "' not found.");
        }
        return user;
    }

    @Transactional
    public User registerUser(String username, String password) throws Exception {
        userProcedureRepository.createUser(username, password);
        Optional<User> newUser = userRepository.findByUsername(username);
        if (newUser.isPresent()) {
            return newUser.get();
        }
        else{
            throw new Exception("Username " + username + " already exists");
        }
    }
    public boolean authenticate(String username, String password) {
        return userProcedureRepository.authenticateUser(username, password);
    }

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


}

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
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserProcedureRepository userProcedureRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        return userProcedureRepository.authenticateUser(username, password);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<User> registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already in use");
        }
        userProcedureRepository.createUser(username, password);
        return userRepository.findByUsername(username);
    }
}

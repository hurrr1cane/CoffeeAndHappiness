package com.mdn.backend.service;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String name) {
        return userRepository.findByEmail(name)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + name));
    }

    public User getMyself(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));
    }
}

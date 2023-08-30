package com.mdn.backend.service;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.user.User;
import com.mdn.backend.model.user.UserEditRequest;
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

    public User editMyself(Principal principal, UserEditRequest user) {

        User myself = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));

        if (user.getFirstName() != null) myself.setFirstName(user.getFirstName());
        if (user.getLastName() != null) myself.setLastName(user.getLastName());
        if (user.getImageUrl() != null) myself.setImageUrl(user.getImageUrl());

        return userRepository.save(myself);
    }
}

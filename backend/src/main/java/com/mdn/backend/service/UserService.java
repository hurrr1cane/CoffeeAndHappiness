package com.mdn.backend.service;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
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
        List<User> users = userRepository.findAll();
        for (User user : users) {
            fetchReviewsToUser(user);
        }
        return users;
    }

    public User getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        fetchReviewsToUser(user);
        return user;
    }

    public User getUserByEmail(String name) {
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + name));
        fetchReviewsToUser(user);
        return user;
    }

    public User getMyself(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));
        fetchReviewsToUser(user);
        return user;
    }

    public User editMyself(Principal principal, UserEditRequest user) {

        User myself = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));

        if (user.getFirstName() != null) myself.setFirstName(user.getFirstName());
        if (user.getLastName() != null) myself.setLastName(user.getLastName());
        if (user.getImageUrl() != null) myself.setImageUrl(user.getImageUrl());

        return userRepository.save(myself);
    }

    private static void fetchReviewsToUser(User user) {
        for (CafeReview review : user.getCafeReviews()) {
            Integer cafeId = review.getCafe().getId();
            Integer userId = review.getUser().getId();
            review.setCafeId(cafeId);
            review.setUserId(userId);
        }
        for (FoodReview foodReview : user.getFoodReviews()) {
            Integer foodId = foodReview.getId();
            Integer userId = foodReview.getUser().getId();
            foodReview.setFoodId(foodId);
            foodReview.setUserId(userId);
        }
    }
}

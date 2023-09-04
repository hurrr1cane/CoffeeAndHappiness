package com.mdn.backend.service;

import com.mdn.backend.exception.InvalidPasswordException;
import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.review.FoodReview;
import com.mdn.backend.model.user.PasswordChangeRequest;
import com.mdn.backend.model.user.User;
import com.mdn.backend.model.user.UserEditRequest;
import com.mdn.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AmazonS3StorageService storageService;
    private final PasswordEncoder passwordEncoder;

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
        if (user.getPhoneNumber() != null) myself.setPhoneNumber(user.getPhoneNumber());

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
            Integer foodId = foodReview.getFood().getId();
            Integer userId = foodReview.getUser().getId();
            foodReview.setFoodId(foodId);
            foodReview.setUserId(userId);
        }
    }

    public User addUserImage(Principal principal, MultipartFile image) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));

        storageService.deleteImage("user", user.getId());
        String imageUrl = storageService.saveImage(image, "user", user.getId());

        user.setImageUrl(imageUrl);
        return userRepository.save(user);

    }

    public User deleteUserImage(Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));

        storageService.deleteImage("user", user.getId());

        user.setImageUrl(null);
        return userRepository.save(user);

    }

    public User changePassword(Principal principal, PasswordChangeRequest request) {

        User myself = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + principal.getName()));

        String storedHashedPassword = myself.getPassword();

        if (!passwordEncoder.matches(request.getOldPassword(), storedHashedPassword)) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        myself.setPassword(encodedPassword);

        return userRepository.save(myself);

    }
}

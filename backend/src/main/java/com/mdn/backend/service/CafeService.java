package com.mdn.backend.service;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final AmazonS3StorageService storageService;

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(?:\\+380|0)?(\\d{9})$");

    public List<Cafe> getAllCafes() {
        for (Cafe cafe : cafeRepository.findAll()) {
            fetchReviewsToCafe(cafe);
        }
        return cafeRepository.findAll();
    }

    private static void fetchReviewsToCafe(Cafe cafe) {
        for (CafeReview review : cafe.getReviews()) {
            Integer userId = review.getUser().getId();
            Integer cafeId = review.getCafe().getId();
            review.setUserId(userId);
            review.setCafeId(cafeId);
        }
    }

    public Cafe getCafeById(Integer id) {
        Cafe cafe = cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );
        for (CafeReview review : cafe.getReviews()) {
            Integer cafeId = review.getCafe().getId();
            Integer userId = review.getUser().getId();
            review.setCafeId(cafeId);
            review.setUserId(userId);
        }
        return cafe;
    }

    public Cafe addCafe(Cafe cafe) {
        formatPhoneNumber(cafe);
        return cafeRepository.save(cafe);
    }

    public Cafe editCafe(Integer id, Cafe cafe) {
        var editedCafe = cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );

        editedCafe.setImageUrl(cafe.getImageUrl());
        editedCafe.setLocationEN(cafe.getLocationEN());
        editedCafe.setLocationUA(cafe.getLocationUA());
        formatPhoneNumber(cafe);
        editedCafe.setPhoneNumber(cafe.getPhoneNumber());
        editedCafe.setLatitude(cafe.getLatitude());
        editedCafe.setLongitude(cafe.getLongitude());

        return cafeRepository.save(editedCafe);
    }

    public void deleteCafe(Integer id) {
        var cafeToDelete = cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );
        cafeRepository.delete(cafeToDelete);
    }

    public Cafe addCafeImage(Integer cafeId, MultipartFile image) {

        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + cafeId + " found")
        );

        storageService.deleteImage("cafe", cafeId);
        String imageUrl = storageService.saveImage(image, "cafe", cafeId);

        cafe.setImageUrl(imageUrl);
        return cafeRepository.save(cafe);

    }

    public Cafe deleteCafeImage(Integer cafeId) {

        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + cafeId + " found")
        );

        storageService.deleteImage("cafe", cafeId);

        cafe.setImageUrl(null);
        return cafeRepository.save(cafe);
    }

    private void formatPhoneNumber(Cafe cafe) {
        String phoneNumber = cafe.getPhoneNumber();
        if (phoneNumber != null) {
            Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
            if (matcher.matches()) {
                cafe.setPhoneNumber("+380" + matcher.group(1));
            } else {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }
}

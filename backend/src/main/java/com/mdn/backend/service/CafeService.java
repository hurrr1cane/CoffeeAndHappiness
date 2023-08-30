package com.mdn.backend.service;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final AmazonS3StorageService storageService;

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
        try {
            return cafeRepository.save(cafe);
        } catch (Exception ex) {
            throw new RuntimeException("Error while adding cafe: " + ex.getMessage(), ex);
        }
    }

    public Cafe editCafe(Integer id, Cafe cafe) {
        var editedCafe = cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );

        editedCafe.setImageUrl(cafe.getImageUrl());
        editedCafe.setLocationEN(cafe.getLocationEN());
        editedCafe.setLocationUA(cafe.getLocationUA());
        editedCafe.setPhoneNumber(cafe.getPhoneNumber());

        try {
            return cafeRepository.save(editedCafe);
        } catch (Exception ex) {
            throw new RuntimeException("Error while editing cafe: " + ex.getMessage(), ex);
        }
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

        String imageUrl = storageService.saveImage(image);

        cafe.setImageUrl(imageUrl);
        return cafeRepository.save(cafe);

    }
}

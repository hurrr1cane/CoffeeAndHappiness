package com.mdn.backend.service;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.dto.CafeDTO;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final AzureBlobStorageService azureStorageService;

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(?:\\+?380|0)(\\d{9})$");

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

    public Cafe addCafe(CafeDTO cafeDTO) {
        Cafe cafe = buildCafeFromDTO(cafeDTO);
        formatPhoneNumber(cafeDTO);
        return cafeRepository.save(cafe);
    }

    public Cafe editCafe(Integer id, CafeDTO cafeDTO) {
        var editedCafe = cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );

        editCafeWithCheckingForNull(cafeDTO, editedCafe);

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

        azureStorageService.deleteImage("cafe", cafeId);
        String imageUrl = azureStorageService.saveImage(image, "cafe", cafeId);

        cafe.setImageUrl(imageUrl);
        return cafeRepository.save(cafe);

    }

    public Cafe deleteCafeImage(Integer cafeId) {

        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + cafeId + " found")
        );

        azureStorageService.deleteImage("cafe", cafeId);

        cafe.setImageUrl(null);
        return cafeRepository.save(cafe);
    }

    private void formatPhoneNumber(CafeDTO cafeDTO) {
        String phoneNumber = cafeDTO.getPhoneNumber();
        Cafe cafe = buildCafeFromDTO(cafeDTO);
        if (phoneNumber != null) {
            Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
            if (matcher.matches()) {
                cafe.setPhoneNumber("+380" + matcher.group(1));
            } else {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }

    private static Cafe buildCafeFromDTO(CafeDTO cafeDTO) {
        return Cafe.builder()
                .locationEN(cafeDTO.getLocationEN())
                .locationUA(cafeDTO.getLocationUA())
                .latitude(cafeDTO.getLatitude())
                .longitude(cafeDTO.getLongitude())
                .imageUrl(cafeDTO.getImageUrl())
                .phoneNumber(cafeDTO.getPhoneNumber())
                .build();
    }

    private static void editCafeWithCheckingForNull(CafeDTO cafeDTO, Cafe editedCafe) {
        if (cafeDTO.getLocationEN() != null) editedCafe.setLocationEN(cafeDTO.getLocationEN());
        if (cafeDTO.getLocationUA() != null) editedCafe.setLocationUA(cafeDTO.getLocationUA());
        if (cafeDTO.getImageUrl() != null) editedCafe.setImageUrl(cafeDTO.getImageUrl());
        if (cafeDTO.getPhoneNumber() != null) editedCafe.setPhoneNumber(cafeDTO.getPhoneNumber());
        if (cafeDTO.getLatitude() != null) editedCafe.setLatitude(cafeDTO.getLatitude());
        if (cafeDTO.getLongitude() != null) editedCafe.setLongitude(cafeDTO.getLongitude());
    }
}

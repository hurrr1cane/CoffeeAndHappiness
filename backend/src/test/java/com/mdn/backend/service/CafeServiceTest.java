package com.mdn.backend.service;

import com.mdn.backend.exception.notfound.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.dto.CafeDTO;
import com.mdn.backend.model.review.CafeReview;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.CafeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.mdn.backend.service.CafeService.buildCafeFromDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CafeServiceTest {

    @Mock
    private CafeRepository cafeRepository;

    @InjectMocks
    private CafeService cafeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCafes() {

        // Mock data
        List<Cafe> cafes = new ArrayList<>();
        cafes.add(new Cafe());
        cafes.add(new Cafe());

        // Mock behavior
        when(cafeRepository.findAll()).thenReturn(cafes);

        // Test
        List<Cafe> result = cafeService.getAllCafes();

        assertEquals(cafes.size(), result.size());
    }

    @Test
    void testGetCafeById() {
        // Mock data
        Cafe cafe = new Cafe();
        cafe.setId(1);

        // Mock behavior
        when(cafeRepository.findById(1)).thenReturn(Optional.of(cafe));

        // Test
        Cafe result = cafeService.getCafeById(1);

        assertEquals(cafe, result);
    }

    @Test
    void testGetCafeByIdNotFound() {
        // Mock behavior
        when(cafeRepository.findById(1)).thenReturn(Optional.empty());

        // Test
        assertThrows(CafeNotFoundException.class, () -> cafeService.getCafeById(1));
    }

    @Test
    void testAddCafe() {
        // Mock data
        CafeDTO cafeDTO = new CafeDTO();
        cafeDTO.setLocationEN("Test Cafe");
        cafeDTO.setLocationUA("Тест Кафе");
        cafeDTO.setLatitude(50.0);
        cafeDTO.setLongitude(30.0);

        Cafe cafe = new Cafe();
        cafe.setLocationEN("Test Cafe");
        cafe.setLocationUA("Тест Кафе");
        cafe.setLatitude(50.0);
        cafe.setLongitude(30.0);

        // Mock behavior
        when(cafeRepository.save(any(Cafe.class))).thenReturn(cafe);

        // Test
        Cafe result = cafeService.addCafe(cafeDTO);

        assertEquals(cafe, result);
    }

    @Test
    void testAddCafeWithoutLocation() {
        CafeDTO cafeDTO = new CafeDTO();

        assertThrows(NullPointerException.class, () -> {
            cafeService.addCafe(cafeDTO);
        });
    }

    @Test
    void testEditCafe() {
        // Mock data
        CafeDTO cafeDTO = new CafeDTO();
        cafeDTO.setLocationEN("Updated Cafe");

        Cafe existingCafe = new Cafe();
        existingCafe.setId(1);
        existingCafe.setLocationEN("Original Cafe");

        // Mock behavior
        when(cafeRepository.findById(1)).thenReturn(Optional.of(existingCafe));
        when(cafeRepository.save(any(Cafe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test
        Cafe result = cafeService.editCafe(1, cafeDTO);

        assertEquals("Updated Cafe", result.getLocationEN());
    }

    @Test
    void testEditCafeWithCheckingForNull() {
        // Arrange
        int cafeId = 1;
        CafeDTO cafeDTO = new CafeDTO();
        Cafe editedCafe = new Cafe();
        editedCafe.setId(cafeId);

        cafeDTO.setLocationEN("New Location EN");
        cafeDTO.setLocationUA("New Location UA");
        cafeDTO.setImageUrl("https://new_image_url");
        cafeDTO.setPhoneNumber("0680200600");
        cafeDTO.setLatitude(40.0);
        cafeDTO.setLongitude(50.0);

        when(cafeRepository.findById(cafeId)).thenReturn(java.util.Optional.of(editedCafe));

        // Act
        cafeService.editCafe(cafeId, cafeDTO);

        // Assert
        assertEquals("New Location EN", editedCafe.getLocationEN());
        assertEquals("New Location UA", editedCafe.getLocationUA());
        assertEquals("https://new_image_url", editedCafe.getImageUrl());
        assertEquals("+380680200600", editedCafe.getPhoneNumber());
        assertEquals(40.0, editedCafe.getLatitude());
        assertEquals(50.0, editedCafe.getLongitude());
    }

    @Test
    void testEditCafeWithCheckingForNullWhenNullValues() {
        // Arrange
        int cafeId = 1;
        Cafe editedCafe = new Cafe();

        editedCafe.setLocationEN("Original Location EN");
        editedCafe.setLocationUA("Original Location UA");
        editedCafe.setImageUrl("https://original_image_url");
        editedCafe.setPhoneNumber("+380680200600");
        editedCafe.setLatitude(40.0);
        editedCafe.setLongitude(50.0);

        CafeDTO cafeDTO = new CafeDTO();
        editedCafe.setId(cafeId);

        when(cafeRepository.findById(cafeId)).thenReturn(java.util.Optional.of(editedCafe));


        // Act
        cafeService.editCafe(cafeId, cafeDTO);

        // Assert
        assertEquals("Original Location EN", editedCafe.getLocationEN());
        assertEquals("Original Location UA", editedCafe.getLocationUA());
        assertEquals("https://original_image_url", editedCafe.getImageUrl());
        assertEquals("+380680200600", editedCafe.getPhoneNumber());
        assertEquals(40.0, editedCafe.getLatitude());
        assertEquals(50.0, editedCafe.getLongitude());
    }

    @Test
    void testEditCafeCafeNotFound() {
        // Arrange
        int cafeId = 1;
        CafeDTO cafeDTO = new CafeDTO();

        when(cafeRepository.findById(cafeId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CafeNotFoundException.class, () -> cafeService.editCafe(cafeId, cafeDTO));
    }

    @Test
    void testDeleteCafe() {
        // Mock behavior
        when(cafeRepository.findById(1)).thenReturn(Optional.of(new Cafe()));

        // Test
        assertDoesNotThrow(() -> cafeService.deleteCafe(1));
    }

    @Test
    void testDeleteCafeNotFound() {
        // Mock behavior
        when(cafeRepository.findById(1)).thenReturn(Optional.empty());

        // Test
        assertThrows(CafeNotFoundException.class, () -> cafeService.deleteCafe(1));
    }

    @Test
    void testFormatPhoneNumberValid() {

        // Arrange
        CafeDTO cafeDTO = new CafeDTO();
        cafeDTO.setPhoneNumber("0680200600");

        // Act
        CafeService.formatPhoneNumber(cafeDTO);

        // Assert
        assertEquals("+380680200600", cafeDTO.getPhoneNumber());
    }

    @Test
    void testFormatPhoneNumberWithPlusSignValid() {
        // Arrange
        CafeDTO cafeDTO = new CafeDTO();
        cafeDTO.setPhoneNumber("+380680200600");

        // Act
        CafeService.formatPhoneNumber(cafeDTO);

        // Assert
        assertEquals("+380680200600", cafeDTO.getPhoneNumber());
    }

    @Test
    void testFormatPhoneNumberWithOutPlusSignValid() {
        // Arrange
        CafeDTO cafeDTO = new CafeDTO();
        cafeDTO.setPhoneNumber("380680200600");

        // Act
        CafeService.formatPhoneNumber(cafeDTO);

        // Assert
        assertEquals("+380680200600", cafeDTO.getPhoneNumber());
    }

    @Test
    void testFormatPhoneNumberInvalidFormat() {
        // Arrange
        CafeDTO cafeDTO = new CafeDTO();
        cafeDTO.setPhoneNumber("12345"); // Invalid format

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> CafeService.formatPhoneNumber(cafeDTO));
    }

    @Test
    void testFetchReviewsToCafeWithNullReviews() {

        // Arrange
        Cafe cafe = new Cafe();
        cafe.setReviews(null);

        // Act
        CafeService.fetchReviewsToCafe(cafe);

        // Assert
        assertNull(cafe.getReviews());
    }

    @Test
    void testFetchReviewsToCafeWithReviews() {
        // Arrange
        CafeReview review = new CafeReview();
        review.setUser(new User());
        review.setCafe(new Cafe());

        Cafe cafe = new Cafe();
        cafe.setReviews(Collections.singletonList(review));

        // Act
        CafeService.fetchReviewsToCafe(cafe);

        // Assert
        assertNotNull(cafe.getReviews());
        assertEquals(1, cafe.getReviews().size());
        assertNotNull(cafe.getReviews().get(0).getUser());
        assertNotNull(cafe.getReviews().get(0).getCafe());
    }

}
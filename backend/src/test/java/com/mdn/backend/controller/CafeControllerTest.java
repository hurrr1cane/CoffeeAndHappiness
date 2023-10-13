package com.mdn.backend.controller;

import com.mdn.backend.exception.notfound.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.dto.CafeDTO;
import com.mdn.backend.service.CafeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class CafeControllerTest {

    @Mock
    private CafeService cafeService;

    @InjectMocks
    private CafeController cafeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCafes() {
        List<Cafe> cafes = new ArrayList<>();
        cafes.add(new Cafe());
        when(cafeService.getAllCafes()).thenReturn(cafes);

        ResponseEntity<?> response = cafeController.getAllCafes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cafes, response.getBody());
    }

    @Test
    void testGetCafeById() {
        Integer cafeId = 1;
        Cafe cafe = new Cafe();
        cafe.setId(cafeId);
        when(cafeService.getCafeById(cafeId)).thenReturn(cafe);

        ResponseEntity<?> response = cafeController.getCafeById(cafeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cafe, response.getBody());
    }

    @Test
    void testGetCafeByIdNotFound() {
        Integer cafeId = 3;
        when(cafeService.getCafeById(cafeId)).thenThrow(
                new CafeNotFoundException("Cafe not found with id: " + cafeId)
        );

        ResponseEntity<?> response = cafeController.getCafeById(cafeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cafe not found with id: " + cafeId, response.getBody());
    }

    @Test
    void testAddCafe() {
        CafeDTO cafeDTO = new CafeDTO();
        Cafe cafe = new Cafe();
        when(cafeService.addCafe(cafeDTO)).thenReturn(cafe);

        ResponseEntity<?> response = cafeController.addCafe(cafeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cafe, response.getBody());
    }

    @Test
    void testAddCafeImage() {
        Integer cafeId = 1;
        Cafe cafe = new Cafe();
        cafe.setId(cafeId);
        when(cafeService.addCafeImage(cafeId, null)).thenReturn(cafe);

        ResponseEntity<?> response = cafeController.addCafeImage(cafeId, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cafe, response.getBody());
    }

    @Test
    void testAddCafeImageNotFound() {
        Integer cafeId = 3;
        when(cafeService.addCafeImage(cafeId, null)).thenThrow(
                new CafeNotFoundException("Cafe not found with id: " + cafeId)
        );

        ResponseEntity<?> response = cafeController.addCafeImage(cafeId, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cafe not found with id: " + cafeId, response.getBody());
    }

    @Test
    void testDeleteCafeImage() {
        Integer cafeId = 1;
        Cafe cafe = new Cafe();
        cafe.setId(cafeId);
        when(cafeService.deleteCafeImage(cafeId)).thenReturn(cafe);

        ResponseEntity<?> response = cafeController.deleteCafeImage(cafeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cafe, response.getBody());
    }

    @Test
    void testDeleteCafeImageNotFound() {
        Integer cafeId = 3;
        when(cafeService.deleteCafeImage(cafeId)).thenThrow(
                new CafeNotFoundException("Cafe not found with id: " + cafeId)
        );

        ResponseEntity<?> response = cafeController.deleteCafeImage(cafeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cafe not found with id: " + cafeId, response.getBody());
    }

    @Test
    void testEditCafe() {
        Integer cafeId = 1;
        CafeDTO cafeDTO = new CafeDTO();
        Cafe cafe = new Cafe();
        cafe.setId(cafeId);
        when(cafeService.editCafe(cafeId, cafeDTO)).thenReturn(cafe);

        ResponseEntity<?> response = cafeController.editCafe(cafeId, cafeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cafe, response.getBody());
    }

    @Test
    void testEditCafeNotFound() {
        Integer cafeId = 3;
        CafeDTO cafeDTO = new CafeDTO();
        when(cafeService.editCafe(cafeId, cafeDTO)).thenThrow(
                new CafeNotFoundException("Cafe not found with id: " + cafeId)
        );

        ResponseEntity<?> response = cafeController.editCafe(cafeId, cafeDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cafe not found with id: " + cafeId, response.getBody());
    }

    @Test
    void testDeleteCafe() {
        Integer cafeId = 1;
        Cafe cafe = new Cafe();
        cafe.setId(cafeId);

        ResponseEntity<?> response = cafeController.deleteCafe(cafeId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteCafeNotFound() {
        Integer cafeId = 3;

        doThrow(new CafeNotFoundException("Cafe not found with id: " + cafeId))
                .when(cafeService).deleteCafe(cafeId);

        ResponseEntity<?> response = cafeController.deleteCafe(cafeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cafe not found with id: " + cafeId, response.getBody());
    }

}
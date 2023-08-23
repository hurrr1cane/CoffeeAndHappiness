package com.mdn.backend.controller;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.service.CafeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cafe")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@Slf4j
public class CafeController {

    private final CafeService cafeService;

    @GetMapping
    public ResponseEntity<List<Cafe>> getAllCafes() {
        log.info("Getting all cafes");
        return ResponseEntity.ok(cafeService.getAllCafes());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCafeById(@PathVariable Integer id) {
        log.info("Getting cafe with id {}", id);
        try {
            Cafe cafe = cafeService.getCafeById(id);
            return ResponseEntity.ok(cafe);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cafe not found with id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<?> addCafe(@RequestBody @Valid Cafe cafe) {
        log.info("Adding new cafe");

        try {
            Cafe addedCafe = cafeService.addCafe(cafe);
            return new ResponseEntity<>(addedCafe, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Error while adding cafe: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editCafe(@PathVariable Integer id, @RequestBody @Valid Cafe cafe) {
        log.info("Editing cafe with id {}", id);

        try {
            Cafe editedCafe = cafeService.editCafe(id, cafe);
            return ResponseEntity.ok(editedCafe);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cafe not found with id: " + id);
        } catch (Exception ex) {
            log.error("Error while editing cafe: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while editing cafe: " + ex.getMessage());
        }
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<?> deleteCafe(@PathVariable Integer id) {
        log.info("Deleting cafe with id {}", id);
        try {
            cafeService.deleteCafe(id);
            return ResponseEntity.noContent().build();
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cafe not found with id: " + id);
        } catch (Exception ex) {
            log.error("Error while deleting cafe: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while deleting cafe: " + ex.getMessage());
        }
    }

}

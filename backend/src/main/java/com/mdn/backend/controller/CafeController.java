package com.mdn.backend.controller;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.service.CafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cafe")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@Slf4j
public class CafeController {

    private final CafeService cafeService;
    private final Validator validator;

    @GetMapping
    public ResponseEntity<List<Cafe>> getAllCafes() {
        log.info("Getting all cafes");
        return ResponseEntity.ok(cafeService.getAllCafes());
    }

    @GetMapping("{id}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable Integer id) {
        log.info("Getting cafe with id {}", id);
        try {
            Cafe cafe = cafeService.getCafeById(id);
            return ResponseEntity.ok(cafe);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addCafe(@RequestBody Cafe cafe) {
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
    public ResponseEntity<?> editCafe(@PathVariable Integer id, @RequestBody Cafe cafe) {
        log.info("Editing cafe with id {}", id);

        try {
            Cafe editedCafe = cafeService.editCafe(id, cafe);
            return ResponseEntity.ok(editedCafe);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Error while editing cafe: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<Void> deleteCafe(@PathVariable Integer id) {
        log.info("Deleting cafe with id {}", id);
        try {
            cafeService.deleteCafe(id);
            return ResponseEntity.noContent().build();
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

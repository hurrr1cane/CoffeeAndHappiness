package com.mdn.backend.controller;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all cafes", description = "Retrieve a list of all available cafes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Cafe>> getAllCafes() {
        log.info("Getting all cafes");
        return ResponseEntity.ok(cafeService.getAllCafes());
    }

    @Operation(summary = "Get cafe by id", description = "Retrieve a cafe by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cafe not found")
    })
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

    @Operation(summary = "Add cafe", description = "Add a new cafe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Edit cafe", description = "Edit a cafe by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cafe not found"),
    })
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

    @Operation(summary = "Delete cafe", description = "Delete a cafe by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cafe not found"),
    })
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

package com.mdn.backend.controller;

import com.mdn.backend.exception.notfound.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.dto.CafeDTO;
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
import org.springframework.web.multipart.MultipartFile;

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
            @ApiResponse(responseCode = "200", description = "Success")
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
    public ResponseEntity<?> addCafe(@RequestBody @Valid CafeDTO cafe) {
        log.info("Adding new cafe");

        Cafe addedCafe = cafeService.addCafe(cafe);
        return new ResponseEntity<>(addedCafe, HttpStatus.CREATED);
    }

    @Operation(summary = "Add cafe image", description = "Add an image to a cafe by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cafe not found"),
    })
    @PostMapping("{cafeId}/image/add")
    public ResponseEntity<?> addCafeImage(@PathVariable Integer cafeId, @RequestParam("image") MultipartFile image) {
        log.info("Adding image to cafe with id {}", cafeId);

        try {
            Cafe cafe = cafeService.addCafeImage(cafeId, image);
            return ResponseEntity.ok(cafe);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", cafeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cafe not found with id: " + cafeId);
        }
    }

    @Operation(summary = "Delete cafe image", description = "Delete an image from a cafe by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cafe not found"),
    })
    @DeleteMapping("{cafeId}/image/delete")
    public ResponseEntity<?> deleteCafeImage(@PathVariable Integer cafeId) {
        log.info("Deleting image from cafe with id {}", cafeId);

        try {
            Cafe cafe = cafeService.deleteCafeImage(cafeId);
            return ResponseEntity.ok(cafe);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", cafeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cafe not found with id: " + cafeId);
        }
    }

    @Operation(summary = "Edit cafe", description = "Edit a cafe by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Cafe not found"),
    })
    @PutMapping("{id}")
    public ResponseEntity<?> editCafe(@PathVariable Integer id, @RequestBody @Valid CafeDTO cafe) {
        log.info("Editing cafe with id {}", id);

        try {
            Cafe editedCafe = cafeService.editCafe(id, cafe);
            return ResponseEntity.ok(editedCafe);
        } catch (CafeNotFoundException ex) {
            log.error("Cafe not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cafe not found with id: " + id);
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
        }
    }

}

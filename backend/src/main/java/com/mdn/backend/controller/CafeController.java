package com.mdn.backend.controller;

import com.mdn.backend.model.Cafe;
import com.mdn.backend.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cafe")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class CafeController {

    private final CafeService cafeService;

    @GetMapping
    public ResponseEntity<List<Cafe>> getAllCafes() {
        return ResponseEntity.ok(cafeService.getAllCafes());
    }

    @GetMapping("{id}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable Integer id) {
        return ResponseEntity.ok(cafeService.getCafeById(id));
    }

    @PostMapping
    public ResponseEntity<Cafe> addCafe(@RequestBody Cafe cafe) {
        return new ResponseEntity<>(cafeService.addCafe(cafe), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Cafe> addCafe(@PathVariable Integer id, @RequestBody Cafe cafe) {
        return ResponseEntity.ok(cafeService.editCafe(id, cafe));
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<Void> deleteCafe(@PathVariable Integer id) {
        cafeService.deleteCafe(id);
        return ResponseEntity.noContent().build();
    }

}

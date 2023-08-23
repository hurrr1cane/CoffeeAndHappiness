package com.mdn.backend.service;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    public Cafe getCafeById(Integer id) {
        return cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );
    }

    public Cafe addCafe(Cafe cafe) {
        return cafeRepository.save(cafe);
    }

    public Cafe editCafe(Integer id, Cafe cafe) {
        var editedCafe = cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );

        editedCafe.setImageUrl(cafe.getImageUrl());
        editedCafe.setLocation(cafe.getLocation());
        editedCafe.setPhoneNumber(cafe.getPhoneNumber());

        return cafeRepository.save(editedCafe);
    }

    public void deleteCafe(Integer id) {
        var cafeToDelete = cafeRepository.findById(id).orElseThrow(
                () -> new CafeNotFoundException("No such cafe with id " + id + " found")
        );
        cafeRepository.delete(cafeToDelete);
    }
}

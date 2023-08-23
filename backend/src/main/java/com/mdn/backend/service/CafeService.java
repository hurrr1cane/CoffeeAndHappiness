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
        editedCafe.setLocation(cafe.getLocation());
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
}

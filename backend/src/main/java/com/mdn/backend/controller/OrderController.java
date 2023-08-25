package com.mdn.backend.controller;

import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.order.Order;
import com.mdn.backend.model.order.OrderRequest;
import com.mdn.backend.service.FoodService;
import com.mdn.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("Placing a new order");

        try {
            List<Food> selectedFoods = foodService.getFoodsByIds(orderRequest.getFoodIds());
            Order order = orderService.placeOrder(orderRequest.getUserId(), selectedFoods);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (FoodNotFoundException ex) {
            log.error("One or more selected foods not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more selected foods not found");
        } catch (Exception ex) {
            log.error("Error while placing an order: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

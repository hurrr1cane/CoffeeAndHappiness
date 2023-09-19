package com.mdn.backend.controller;

import com.mdn.backend.exception.FoodNotFoundException;
import com.mdn.backend.exception.NotEnoughBonusPointsException;
import com.mdn.backend.exception.OrderNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.order.Order;
import com.mdn.backend.model.order.OrderRequest;
import com.mdn.backend.service.FoodService;
import com.mdn.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final FoodService foodService;

    @Operation(summary = "Place a new order", description = "Place a new order for a user with a list of selected foods.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Invalid food id", content = @Content(schema = @Schema(implementation = String.class), examples = {@ExampleObject(name = "Invalid food id", value = "One or more selected foods not found")}))
    })
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
        }
    }

    @Operation(summary = "Delete order", description = "Delete an order by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = String.class), examples = {@ExampleObject(name = "Order not found", value = "Order not found with id: 1")})),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        log.info("Deleting order with id {}", id);
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (OrderNotFoundException ex) {
            log.error("Order not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found with id: " + id);
        }
    }

    @Operation(summary = "Spend points", description = "Spend points for a user with a list of selected foods.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Points spent", content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = String.class), examples = {@ExampleObject(name = "User not found", value = "User not found with id: 1")})),
            @ApiResponse(responseCode = "400", description = "Not enough bonus points", content = @Content(schema = @Schema(implementation = String.class), examples = {@ExampleObject(name = "Not enough bonus points", value = "Not enough bonus points")}))
    })
    @PostMapping("/spend-points")
    public ResponseEntity<?> spendPoints(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("Spending points");

        try {
            List<Food> selectedFoods = foodService.getFoodsByIds(orderRequest.getFoodIds());
            var orderResult = orderService.spendPoints(orderRequest.getUserId(), selectedFoods);
            return new ResponseEntity<>(orderResult, HttpStatus.OK);
        } catch (FoodNotFoundException ex) {
            log.error("One or more selected foods not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or more selected foods not found");
        } catch (NotEnoughBonusPointsException ex) {
            log.error("Not enough bonus points");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough bonus points");
        }
    }
}

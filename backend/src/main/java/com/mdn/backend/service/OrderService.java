package com.mdn.backend.service;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.model.Food;
import com.mdn.backend.model.order.Order;
import com.mdn.backend.model.user.User;
import com.mdn.backend.repository.OrderRepository;
import com.mdn.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public Order placeOrder(Integer userId, List<Food> selectedFoods) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Double totalPrice = calculateTotalPrice(selectedFoods);
        Integer bonusPointsEarned = calculateBonusPoints(totalPrice);

        Order order = Order.builder()
                .user(user)
                .foods(selectedFoods)
                .totalPrice(totalPrice)
                .orderDate(new Date())
                .bonusPointsEarned(bonusPointsEarned)
                .build();

        user.setBonusPoints(user.getBonusPoints() + bonusPointsEarned);
        userRepository.save(user);

        return orderRepository.save(order);
    }

    private Double calculateTotalPrice(List<Food> foods) {
        return foods.stream()
                .mapToDouble(Food::getPrice)
                .sum();
    }

    private Integer calculateBonusPoints(Double totalPrice) {
        return (int) (totalPrice * 10);
    }

}

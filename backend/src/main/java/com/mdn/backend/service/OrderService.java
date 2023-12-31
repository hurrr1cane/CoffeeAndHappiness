package com.mdn.backend.service;

import com.mdn.backend.exception.NotEnoughBonusPointsException;
import com.mdn.backend.exception.notfound.UserNotFoundException;
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

        double totalPrice = calculateTotalPrice(selectedFoods);
        int bonusPointsEarned = (int) totalPrice;

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

    public void deleteOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Order not found with id: " + id));

        User user = order.getUser();
        user.setBonusPoints(user.getBonusPoints() - order.getBonusPointsEarned());
        userRepository.save(user);

        orderRepository.delete(order);
    }

    public Order spendPoints(Integer userId, List<Food> selectedFoods) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        double totalPrice = calculateTotalPrice(selectedFoods);
        int bonusPointsNeededForOrder = calculateBonusPoints(totalPrice);

        if (user.getBonusPoints() < bonusPointsNeededForOrder) {
            throw new NotEnoughBonusPointsException("Not enough bonus points");
        }

        user.setBonusPoints(user.getBonusPoints() - bonusPointsNeededForOrder);
        userRepository.save(user);

        Order order = Order.builder()
                .user(user)
                .foods(selectedFoods)
                .totalPrice(totalPrice)
                .orderDate(new Date())
                .bonusPointsEarned(-bonusPointsNeededForOrder)
                .build();

        return orderRepository.save(order);

    }

    private double calculateTotalPrice(List<Food> foods) {
        double totalPrice = foods.stream()
                .mapToDouble(Food::getPrice)
                .sum();

        totalPrice = Math.round(totalPrice * 100.0) / 100.0;

        return totalPrice;
    }

    private int calculateBonusPoints(Double totalPrice) {
        return (int) (totalPrice * 10);
    }

}

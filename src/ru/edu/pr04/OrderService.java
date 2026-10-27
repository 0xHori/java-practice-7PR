package ru.edu.pr04;

public class OrderService {

    public Order createOrder(Employee employee) {
        return new Order(employee);
    }

    public void validateForConfirm(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Заказ не может быть null");
        }
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new IllegalStateException("Заказ уже подтверждён");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Заказ не содержит позиций");
        }
    }
}

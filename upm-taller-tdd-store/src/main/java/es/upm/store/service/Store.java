package es.upm.store.service;

import es.upm.store.model.Order;
import es.upm.store.model.OrderStatus;
import es.upm.store.model.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Store {

    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Order> orders = new LinkedHashMap<>();

    public User addUser(String id, String name, String address) {
        if (users.containsKey(id)) {
            throw new IllegalArgumentException("User already exists: " + id);
        }
        User user = new User(id, name, address);
        users.put(id, user);
        return user;
    }

    public User getUser(String id) {
        User user = users.get(id);
        if (user == null) {
            throw new NoSuchElementException("User not found: " + id);
        }
        return user;
    }

    public Order createOrder(String id, String userId, double price) {
        if (orders.containsKey(id)) {
            throw new IllegalArgumentException("Order already exists: " + id);
        }
        getUser(userId);
        Order order = new Order(id, userId, price);
        orders.put(id, order);
        return order;
    }

    public Order getOrder(String id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new NoSuchElementException("Order not found: " + id);
        }
        return order;
    }

    public List<Order> getOrders() {
        return List.copyOf(orders.values());
    }

    public List<Order> getOrdersByUser(String userId) {
        getUser(userId);
        return orders.values().stream()
                .filter(order -> order.getUserId().equals(userId))
                .toList();
    }

    public Order processOrder(String orderId) {
        return advance(orderId, OrderStatus.PROCESSED);
    }

    public Order dispatchOrder(String orderId) {
        return advance(orderId, OrderStatus.DISPATCHED);
    }

    public Order deliverOrder(String orderId) {
        return advance(orderId, OrderStatus.DELIVERED);
    }

    private Order advance(String orderId, OrderStatus next) {
        Order order = getOrder(orderId);
        order.updateStatus(next);
        return order;
    }
}

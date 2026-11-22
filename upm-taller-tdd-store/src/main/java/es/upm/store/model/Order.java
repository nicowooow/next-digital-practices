package es.upm.store.model;

import java.util.Objects;

public class Order {

    private final String id;
    private final String userId;
    private final double price;
    private OrderStatus status;

    public Order(String id, String userId, double price) {
        this.id = Objects.requireNonNull(id, "id");
        this.userId = Objects.requireNonNull(userId, "userId");
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        this.price = price;
        this.status = OrderStatus.DRAFT;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public double getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void updateStatus(OrderStatus next) {
        Objects.requireNonNull(next, "next");
        if (!status.canTransitionTo(next)) {
            throw new IllegalStateException("Cannot transition from " + status + " to " + next);
        }
        this.status = next;
    }
}

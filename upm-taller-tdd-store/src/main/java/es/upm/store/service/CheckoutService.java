package es.upm.store.service;

import es.upm.store.model.Order;
import es.upm.store.model.User;

import java.util.Objects;

public class CheckoutService {

    private final Store store;
    private final PaymentGateway paymentGateway;
    private final Notifier notifier;

    public CheckoutService(Store store, PaymentGateway paymentGateway, Notifier notifier) {
        this.store = Objects.requireNonNull(store, "store");
        this.paymentGateway = Objects.requireNonNull(paymentGateway, "paymentGateway");
        this.notifier = Objects.requireNonNull(notifier, "notifier");
    }

    public Order checkout(String orderId) {
        Order order = store.getOrder(orderId);
        User user = store.getUser(order.getUserId());

        if (!paymentGateway.charge(user, order.getPrice())) {
            throw new IllegalStateException("Payment failed for order " + orderId);
        }

        store.processOrder(orderId);
        notifier.orderStatusChanged(user, order);
        return order;
    }
}

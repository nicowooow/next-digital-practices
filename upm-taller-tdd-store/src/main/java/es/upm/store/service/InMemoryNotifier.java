package es.upm.store.service;

import es.upm.store.model.Order;
import es.upm.store.model.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryNotifier implements Notifier {

    private final List<String> notifications = new ArrayList<>();

    @Override
    public void orderStatusChanged(User user, Order order) {
        notifications.add(user.getName() + ": order " + order.getId()
                + " is " + order.getStatus());
    }

    public List<String> getNotifications() {
        return List.copyOf(notifications);
    }
}

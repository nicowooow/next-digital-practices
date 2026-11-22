package es.upm.store.service;

import es.upm.store.model.Order;
import es.upm.store.model.User;

public interface Notifier {

    void orderStatusChanged(User user, Order order);
}

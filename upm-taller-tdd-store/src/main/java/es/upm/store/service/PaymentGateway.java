package es.upm.store.service;

import es.upm.store.model.User;

public interface PaymentGateway {

    boolean charge(User user, double amount);
}

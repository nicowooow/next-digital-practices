package es.upm.store;

import es.upm.store.model.Order;
import es.upm.store.service.Store;

public class Main {

    public static void main(String[] args) {
        Store store = new Store();

        store.addUser("U1", "Alice", "Calle Mayor 1, Madrid");
        Order order = store.createOrder("O1", "U1", 49.99);

        store.processOrder("O1");
        store.dispatchOrder("O1");
        store.deliverOrder("O1");

        System.out.println("Order " + order.getId()
                + " for user " + order.getUserId()
                + " costs " + order.getPrice()
                + " and is " + order.getStatus());
    }
}

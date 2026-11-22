package es.upm.store.service;

import es.upm.store.model.Order;
import es.upm.store.model.OrderStatus;
import es.upm.store.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StoreTest {

    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store();
    }

    @Test
    void newOrderStartsAsDraft() {
        store.addUser("U1", "Alice", "Calle Mayor 1");

        Order order = store.createOrder("O1", "U1", 20.0);

        assertEquals(OrderStatus.DRAFT, order.getStatus());
    }

    @Test
    void orderCannotBeCreatedForUnknownUser() {
        assertThrows(NoSuchElementException.class,
                () -> store.createOrder("O1", "missing", 20.0));
    }

    @Test
    void orderCannotHaveNegativePrice() {
        store.addUser("U1", "Alice", "Calle Mayor 1");

        assertThrows(IllegalArgumentException.class,
                () -> store.createOrder("O1", "U1", -1.0));
    }

    @Test
    void orderFollowsDraftProcessedDispatchedDelivered() {
        store.addUser("U1", "Alice", "Calle Mayor 1");
        store.createOrder("O1", "U1", 20.0);

        assertEquals(OrderStatus.PROCESSED, store.processOrder("O1").getStatus());
        assertEquals(OrderStatus.DISPATCHED, store.dispatchOrder("O1").getStatus());
        assertEquals(OrderStatus.DELIVERED, store.deliverOrder("O1").getStatus());
    }

    @Test
    void orderCannotSkipStates() {
        store.addUser("U1", "Alice", "Calle Mayor 1");
        store.createOrder("O1", "U1", 20.0);

        assertThrows(IllegalStateException.class,
                () -> store.dispatchOrder("O1"));
    }

    @Test
    void deliveredOrderIsFinal() {
        store.addUser("U1", "Alice", "Calle Mayor 1");
        store.createOrder("O1", "U1", 20.0);
        store.processOrder("O1");
        store.dispatchOrder("O1");
        store.deliverOrder("O1");

        assertThrows(IllegalStateException.class,
                () -> store.processOrder("O1"));
    }

    @Test
    void ordersCanBeFoundByUser() {
        User alice = store.addUser("U1", "Alice", "Calle Mayor 1");
        store.addUser("U2", "Bob", "Calle Menor 2");
        store.createOrder("O1", alice.getId(), 20.0);
        store.createOrder("O2", alice.getId(), 30.0);
        store.createOrder("O3", "U2", 40.0);

        assertEquals(2, store.getOrdersByUser("U1").size());
        assertEquals(1, store.getOrdersByUser("U2").size());
    }

    @Test
    void userIdsMustBeUnique() {
        store.addUser("U1", "Alice", "Calle Mayor 1");

        assertThrows(IllegalArgumentException.class,
                () -> store.addUser("U1", "Bob", "Calle Menor 2"));
    }
}

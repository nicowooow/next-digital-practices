package es.upm.store.service;

import es.upm.store.model.Order;
import es.upm.store.model.OrderStatus;
import es.upm.store.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Or;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EjerciciosTest {

    private Store store;
    private User user;
    private Order order;

    private PaymentGateway pg;
    private Notifier nt;
    private CheckoutService co;
//    private CheckoutService checkOut;

    @BeforeEach
    void setUp() {
        this.store = new Store();
        this.user = store.addUser("U1", "usuario prueba", "calle upm");
        this.order = store.createOrder("order1", "U1", 20);
        ;


    }


    // LEVEL 1
    @Test
    void userIsMissing() {

        assertThrows(NoSuchElementException.class, () -> store.getUser("missing"));
    }

    @Test
    void existDuplicateOrder() {

        store.createOrder("02", "U1", 12.3);
        assertThrows(IllegalArgumentException.class, () -> store.createOrder("01", "U1", 12.3));
    }

    @Test
    void userOrders() {
        assertEquals(new ArrayList<>(), store.getOrdersByUser("U1"));
    }

    @Test
    void existsUser() {
        assertThrows(NoSuchElementException.class, () -> store.getOrdersByUser("U2"));
    }

    @Test
    void isPriceCorrect() {
        assertThrows(IllegalArgumentException.class, () -> store.createOrder("01", "U1", -0.01));
    }

    @Test
    void snapshotOrders() {
        List<Order> lista = store.getOrders();

        System.out.println("longitud de la lista inicial ==> " + lista.size());

//        lista.add(new Order("9", "U1", 1.0));
        assertThrows(UnsupportedOperationException.class,
                () -> lista.add(new Order("8", "U1", 1.0))
        );
    }

    @Test
    void insertionOrder() {
        store.createOrder("1", "U1", 1);
        store.createOrder("2", "U1", 1);
        store.createOrder("3", "U1", 1);
//        System.out.println(store.getOrders().toString());
        for (int i = 0; i < store.getOrders().size(); i++) {
            assertEquals((i + 1) + "", store.getOrders().get(i).getId());

        }

    }

    // LEVEL 2 -- nos dijo que lo saltaramos
/*
    @ParameterizedTest
    @CsvSource({
           ";;",
           ";;",
           ";;",
           ";;",
    })
    void transitionMatrix(){

    }
    */

    // LEVEL 3
    // este será el 14 que el 13 no lo quiere
//    @Test
//    void ChargeTheRightAsmount() {
//        PaymentGateway pg = mock(PaymentGateway.class); // creamos un mook de la pasarela de pago
//        Notifier nt = mock(Notifier.class);
//        CheckoutService co = new CheckoutService(store, pg, nt);
//
//        co.checkout(order.getId());
//        verify(nt).orderStatusChanged(user, order);
//
//    }


    @BeforeEach
    void start() {
        pg = mock(PaymentGateway.class); // creamos un mook de la pasarela de pago
        nt = mock(Notifier.class);
        co = new CheckoutService(store, pg, nt);
    }

    @Test
    void captureNotification() {


        when(pg.charge(user, 20.0)).thenReturn(true);
        // el capturador
        ArgumentCaptor<Order> acOrder = ArgumentCaptor.forClass(Order.class);
        co.checkout("order1");
//        verify(nt).notify();

        assertEquals(OrderStatus.PROCESSED, order.getStatus());
    }


    @Test
    void NotifyExactlyOnce() {
        when(pg.charge(user, 20.0)).thenReturn(true); // hacemos que se haga el pago
        co.checkout("order1"); // le cambiamos el estado
        verify(nt, times(1)).orderStatusChanged(user, order); // le notificamos
        verifyNoMoreInteractions(nt); // verificamos que no tenga más interacciones
    }

    @Test
    void noNotificationOnFailedPayment(){
        when(pg.charge(user, 20.0)).thenReturn(false); // hacemos que no se haga el pago
        co.checkout("order1"); // le cambiamos el estado
    assertEquals(OrderStatus.DRAFT, order.getStatus());
        verifyNoMoreInteractions(nt); // verificamos que no tenga más interacciones
    }
    // LEVEL 4

}

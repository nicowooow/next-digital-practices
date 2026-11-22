package es.upm.store.service;

import es.upm.store.model.Order;
import es.upm.store.model.OrderStatus;
import es.upm.store.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CheckoutServiceTest {

    private Store store;
    private User alice;
    private Order order;

    @BeforeEach
    void setUp() {
        store = new Store();
        alice = store.addUser("U1", "Alice", "Calle Mayor 1");
        order = store.createOrder("O1", "U1", 20.0);
    }

    @Test
    void stubControlsThePaymentResult() {
        PaymentGateway stubGateway = mock(PaymentGateway.class);
        when(stubGateway.charge(alice, 20.0)).thenReturn(true);
        Notifier dummyNotifier = mock(Notifier.class);

        CheckoutService checkout = new CheckoutService(store, stubGateway, dummyNotifier);
        checkout.checkout("O1");

        assertEquals(OrderStatus.PROCESSED, order.getStatus());
    }

    @Test
    void mockVerifiesThatTheCustomerWasNotified() {
        PaymentGateway stubGateway = (user, amount) -> true;
        Notifier mockNotifier = mock(Notifier.class);

        CheckoutService checkout = new CheckoutService(store, stubGateway, mockNotifier);
        checkout.checkout("O1");

        verify(mockNotifier).orderStatusChanged(alice, order);
    }

    @Test
    void spyUsesRealBehaviourAndStillRecordsTheCall() {
        InMemoryNotifier spyNotifier = spy(new InMemoryNotifier());

        CheckoutService checkout = new CheckoutService(store, (user, amount) -> true, spyNotifier);
        checkout.checkout("O1");

        verify(spyNotifier).orderStatusChanged(alice, order);
        assertEquals(1, spyNotifier.getNotifications().size());
    }

    @Test
    void failedPaymentKeepsTheOrderAsDraftAndDoesNotNotify() {
        PaymentGateway stubGateway = mock(PaymentGateway.class);
        when(stubGateway.charge(alice, 20.0)).thenReturn(false);
        Notifier mockNotifier = mock(Notifier.class);

        CheckoutService checkout = new CheckoutService(store, stubGateway, mockNotifier);

        assertThrows(IllegalStateException.class, () -> checkout.checkout("O1"));
        assertEquals(OrderStatus.DRAFT, order.getStatus());
        verifyNoInteractions(mockNotifier);
    }
}

# Test exercises

Write one test at a time, run it, and watch it fail for the right reason before
changing production code (red - green - refactor).

Useful commands:

```sh
mvn test
mvn -Dtest=StoreTest test
mvn -Dtest=CheckoutServiceTest test
```

## Level 1 - Store (no test doubles)

1. **Unknown user**
   - Action: `store.getUser("missing")`.
   - Assert: throws `NoSuchElementException`.

2. **Duplicate order**
   - Setup: create `O1`, then call `createOrder("O1", ...)` again.
   - Assert: throws `IllegalArgumentException` and the first order is unchanged.

3. **User without orders**
   - Setup: add a user but no orders.
   - Assert: `getOrdersByUser(id)` returns an empty list (not `null`).

4. **Unknown user in `getOrdersByUser`**
   - Action: `getOrdersByUser("missing")`.
   - Assert: throws `NoSuchElementException`.

5. **Price boundaries**
   - Assert: price `0.0` is accepted.
   - Assert: price `-0.01` throws `IllegalArgumentException`.

6. **Snapshot of orders**
   - Setup: get `getOrders()`, then create another order.
   - Assert: the earlier list size did not change.
   - Assert: calling `add(...)` on the returned list throws
     `UnsupportedOperationException`.

7. **Insertion order**
   - Setup: create `O1`, `O2`, `O3`.
   - Assert: `getOrders()` returns them in that same order.

## Level 2 - Order state machine

8. **Transition matrix**
   - Use a `@ParameterizedTest` with `@CsvSource` and cover all 4 x 4 pairs.
   - Assert: only `DRAFT->PROCESSED`, `PROCESSED->DISPATCHED`,
     `DISPATCHED->DELIVERED` are allowed.

9. **Null target status**
   - Action: `order.updateStatus(null)`.
   - Assert: throws `NullPointerException` and the status does not change.

10. **Processing twice**
    - Setup: process an order, then process it again.
    - Assert: throws `IllegalStateException` and the status is still `PROCESSED`.

11. **Deliver without dispatch**
    - Setup: a `DRAFT` order.
    - Assert: `updateStatus(DELIVERED)` throws and the status is still `DRAFT`.

12. **Full happy path on `Order`**
    - Assert the status after each step: `DRAFT`, `PROCESSED`, `DISPATCHED`,
      `DELIVERED`.

## Level 3 - `CheckoutService` with doubles

13. **Charge the right amount**
    - Use a mock `PaymentGateway`.
    - Assert with `verify` that it was charged with Alice and `20.0`.
    - Hint: `eq(...)` and `any(...)` help when mixing exact values and matchers.

14. **Capture the notification**
    - Use an `ArgumentCaptor<Order>`.
    - Assert: the captured order has id `O1` and status `PROCESSED`.

15. **Notify exactly once**
    - Assert: `verify(notifier, times(1)).orderStatusChanged(...)`.
    - Add `verifyNoMoreInteractions(notifier)`.

16. **No notification on failed payment**
    - Stub the gateway to return `false`.
    - Assert: `IllegalStateException`, order still `DRAFT`, and
      `verifyNoInteractions(notifier)`.

17. **Unknown order**
    - Action: `checkout("missing")`.
    - Assert: `NoSuchElementException`, and neither the gateway nor the notifier
      was touched.

18. **Gateway throws**
    - Stub the gateway with `when(...).thenThrow(new RuntimeException("boom"))`.
    - Assert: the exception propagates, the order stays `DRAFT`, and the
      notifier is not called.

19. **Checkout an already processed order** (intentional failing test)
    - Setup: process `O1` before calling `checkout`.
    - Assert: throws `IllegalStateException`.
    - Assert: the gateway is **not** charged again.
    - This test will fail. Fix `CheckoutService` so it validates the order
      status before charging.

20. **Verify the store interaction**
    - Use `spy(new Store())` and create the order through the spy.
    - Assert: `verify(storeSpy).processOrder("O1")` is called exactly once on
      success, and `never()` when payment fails.

21. **Order of interactions**
    - Use `InOrder` with the gateway, a store spy, and the notifier.
    - Assert: charge, then process, then notify.

22. **Notifier throws after processing**
    - Use `doThrow(...).when(spyNotifier).orderStatusChanged(...)`.
    - Decide and document the expected behaviour (the order is already
      `PROCESSED`). Then assert it.

23. **Hand-written stub**
    - Implement `RecordingPaymentGateway` with a configurable result plus lists
      of charged users and amounts.
    - Use it for both the success and failure cases, without Mockito.

24. **Hand-written spy**
    - Implement `RecordingNotifier` that both records and delegates to an
      `InMemoryNotifier`.
    - Repeat exercise 15 with it to compare with the Mockito version.

## Level 4 - Stretch goals

25. **Unmodifiable results**
    - Write tests for `getOrders()` and `getOrdersByUser(...)`.
    - If they fail, make the production code return unmodifiable snapshots.

26. **Checkout idempotency**
    - Write a test that checks a `DELIVERED` order cannot be checked out and is
      never charged.

27. **Extract a repository interface**
    - Introduce `OrderRepository` and make `CheckoutService` depend on it.
    - Rewrite the tests using a stub repository instead of a real `Store`.

28. **Mutation check**
    - Write a test proving that changing the returned `User` or `Order` object
      cannot corrupt the `Store`.

## Definition of done for each exercise

- The test fails before the production change.
- The name describes the behaviour, not the implementation.
- One behaviour is asserted per test.
- The test does not depend on execution order or on another test's data.

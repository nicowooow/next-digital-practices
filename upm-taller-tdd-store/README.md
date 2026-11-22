# Store

Simple Java store for practising TDD.

## Domain

- `User`: `id`, `name`, `address`
- `Order`: `id`, `userId`, `status`, `price`
- `OrderStatus`: `DRAFT -> PROCESSED -> DISPATCHED -> DELIVERED`

An order always starts as `DRAFT`. States can only advance one step at a time,
and `DELIVERED` is final.

## Test doubles exercise

`CheckoutServiceTest` shows the three classic test doubles with Mockito:

- **Stub**: `stubControlsThePaymentResult` fixes what `PaymentGateway` returns.
- **Mock**: `mockVerifiesThatTheCustomerWasNotified` verifies the interaction
  with `Notifier`.
- **Spy**: `spyUsesRealBehaviourAndStillRecordsTheCall` wraps a real
  `InMemoryNotifier` and still records the call.

`failedPaymentKeepsTheOrderAsDraftAndDoesNotNotify` combines a stub with a mock.

More exercises for students are listed in [EXERCISES.md](EXERCISES.md).

## Commands

```sh
mvn test        # run the tests
mvn compile     # compile the project
mvn exec:java -Dexec.mainClass=es.upm.store.Main   # run the demo
```

Java 21 or newer is required.

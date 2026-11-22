package es.upm.store.model;

public enum OrderStatus {
    DRAFT,
    PROCESSED,
    DISPATCHED,
    DELIVERED;

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case DRAFT -> next == PROCESSED;
            case PROCESSED -> next == DISPATCHED;
            case DISPATCHED -> next == DELIVERED;
            case DELIVERED -> false;
        };
    }
}

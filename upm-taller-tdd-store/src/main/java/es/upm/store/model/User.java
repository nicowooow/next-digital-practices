package es.upm.store.model;

import java.util.Objects;

public class User {

    private final String id;
    private final String name;
    private final String address;

    public User(String id, String name, String address) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.address = Objects.requireNonNull(address, "address");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}

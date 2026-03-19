package graph;

import Structures.Address;

public class V {
    private final Address address;

    public V(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return address.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        V vertex = (V) obj;
        return address.equals(vertex.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}

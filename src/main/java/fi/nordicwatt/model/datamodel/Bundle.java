package fi.nordicwatt.model.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fi.nordicwatt.types.Status;

public class Bundle<T> {
    private final String id;
    protected List<T> items;
    private Status status;

    public Bundle() {
        this.id = UUID.randomUUID().toString();
        this.status = Status.PENDING;
        this.items = new ArrayList<>(); 
    }

    public String getId() {
        return id;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void addItem(T item) {
        this.items.add(item);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String str = "";
        for (T item : items) {
            str += item.toString() + "\n";
        }
        return str;
    }
}

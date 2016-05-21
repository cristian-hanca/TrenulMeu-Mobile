package ro.trenulmeu.mobile.filters.models;

import java.io.Serializable;

/**
 * Item used in the Selection Adapter.
 */
public class CheckItem<T> implements Serializable {

    private final String name;
    private boolean check;
    private final T item;

    public CheckItem(T item, String name, boolean check) {
        this.item = item;
        this.name = name;
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public T getItem() {
        return item;
    }

    public String getName() {
        return name;
    }
}

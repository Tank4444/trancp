package entity;

public class Road {
    private Store store;
    private Magazine magazine;

    public Store getStore()
    {
        return store;
    }

    public void setStore(Store store)
    {
        this.store=store;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    private int value;
    public int getValue()
    {
        return value;
    }
    public void setValue(int value)
    {
        this.value=value;
    }

    public Road(Store store, Magazine magazine, int value) {
        this.store = store;
        this.magazine = magazine;
        this.value = value;
    }

    public Road() {
    }
}
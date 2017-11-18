package entity;

/**
 * Created by tank on 14.11.17.
 */
public class Store {
    private int value;

    public int getValue()
    {
        return value;
    }
    public void setValue(int value)
    {
        this.value=value;
    }
    public void setValue(String value){this.value=Integer.parseInt(value);}

    public Store(int value) {
        this.value = value;
    }

    public Store() {
        value = 0 ;
    }

    public Store(String string)
    {
        value=Integer.parseInt(string);
    }

}

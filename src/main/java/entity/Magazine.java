package entity;

/**
 * Created by tank on 14.11.17.
 */
public class Magazine {
    private int value;
    public int getValue()
    {
        return value;
    }
    public void setValue(int value)
    {
        this.value=value;
    }

    public Magazine(int value) {
        this.value = value;
    }

    public Magazine() {

    }

    public Magazine(String value){
        this.value=Integer.parseInt(value);
    }
}

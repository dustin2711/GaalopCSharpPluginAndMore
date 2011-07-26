package de.gaalop.tba;

/**
 * Stores a variable and it's value
 * @author christian
 */
public class VariableValue {

    private String name;
    private float value;

    public VariableValue(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(float value) {
        this.value = value;
    }

    

}

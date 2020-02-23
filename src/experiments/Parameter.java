package experiments;

public class Parameter {

    public String name;
    public String[] values;
    public int activeValueIndex;

    public Parameter(String name, String[] values) {
        this.name = name;
        this.values = values;
        activeValueIndex = 0;
    }

    public String getActiveValue() {
        return values[activeValueIndex];
    }

    public int getNumberOfValues() {
        return values.length;
    }
}

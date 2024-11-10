package models;

public class CellResolution {
    private final CellResolutionType type;
    private final int value;

    public CellResolution(CellResolutionType type, int value) {
        this.type = type;
        this.value = value;
    }

    public CellResolutionType getType() { return type; }
    public int getValue() { return value; }
}

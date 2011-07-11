package boardHelpers;

/**
 * Defines all the sides available, and can return the other side via otherSide
 * @author Randy
 */
public enum Side {
    X, O;

    public static Side otherSide(Side side) {
        return (side == X) ? O : X;
    }
}

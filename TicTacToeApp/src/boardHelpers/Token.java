package boardHelpers;

/**
 * Represents a "piece" (in the lingo of checkers) on the board
 * @author Randy
 */
public class Token {
    public final Side SIDE;

    public Token (Side side) {
        this.SIDE = side;
    }

    public static final Token X_Token = new Token(Side.X);
    public static final Token O_Token = new Token(Side.O);

    public static Token getToken(Side side) {
        return (side == Side.X) ? X_Token : O_Token;
    }
}

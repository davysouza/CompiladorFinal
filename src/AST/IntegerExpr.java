
package AST;

/**
 *
 * @author Davy
 */
public class IntegerExpr extends NumberExpr {
    
    private final long value;

    public IntegerExpr(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        pw.out.print(value);
    }

    @Override
    public Type getType() {
        return Type.integerType;
    }
}

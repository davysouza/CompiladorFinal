package AST;

/**
 *
 * @author Davy
 */
public class RealExpr extends NumberExpr {
    
    private final double value;

    public RealExpr(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        pw.out.print(value);
    }

    @Override
    public Type getType() {
        return Type.realType;
    }
}

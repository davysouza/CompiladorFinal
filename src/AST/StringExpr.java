package AST;

/**
 *
 * @author Davy
 */
public class StringExpr extends Expr {
    private final String value;

    public StringExpr(String value) {
        this.value = value;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        pw.out.print(value);
    }

    @Override
    public Type getType() {
        return Type.stringType;
    }
    
    public String getValue() {
        return value;
    }
}

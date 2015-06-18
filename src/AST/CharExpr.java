package AST;

/**
 *
 * @author Davy
 */
public class CharExpr extends Expr {

    private final char value;

    public CharExpr(char value) {
        this.value = value;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        pw.out.print(value);
    }

    @Override
    public Type getType() {
        return Type.charType;
    }
    
    public char getValue() {
        return value;
    }
}
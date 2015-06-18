package AST;

/**
 *
 * @author Davy
 */
public class VariableExpr extends Expr {

    private final Variable v;
    private final long vectorPos;
    private final long vectorRatio;

    public VariableExpr(Variable v, long vectorPos, long vectorRatio) {
        this.v = v;
        this.vectorPos = vectorPos;
        this.vectorRatio = vectorRatio;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        pw.out.print(v.getName());
        
        if(vectorPos >= 0 && v.getSize() > 0)
            pw.out.print("[" + (vectorPos-vectorRatio) + "]");
    }

    @Override
    public Type getType() {
        return v.getType();
    }
}

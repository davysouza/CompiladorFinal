package AST;

/**
 *
 * @author Davy
 */
public class ReturnStatement extends Statement {

    private final Expr expr;

    public ReturnStatement(Expr expr) {
        this.expr = expr;
    }

    public ReturnStatement() {
        this.expr = null;
    }

    @Override
    public void genC(PW pw) {
        pw.print("return ");
        if (expr != null) {
            if (expr.getType() == Type.stringType && !(expr instanceof VariableExpr)) {
                pw.out.print("\"");
                expr.genC(pw, false);
                pw.out.print("\"");
            } else {
                expr.genC(pw, false);
            }
        }
        pw.out.println(";");
    }

}

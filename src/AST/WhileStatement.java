package AST;

/**
 *
 * @author Davy
 */
public class WhileStatement extends Statement {

    private final Expr expr;
    private final StatementList doPart;

    public WhileStatement(Expr expr, StatementList doPart) {
        this.expr = expr;
        this.doPart = doPart;
    }

    @Override
    public void genC(PW pw) {
        pw.print("while ( ");
        expr.genC(pw, false);
        pw.out.println(" ) { ");
        pw.add();
        doPart.genC(pw);
        pw.sub();
        pw.println("}");
    }
}

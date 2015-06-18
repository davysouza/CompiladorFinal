package AST;

/**
 *
 * @author Davy
 */
public class IfStatement extends Statement {

    private final Expr expr;
    private final StatementList thenPart;
    private final StatementList elsePart;

    public IfStatement(Expr expr, StatementList thenPart, StatementList elsePart) {
        this.expr = expr;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
    }

    @Override
    public void genC(PW pw) {
        pw.print("if ( ");
        expr.genC(pw, false);
        pw.out.println(" ) { ");
        if (thenPart != null) {
            pw.add();
            thenPart.genC(pw);
            pw.sub();
            pw.println("}");
        }
        if (elsePart != null) {
            pw.println("else {");
            pw.add();
            elsePart.genC(pw);
            pw.sub();
            pw.println("}");
        }
    }
}

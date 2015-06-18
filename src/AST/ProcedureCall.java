package AST;

/**
 *
 * @author Davy
 */
public class ProcedureCall extends Statement {

    private final Procedure procedure;
    private final ExprList exprList;

    public ProcedureCall(Procedure procedure, ExprList exprList) {
        this.procedure = procedure;
        this.exprList = exprList;
    }

    @Override
    public void genC(PW pw) {
        pw.print(procedure.getName() + "(");
        if (exprList != null) {
            exprList.genC(pw);
        }
        pw.out.println(");");
    }
}

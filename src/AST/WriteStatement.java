package AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class WriteStatement extends Statement {

    private final ExprList exprList;

    public WriteStatement(ExprList exprList) {
        this.exprList = exprList;
    }

    @Override
    public void genC(PW pw) {
        ArrayList<Expr> exList = exprList.getExprList();
        Enumeration e = Collections.enumeration(exList);
        while (e.hasMoreElements()) {
            Expr ex = (Expr) e.nextElement();

            if (ex.getType() == Type.charType) {
                pw.print("printf(\"%c\\n\", ");
                if (ex instanceof FunctionCall) {
                    ex.genC(pw, false);
                    pw.out.println(");");
                } else if (ex instanceof CharExpr) {
                    pw.out.print("'");
                    ex.genC(pw, false);
                    pw.out.println("' );");
                } else if (ex instanceof VariableExpr) {
                    ex.genC(pw, false);
                    pw.out.println(" );");
                }
            } else if (ex.getType() == Type.stringType) {
                pw.print("printf(\"%s\\n\", ");
                if (ex instanceof FunctionCall) {
                    ex.genC(pw, false);
                    pw.out.println(");");
                } else if (ex instanceof CharExpr || ex instanceof StringExpr) {
                    pw.out.print("\"");
                    ex.genC(pw, false);
                    pw.out.println("\" );");
                } else if (ex instanceof VariableExpr) {
                    ex.genC(pw, false);
                    pw.out.println(" );");
                }
            } else if (ex.getType() == Type.realType) {
                pw.print("printf(\"%lf\\n\", ");
                if (ex instanceof FunctionCall) {
                    ex.genC(pw, false);
                    pw.out.println(");");
                } else {
                    ex.genC(pw, false);
                    pw.out.println(" );");
                }
            } else {
                pw.print("printf(\"%ld\\n\", ");
                if (ex instanceof FunctionCall) {
                    ex.genC(pw, false);
                    pw.out.println(");");
                } else {
                    ex.genC(pw, false);
                    pw.out.println(" );");
                }
            }
        }
    }
}

package AST;

/**
 *
 * @author Davy
 */
public class AssignmentStatement extends Statement {

    private final Variable v;
    private final Expr expr;
    private final long variableSize;
    private final long ratio;

    public AssignmentStatement(Variable v, Expr expr, long variableSize, long ratio) {
        this.v = v;
        this.expr = expr;
        this.variableSize = variableSize;
        this.ratio = ratio;
    }

    @Override
    public void genC(PW pw) {
        if (v.getSize() == 0) {
            if (expr instanceof FunctionCall) {
                pw.print(v.getName() + " = ");
                expr.genC(pw, false);
            } else if (v.getType() == Type.stringType) {
                pw.print("strcpy(" + v.getName() + ", ");
                if (expr instanceof StringExpr || expr instanceof CharExpr) {
                    pw.out.print("\"");
                    expr.genC(pw, false);
                    pw.out.print("\")");
                } else if (expr instanceof VariableExpr) {
                    expr.genC(pw, false);
                    pw.out.print(")");
                }
            } else {
                pw.print(v.getName() + " = ");
                if (v.getType() == Type.charType) {
                    if (expr instanceof CharExpr) {
                        pw.out.print("'");
                        expr.genC(pw, false);
                        pw.out.print("'");
                    } else if (expr instanceof VariableExpr) {
                        expr.genC(pw, false);
                    }
                } else {
                    expr.genC(pw, false);
                }
            }
        } else {
            if (expr instanceof FunctionCall) {
                pw.print(v.getName() + "[" + (variableSize - ratio) + "] = ");
                expr.genC(pw, false);
            } else if (v.getType() == Type.stringType) {
                pw.print("strcpy(" + v.getName() + "[" + (variableSize - ratio) + "], ");
                if (expr instanceof StringExpr || expr instanceof CharExpr) {
                    pw.out.print("\"");
                    expr.genC(pw, false);
                    pw.out.print("\")");
                } else if (expr instanceof VariableExpr) {
                    expr.genC(pw, false);
                    pw.out.print(")");
                }
            } else {
                pw.print(v.getName() + "[" + (variableSize - ratio) + "] = ");
                if (v.getType() == Type.charType) {
                    if (expr instanceof CharExpr) {
                        pw.out.print("'");
                        expr.genC(pw, false);
                        pw.out.print("'");
                    } else if (expr instanceof VariableExpr) {
                        expr.genC(pw, false);
                    }
                } else {
                    expr.genC(pw, false);
                }
            }
        }
        pw.out.println(";");
    }

    public long getVariableSize() {
        return variableSize;
    }
}

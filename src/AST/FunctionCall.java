package AST;

/**
 *
 * @author Davy
 */
public class FunctionCall extends Expr {

    private final Function function;
    private final ExprList exprList;

    public FunctionCall(Function function, ExprList exprList) {
        this.function = function;
        this.exprList = exprList;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        pw.out.print(function.getName() + "(");
        if (exprList != null) {
            exprList.genC(pw);
        }
        pw.out.print(")");
    }

    @Override
    public Type getType() {
        return function.getReturnType();
    }

}

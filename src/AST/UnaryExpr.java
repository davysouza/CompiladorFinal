package AST;

import Lexer.*;

/**
 *
 * @author Davy
 */
public class UnaryExpr extends Expr {

    private final Expr expr;
    private final int op;

    public UnaryExpr(Expr expr, int op) {
        this.expr = expr;
        this.op = op;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        switch (op) {
            case Symbol.PLUS:
                pw.out.print("+");
                break;
            case Symbol.MINUS:
                pw.out.print("-");
                break;
            case Symbol.NOT:
                pw.out.print("!");
                break;
        }
        expr.genC(pw, false);
    }

    @Override
    public Type getType() {
        return expr.getType();
    }
}

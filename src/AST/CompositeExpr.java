package AST;

import Lexer.*;

/**
 *
 * @author Davy
 */
public class CompositeExpr extends Expr {

    private final Expr left;
    private final Expr right;
    private final int oper;
    private static final String[] arrayOper;

    static {
        arrayOper = new String[Symbol.LastSymbol];
        int i;
        for (i = 0; i < arrayOper.length; i++) {
            arrayOper[i] = "";
        }
        arrayOper[Symbol.PLUS] = "+";
        arrayOper[Symbol.MINUS] = "-";
        arrayOper[Symbol.MULT] = "*";
        arrayOper[Symbol.REALDIV] = "/";
        arrayOper[Symbol.DIV] = "/";
        arrayOper[Symbol.MOD] = "%";
        arrayOper[Symbol.LT] = "<";
        arrayOper[Symbol.LE] = "<=";
        arrayOper[Symbol.GT] = ">";
        arrayOper[Symbol.GE] = ">=";
        arrayOper[Symbol.NEQ] = "!=";
        arrayOper[Symbol.EQ] = "==";
        arrayOper[Symbol.ASSIGN] = "=";
        arrayOper[Symbol.AND] = "&&";
        arrayOper[Symbol.OR] = "||";
    }

    public CompositeExpr(Expr p_left, int p_oper, Expr p_right) {
        this.left = p_left;
        this.oper = p_oper;
        this.right = p_right;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {

        if (putParenthesis) {
            pw.out.print("(");
        }
        left.genC(pw, true);
        pw.out.print(" " + arrayOper[oper] + " ");
        right.genC(pw, true);
        if (putParenthesis) {
            pw.out.print(")");
        }
    }

    @Override
    public Type getType() {
        // left and right must be the same type
        if (oper == Symbol.EQ || oper == Symbol.NEQ || oper == Symbol.LE
                || oper == Symbol.LT || oper == Symbol.GE || oper == Symbol.GT
                || oper == Symbol.AND || oper == Symbol.OR) {
            return Type.integerType;
        } else {
            if (left.getType() == Type.realType || right.getType() == Type.realType) {
                return Type.realType;
            }
            return Type.integerType;
        }
    }
}

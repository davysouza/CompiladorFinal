package AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class ExprList {

    private final ArrayList<Expr> list;

    public ExprList() {
        list = new ArrayList();
    }

    public void add(Expr expr) {
        list.add(expr);
    }

    public ArrayList<Expr> getExprList() {
        return list;
    }

    public void genC(PW pw) {
        int size = list.size();
        Enumeration e = Collections.enumeration(list);
        while (e.hasMoreElements()) {
            ((Expr) e.nextElement()).genC(pw, false);
            if (--size > 0) {
                pw.out.print(", ");
            }
        }
    }
}

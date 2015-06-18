package AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class ReadStatement extends Statement {

    private final ArrayList<Variable> varList;
    private final ArrayList variableSize;

    public ReadStatement(ArrayList varList, ArrayList variableSize) {
        this.varList = varList;
        this.variableSize = variableSize;
    }

    @Override
    public void genC(PW pw) {
        Enumeration e = Collections.enumeration(varList);
        Enumeration f = Collections.enumeration(variableSize);
        while (e.hasMoreElements()) {
            Variable v = (Variable) e.nextElement();
            long n = (long) f.nextElement();

            if (v.getType() == Type.charType) {
                pw.print("{ char s[256]; gets(s); sscanf(s, \"%c\", &");
            } else if (v.getType() == Type.stringType) {
                pw.print("{ char s[256]; gets(s); sscanf(s, \"%s\", &");
            } else if (v.getType() == Type.realType) {
                pw.print("{ char s[256]; gets(s); sscanf(s, \"%lf\", &");
            } else {
                pw.print("{ char s[256]; gets(s); sscanf(s, \"%ld\", &");
            }
            if (n == 0) {
                pw.out.println(v.getName() + "); }");
            } else {
                pw.out.println(v.getName() + "[" + n + "]); }");
            }
        }
    }
}

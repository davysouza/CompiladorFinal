package AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class DeclarationPart {

    private final ArrayList<Variable> dclList;
    private final ArrayList<Subroutine> subList;

    public DeclarationPart(ArrayList<Variable> dclList, ArrayList<Subroutine> subList) {
        this.dclList = dclList;
        this.subList = subList;
    }

    public void genC(PW pw) {
        // Generation code C -  variable declaration list
        Enumeration e = Collections.enumeration(dclList);
        while (e.hasMoreElements()) {
            Variable v = (Variable) e.nextElement();
            if (v.getSize() == 0) {
                if (v.getType() == Type.stringType) {
                    pw.println(v.getType().getCname() + " " + v.getName() + "[1024];");
                } else {
                    pw.println(v.getType().getCname() + " " + v.getName() + ";");
                }
            } else {
                if (v.getType() == Type.stringType) {
                    pw.println(v.getType().getCname() + " " + v.getName() + "[" + v.getSize() + "][1024];");
                } else {
                    pw.println(v.getType().getCname() + " " + v.getName() + "[" + v.getSize() + "];");
                }
            }
        }

        // Generation code C -  subroutines
        pw.out.println("");
        e = Collections.enumeration(subList);
        while (e.hasMoreElements()) {
            Subroutine s = (Subroutine) e.nextElement();
            s.genC(pw);
            pw.out.println("");
        }
        pw.sub();
    }
}

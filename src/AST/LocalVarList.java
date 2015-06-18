package AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class LocalVarList {

    private final ArrayList<Variable> list;

    public LocalVarList() {
        list = new ArrayList();
    }

    public void add(Variable variable) {
        list.add(variable);
    }

    public void genC(PW pw) {       
        Enumeration e = Collections.enumeration(list);
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
    }

}

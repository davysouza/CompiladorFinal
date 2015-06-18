package AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class ParamList {

    private final ArrayList<Parameter> list;

    public ParamList() {
        this.list = new ArrayList();
    }

    public void add(Parameter parameter) {
        list.add(parameter);
    }

    public int getSize() {
        return list.size();
    }

    public Enumeration elements() {
        return Collections.enumeration(list);
    }

    public void genC(PW pw) {
        Enumeration e = Collections.enumeration(list);
        int size = list.size();
        while (e.hasMoreElements()) {
            Parameter p = (Parameter) e.nextElement();
            if (p.getSize() == 0) {
                if (p.getType() == Type.stringType) {
                    pw.out.print(p.getType().getCname() + " " + p.getName() + "[1024]");
                } else {
                    pw.out.print(p.getType().getCname() + " " + p.getName());
                }
                if (--size > 0) {
                    pw.out.print(", ");
                }
            } else {
                if (p.getType() == Type.stringType) {
                    pw.out.print(p.getType().getCname() + " " + p.getName() + "[" + p.getSize() + "][1024]");
                } else {
                    pw.out.print(p.getType().getCname() + " " + p.getName() + "[" + p.getSize() + "]");
                }
                if (--size > 0) {
                    pw.out.print(", ");
                }
            }
        }
    }

}

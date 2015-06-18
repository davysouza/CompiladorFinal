package AST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class StatementList {
    
    private final ArrayList<Statement> list;
    
    public StatementList(ArrayList<Statement> list){
        this.list = list;
    }
    
    public void genC(PW pw){
        Enumeration e = Collections.enumeration(list);
        while (e.hasMoreElements()) {
            ((Statement) e.nextElement()).genC(pw);
        }
    }
}

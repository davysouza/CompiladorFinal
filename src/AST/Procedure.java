package AST;

/**
 *
 * @author Davy
 */
public class Procedure extends Subroutine {

    public Procedure(String name) {
        this.name = name;
    }
    
    @Override
    public void genC(PW pw){
        pw.print("void " + name + "(");
        if (paramList != null) {
            paramList.genC(pw);
        }
        pw.out.println(") {");
        pw.add();
        if (localVarList != null) {
            localVarList.genC(pw);
        }
        compositeStatement.getStatementList().genC(pw);
        pw.sub();
        pw.println("}");
    }

}

package AST;

/**
 *
 * @author Davy
 */
public class Function extends Subroutine {

    private Type returnType;

    public Function(String name) {
        this.name = name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public void genC(PW pw) {
        if (returnType instanceof StringType) {
            pw.print(returnType.getCname() + "* " + name + "(");
        } else {
            pw.print(returnType.getCname() + " " + name + "(");
        }
        if (paramList != null) {
            paramList.genC(pw);
        }
        pw.out.println(") {");
        pw.add();
        if (localVarList != null) {
            localVarList.genC(pw);
        }
        pw.out.println();
        compositeStatement.getStatementList().genC(pw);
        pw.sub();
        pw.println("}");
    }
}

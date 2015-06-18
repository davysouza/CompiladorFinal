package AST;

/**
 *
 * @author Davy
 */
public class CompositeStatement extends Statement {

    private final StatementList statementList;

    public CompositeStatement(StatementList statementList) {
        this.statementList = statementList;
    }

    public StatementList getStatementList() {
        return statementList;
    }

    @Override
    public void genC(PW pw) {
        //pw.println("{");
        if (statementList != null) {
            pw.add();
            statementList.genC(pw);
            pw.sub();
        }
        //pw.println("}");
    }
}

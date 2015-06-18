package AST;

/**
 *
 * @author Davy
 */
abstract public class Subroutine {

    // fields should be accessible in subclasses
    protected String name;
    protected LocalVarList localVarList;
    protected CompositeStatement compositeStatement;
    protected ParamList paramList;

    abstract public void genC(PW pw);

    public String getName() {
        return name;
    }

    public ParamList getParamList() {
        return paramList;
    }

    public void setLocalVarList(LocalVarList localVarList) {
        this.localVarList = localVarList;
    }

    public void setCompositeStatement(CompositeStatement compositeStatement) {
        this.compositeStatement = compositeStatement;
    }

    public void setParamList(ParamList paramList) {
        this.paramList = paramList;
    }

}

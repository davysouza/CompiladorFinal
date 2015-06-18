package AST;

/**
 *
 * @author Davy
 */
abstract public class NumberExpr extends Expr {

    /**
     *
     * @param pw
     */
    @Override
    abstract public void genC(PW pw, boolean putParenthesis);

    /**
     *
     * @return
     */
    @Override
    abstract public Type getType();
}

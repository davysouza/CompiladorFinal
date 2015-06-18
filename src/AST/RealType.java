package AST;

/**
 *
 * @author Davy
 */
public class RealType extends Type {
    
    public RealType(){
        super("REAL");
    }
    
    @Override
    public String getCname(){
        return "double";
    }
}

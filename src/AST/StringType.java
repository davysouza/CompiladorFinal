package AST;

/**
 *
 * @author Davy
 */
public class StringType extends Type {
    
    public StringType(){
        super("STRING");
    }
    
    @Override
    public String getCname(){
        return "char";
    }
}

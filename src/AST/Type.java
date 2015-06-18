package AST;

/**
 *
 * @author Davy
 */
abstract public class Type {

    private final String name;
    private long size;
    private long ratio;

    public static Type integerType = new IntegerType();
    public static Type realType = new RealType();
    public static Type charType = new CharType();
    public static Type stringType = new StringType();

    public Type(String name) {
        this.name = name;
        this.size = 0;
    }
    
    public void setSize(long size, long ratio){
        this.size = size;
        this.ratio = ratio;
    }

    public String getName() {
        return name;
    }
    
    /**
     *
     * @return
     */
    public long getSize(){
        return size;
    }
    
    public long getRatio(){
        return ratio;
    }

    abstract public String getCname();
}

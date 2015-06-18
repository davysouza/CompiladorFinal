package AST;

/**
 *
 * @author Davy
 */
public class Variable {

    private final String name;
    private Type type;
    private long size;
    private long ratio;
    private long currentSize;

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Variable(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
    public void setRatio(long ratio) {
        this.ratio = ratio;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public long getSize() {
        return size;
    }
    
    public long getRatio() {
        return ratio;
    }
    
    public void setCurrentSize(long currentSize){
        this.currentSize = currentSize;
    }
    
    public long getCurrentSize(){
        return currentSize;
    }
}

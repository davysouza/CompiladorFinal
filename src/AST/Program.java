package AST;

/**
 *
 * @author Davy
 */
public class Program {

    private final String pid;
    private final Body body;

    public Program(String pid, Body body) {
        this.pid = pid;
        this.body = body;
    }
    
    public void genC(PW pw){
        body.genC(pw);
    }
}

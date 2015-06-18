package Compiler;

import Lexer.Lexer;
import java.io.PrintWriter;

/**
 *
 * @author Davy
 */
public class CompilerError {

    private Lexer lexer;
    private final PrintWriter out;
    private boolean thereWasAnError;

    /**
     * Constructor for CompilerError. Initializes @lexer and @out with
     * parameters and @thereWasAnError with false.
     *
     * @param lexer
     * @param out
     */
    public CompilerError(Lexer lexer, PrintWriter out) {
        this.lexer = lexer;
        this.out = out;
        this.thereWasAnError = false;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public boolean wasAnErrorSignalled() {
        return thereWasAnError;
    }

    public void show(String strMessage) {
        show(strMessage, false);
    }

    public void show(String errorType, String strMessage) {
        show(errorType, strMessage, false);
    }
    
    public void show(String strMessage, boolean goPreviousToken) {
        /* 
         is goPreviousToken is true, the error is signalled at the line of the
         previous token, not the last one.
         */
        if (goPreviousToken) {
            out.println("Error at line " + lexer.getLineNumberBeforeLastToken() + ": ");
            out.println(lexer.getLineBeforeLastToken());
        } else {
            out.println("Error at line " + lexer.getLineNumber() + ": ");
            out.println(lexer.getCurrentLine());
        }
        out.println(strMessage);
        out.flush();
        if (out.checkError()) {
            System.out.println("Error in signaling an error");
        }
        thereWasAnError = true;
    }
    
    public void show(String errorType, String strMessage, boolean goPreviousToken) {
        /* 
         is goPreviousToken is true, the error is signalled at the line of the
         previous token, not the last one.
         */
        if (goPreviousToken) {
            out.println(errorType + " error at line " + lexer.getLineNumberBeforeLastToken() + ": ");
            out.println(lexer.getLineBeforeLastToken());
        } else {
            out.println(errorType + " error at line " + lexer.getLineNumber() + ": ");
            out.println(lexer.getCurrentLine());
        }
        out.println(strMessage);
        out.flush();
        if (out.checkError()) {
            System.out.println("Error in signaling an error");
        }
        thereWasAnError = true;
        System.exit(0);
    }

    public void signal(String strMessage) {
        show(strMessage);
        out.flush();
        thereWasAnError = true;
        //throw new RuntimeException();
    }
    
    public void signal(String errorType, String strMessage) {
        show(errorType, strMessage);
        out.flush();
        thereWasAnError = true;
        System.exit(0);
        //throw new RuntimeException();
    }
}

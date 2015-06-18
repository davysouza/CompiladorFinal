package Lexer;

import Compiler.CompilerError;
import java.util.HashMap;

/**
 *
 * @author Davy
 */
public class Lexer {

    // current token
    public int token;

    private String stringValue;
    private int numberValue;
    private char charValue;
    private int tokenPos;

    // input[lastTokenPos] is the last character of the last token found
    private int lastTokenPos;

    // input[beforeLastTokenPos] is the last character of the token before the last token found
    private int beforeLastTokenPos;

    // program given as input - source code
    private final char[] input;

    // number of current line. Starts with 1
    private int lineNumber;

    private final CompilerError error;
    private static final int MaxValueInteger = 2147483647;

    /**
     * Constructor. @input receives string input from @args[0] and @error
     * receives CompilerError object.
     *
     * @param input
     * @param error
     */
    public Lexer(char[] input, CompilerError error) {
        this.input = input;

        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';

        // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        lastTokenPos = 0;
        beforeLastTokenPos = 0;
        this.error = error;
    }

    // contains the keywords
    private static final HashMap keywordsTable;

    static {
        keywordsTable = new HashMap();
        keywordsTable.put("PROGRAM", Symbol.PROGRAM);
        keywordsTable.put("PROCEDURE", Symbol.PROCEDURE);
        keywordsTable.put("FUNCTION", Symbol.FUNCTION);
        keywordsTable.put("BEGIN", Symbol.BEGIN);
        keywordsTable.put("END", Symbol.END);
        keywordsTable.put("VAR", Symbol.VAR);
        keywordsTable.put("INTEGER", Symbol.INTEGER);
        keywordsTable.put("REAL", Symbol.REAL);
        keywordsTable.put("CHAR", Symbol.CHAR);
        keywordsTable.put("STRING", Symbol.STRING);
        keywordsTable.put("ARRAY", Symbol.ARRAY);
        keywordsTable.put("OF", Symbol.OF);
        keywordsTable.put("IF", Symbol.IF);
        keywordsTable.put("THEN", Symbol.THEN);
        keywordsTable.put("ELSE", Symbol.ELSE);
        keywordsTable.put("ENDIF", Symbol.ENDIF);
        keywordsTable.put("WHILE", Symbol.WHILE);
        keywordsTable.put("DO", Symbol.DO);
        keywordsTable.put("ENDWHILE", Symbol.ENDWHILE);
        keywordsTable.put("READ", Symbol.READ);
        keywordsTable.put("WRITE", Symbol.WRITE);
        keywordsTable.put("WRITELN", Symbol.WRITELN);
        keywordsTable.put("RETURN", Symbol.RETURN);
        keywordsTable.put("TRUE", Symbol.TRUE);
        keywordsTable.put("FALSE", Symbol.FALSE);
        keywordsTable.put("AND", Symbol.AND);
        keywordsTable.put("OR", Symbol.OR);
        keywordsTable.put("NOT", Symbol.NOT);
        keywordsTable.put("MOD", Symbol.MOD);
        keywordsTable.put("DIV", Symbol.DIV);
        
    }

    public void skipBraces() {
        // skip any of the symbols [ ] { }
        if (token == Symbol.LEFTCURLYBRACKET || token == Symbol.RIGHTCURLYBRACKET
                || token == Symbol.LEFTBRACKET || token == Symbol.RIGHTBRACKET) {
            nextToken();
        }
        if (token == Symbol.EOF) {
            error.signal("Unexpected EOF");
        }
    }

    public void skipPunctuation() {
        // skip any punctuation symbols
        while (token != Symbol.EOF
                && (token == Symbol.COLON || token == Symbol.COMMA || token == Symbol.SEMICOLON)) {
            nextToken();
        }
        if (token == Symbol.EOF) {
            error.signal("Unexpected EOF");
        }
    }

    public void skipTo(int[] arraySymbol) {
        // skip till one of the characters of arraySymbol appears in the input
        while (token != Symbol.EOF) {
            int i = 0;
            while (i < arraySymbol.length) {
                if (token == arraySymbol[i]) {
                    return;
                } else {
                    i++;
                }
            }
            nextToken();
        }
        if (token == Symbol.EOF) {
            error.signal("Unexpected EOF");
        }
    }

    public void skipToNextStatement() {
        while (token != Symbol.EOF 
                && token != Symbol.ELSE && token != Symbol.ENDIF
                && token != Symbol.END && token != Symbol.SEMICOLON) {
            nextToken();
        }
        if (token == Symbol.SEMICOLON) {
            nextToken();
        }
    }

    /**
     * Go to the next token found in @input.
     */
    public void nextToken() {
        char ch = input[tokenPos];

        while (ch == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {
            // count the number of lines
            if (ch == '\n') {
                lineNumber++;
            }
            tokenPos++;
            ch = input[tokenPos];
        }

        if (ch == '\0') {
            token = Symbol.EOF;
        } else {
            if (input[tokenPos] == '{') {
                // comment found
                while (input[tokenPos] != '\0' && input[tokenPos] != '}') {
                    tokenPos++;
                }
                tokenPos++;
                nextToken();
            } else {
                if (Character.isLetter(ch)) {

                    // get an identifier or keyword
                    StringBuilder ident = new StringBuilder();
                    while (Character.isLetterOrDigit(input[tokenPos])) {
                        ident.append(input[tokenPos]);
                        tokenPos++;
                    }

                    stringValue = ident.toString();

                    // if identStr is in the list of keywords, it is a keyword !
                    Object value = keywordsTable.get(stringValue);
                    if (value == null) {
                        token = Symbol.IDENT;
                    } else {
                        token = ((Integer) value);
                    }
                } else if (Character.isDigit(ch)) {

                    // get a number
                    StringBuilder number = new StringBuilder();
                    while (Character.isDigit(input[tokenPos])) {
                        number.append(input[tokenPos]);
                        tokenPos++;
                    }
                    token = Symbol.NUMBER;

                    try {
                        numberValue = Integer.parseInt(number.toString());
                    } catch (NumberFormatException e) {
                        error.signal("Semantic", "Number out of limits.");
                    }
                    if (numberValue >= MaxValueInteger) {
                        error.signal("Semantic", "Number out of limits.");
                    }
                } else {
                    tokenPos++;

                    switch (ch) {
                        case '+':
                            token = Symbol.PLUS;
                            break;
                        case '-':
                            token = Symbol.MINUS;
                            break;
                        case '*':
                            token = Symbol.MULT;
                            break;
                        case '/':
                            token = Symbol.REALDIV;
                            break;
                        case '=':
                            token = Symbol.EQ;
                            break;
                        case ':':
                            if (input[tokenPos] == '=') {
                                tokenPos++;
                                token = Symbol.ASSIGN;
                            } else {
                                token = Symbol.COLON;
                            }
                            break;
                        case '<':
                            if (input[tokenPos] == '=') {
                                tokenPos++;
                                token = Symbol.LE;
                            } else if (input[tokenPos] == '>') {
                                tokenPos++;
                                token = Symbol.NEQ;
                            } else {
                                token = Symbol.LT;
                            }
                            break;
                        case '>':
                            if (input[tokenPos] == '=') {
                                tokenPos++;
                                token = Symbol.GE;
                            } else {
                                token = Symbol.GT;
                            }
                            break;
                        case '(':
                            token = Symbol.LEFTPAR;
                            break;
                        case ')':
                            token = Symbol.RIGHTPAR;
                            break;
                        case '[':
                            token = Symbol.LEFTBRACKET;
                            break;
                        case ']':
                            token = Symbol.RIGHTBRACKET;
                            break;
                        case ',':
                            token = Symbol.COMMA;
                            break;
                        case ';':
                            token = Symbol.SEMICOLON;
                            break;
                        case '.':
                            if (input[tokenPos] == '.') {
                                tokenPos++;
                                token = Symbol.DOUBLEDOT;
                            } else {
                                token = Symbol.DOT;
                            }
                            break;
                        case '\'':
                            token = Symbol.CHARACTER;
                            stringValue = "";
                            while (input[tokenPos] != '\'') {
                                if (input[tokenPos] == '\0') {
                                    error.signal("Syntatic", "\"'\" expected");
                                }
                                stringValue = stringValue + String.valueOf(input[tokenPos]);
                                tokenPos++;
                            }
                            if (input[tokenPos] != '\'') {
                                error.signal("Lexical", "Illegal literal character" + input[tokenPos - 1]);
                            }
                            tokenPos++;
                            break;
                        default:
                            error.signal("Lexical", "Invalid Character: '" + ch + "'");
                    }
                }
            }
        }
        beforeLastTokenPos = lastTokenPos;
        lastTokenPos = tokenPos - 1;
    }

    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineNumberBeforeLastToken() {
        return getLineNumber(beforeLastTokenPos);
    }

    private int getLineNumber(int index) {
        // return the line number in which the character input[index] is
        int i = 0;
        int n = 1;
        int size = input.length;
        while (i < size && i < index) {
            if (input[i] == '\n') {
                n++;
            }
            i++;
        }
        return n;
    }

    public String getCurrentLine() {
        return getLine(lastTokenPos);
    }

    public String getLineBeforeLastToken() {
        return getLine(beforeLastTokenPos);
    }

    private String getLine(int index) {
        /* get the line that contains input[index]. Assume input[index] is at a token, not
         a white space or newline */
        int i = index;
        if (i == 0) {
            i = 1;
        } else if (i >= input.length) {
            i = input.length;
        }
        StringBuilder line = new StringBuilder();
        // go to the beginning of the line
        while (i >= 1 && input[i] != '\n') {
            i--;
        }
        if (input[i] == '\n') {
            i++;
        }
        // go to the end of the line putting it in variable line
        while (input[i] != '\0' && input[i] != '\n' && input[i] != '\r') {
            line.append(input[i]);
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public char getCharValue() {
        return charValue;
    }
}

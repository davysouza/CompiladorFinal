/*

    3rd Phase - Compiler

    Grammar:

        program ::= PROGRAM pid ';' body '.'
        body ::= [declarationPart] compositeStatement
        
        declarationPart ::= VAR dclList [subList] | subList
        dclList ::= declaration {declaration}
        declaration ::= idList ':' type ';'
        idList ::= id {',' id}

        type ::= stdType | arrayType
        stdType ::= INTEGER | REAL | CHAR | STRING
        arrayType ::= ARRAY '[' intnum '..' intnum ']' OF stdtype

        subList ::= subDeclaration {subDeclaration}
        subDeclaration ::= subHead ';' subBody ';'
        subHead ::= FUNCTION pid args ':' stdType | PROCEDURE pid args
        subBody ::= [localVarDec] compositeStatement
        localVarDec ::= varDecList2 {varDecList2}
        varDecList2 ::= id {',' id} ':' type ';'        
        args ::= '(' [paramList] ')'
        paramList ::= paramDec {paramDec}
        paramDec ::= Ident { ',' Ident } ':' Type
        

        compositeStatement ::= BEGIN statementList END

        statementList ::= statement {';' statement} ';'
        statement ::= ifstmt | whilestmt | assignstmt | compstmt | readstmt
                 | writestmt | writelnstmt | returnstmt | procedureCall

        ifstmt ::= IF expr THEN statementList [ELSE statementList] ENDIF
        whilestmt ::= WHILE expr DO statementList ENDWHILE
        assignstmt ::= variable ':=' expr
        readstmt ::= READ '(' vblist ')'
        writestmt ::= WRITE '(' exprList ')'
        writelnstmt ::= WRITELN '(' [exprList] ')'
        returnstmt ::= RETURN [expr]
        procedureCall ::= pid '(' [exprList] ')'
        
        vblist ::= variable {',' variable}
        variable ::= id ['[' expr ']']

        exprList ::= expr {',' expr}
        expr ::= simexp [relop expr]
        simexp ::= [unary] term {addop term}
        term ::= factor {mulop factor}
        factor ::= variable | number | '(' expr ')' | '"'.'"' | functionCall
        number ::= ['+' | '-'] {digit} ['.'] {digit}
        functionCall ::= pid '(' [exprlist] ')'

        id ::= letter {letter | digit}
        pid ::= letter {letter | digit}
        intnum ::= digit {digit}
        relop ::= '=' | '<' | '>' | '<=' | '>=' | '<>'
        addop ::= '+' | '-' | OR
        mulop ::= '*' | '/' | AND | MOD | DIV
        unary ::= '+' | '-' | NOT

*/
package Compiler;

import AST.*;
import Lexer.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Davy
 */
public class Compiler {

    private SymbolTable symbolTable;
    private Lexer lexer;
    private CompilerError error;

    // keeps a pointer to the current function being compiled
    private Function currentFunction;
    private boolean notReturn;

    // compile must receive an input with an character less than p_input.lenght
    public Program compile(char[] input, PrintWriter outError) {

        symbolTable = new SymbolTable();
        error = new CompilerError(lexer, new PrintWriter(outError));
        lexer = new Lexer(input, error);
        error.setLexer(lexer);

        lexer.nextToken();
        Program p = program();
        
        if (error.wasAnErrorSignalled()) {
            return null;
        } else {
            return p;
        }
    }

    // prog ::= PROGRAM pid ';' body '.'
    private Program program() {
        if (lexer.token != Symbol.PROGRAM) {
            error.signal("Syntatic", "\"PROGRAM\" expected.");
        }
        lexer.nextToken();

        if (lexer.token != Symbol.IDENT) {
            error.signal("Syntatic", "Program identifier (PID) expected after \"PROGRAM\".");
        }
        // Pid Identifier name
        String pid;
        pid = lexer.getStringValue();
        lexer.nextToken();

        if (lexer.token != Symbol.SEMICOLON) {
            error.show("Syntatic", "\";\" expected after program identifier (PID).");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }

        Body body = body();

        if (lexer.token != Symbol.DOT) {
            error.show("Syntatic", "\".\" expected at the end of the program.");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }

        return new Program(pid, body);
    }

    // body ::= declarationPart compositeStatement
    private Body body() {
        return new Body(declarationPart(), compositeStatement());
    }

    // declarationPart ::= [VAR dclList [subList] | subList]
    private DeclarationPart declarationPart() {
        ArrayList<Variable> dclList = new ArrayList();
        ArrayList<Subroutine> subList = new ArrayList();

        if (lexer.token == Symbol.VAR) {
            lexer.nextToken();
            dclList = dclList();

            if (lexer.token == Symbol.FUNCTION || lexer.token == Symbol.PROCEDURE) {
                subList = subList();
            }
        } else if (lexer.token == Symbol.FUNCTION || lexer.token == Symbol.PROCEDURE) {
            subList = subList();
        }

        return new DeclarationPart(dclList, subList);
    }

    // dclList ::= declaration {decalration}
    private ArrayList<Variable> dclList() {
        ArrayList<Variable> varList = new ArrayList();
        declaration(varList);
        while (lexer.token == Symbol.IDENT) {
            declaration(varList);
        }
        return varList;
    }

    // declaration ::= idlist ':' type ';'
    private void declaration(ArrayList<Variable> varList) {
        ArrayList lastVarList = new ArrayList();

        while (true) {
            if (lexer.token != Symbol.IDENT) {
                error.signal("Syntatic", "Identifier expected after \"VAR\".");
            }

            // Name of the Identifier
            String name = lexer.getStringValue();
            lexer.nextToken();

            // Semantic Analysis
            // if the name is in the symbol table, the variable is been declared twice.
            if (symbolTable.getInGlobal(name) != null) {
                error.show("Semantic", "Variable " + name + " has already been declared.");
            }

            // variable does not have a type yet
            Variable v = new Variable(name);

            // inserts the variable in the symbolTable. The name is the key and an
            // object of class Variable is the value. Hash tables store a pair (key, value)
            // retrieved by the key.
            symbolTable.putInGlobal(name, v);

            // list of the last variables declared. They don't have types yet
            lastVarList.add(v);
            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            } else {
                break;
            }
        }

        if (lexer.token != Symbol.COLON) {
            error.show("Syntatic", "\":\" expected after list of identifiers.");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }

        // get the type
        Type typeVar = type();
        Variable v;
        Enumeration e = Collections.enumeration(lastVarList);
        while (e.hasMoreElements()) {

            // add type to the variable
            v = (Variable) e.nextElement();
            v.setType(typeVar);
            v.setSize(typeVar.getSize());
            v.setRatio(typeVar.getRatio());

            // add variable to the list of variable
            varList.add(v);
        }
        if (lexer.token != Symbol.SEMICOLON) {
            error.show("Syntatic", "\";\" expected after variable type.");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }
    }

    // type ::= stdtype | arraytype
    private Type type() {
        if (lexer.token == Symbol.ARRAY) {
            lexer.nextToken();
            return arrayType();
        }
        return stdType();
    }

    // stdtype ::= INTEGER | REAL | CHAR | STRING
    private Type stdType() {
        Type result;
        switch (lexer.token) {
            case Symbol.INTEGER:
                result = Type.integerType;
                break;
            case Symbol.REAL:
                result = Type.realType;
                break;
            case Symbol.CHAR:
                result = Type.charType;
                break;
            case Symbol.STRING:
                result = Type.stringType;
                break;
            default:
                error.show("Syntatic", "Type expected after \":\".");
                result = null;
        }
        lexer.nextToken();
        if (result != null) {
            result.setSize(0, 0);
        }
        return result;
    }

    // arraytype ::= ARRAY '[' intnum '..'  intnum ']' OF stdtype
    private Type arrayType() {
        if (lexer.token != Symbol.LEFTBRACKET) {
            error.show("Syntatic", "\"[\" expected after \"ARRAY\" statement.");
            lexer.skipBraces();
        } else {
            lexer.nextToken();
        }

        if (lexer.token != Symbol.NUMBER) {
            error.show("Syntatic", "Number expected after token \"[\".");
        }
        long n1 = lexer.getNumberValue();
        lexer.nextToken();

        if (lexer.token != Symbol.DOUBLEDOT) {
            error.show("Syntatic", "\"..\" expected after number " + n1 + ".");
        }
        lexer.nextToken();

        if (lexer.token != Symbol.NUMBER) {
            error.show("Syntatic", "Number expected after token \"..\".");
        }
        long n2 = lexer.getNumberValue();
        if (n2 - n1 < 0) {
            error.show("Semantic", "Array Size = " + (n2 - n1) + ".\nThe array size must be greater than 0.");
        }
        lexer.nextToken();

        if (lexer.token != Symbol.RIGHTBRACKET) {
            error.show("Syntatic", "\"]\" expected after number " + n2 + ".");
            lexer.skipBraces();
        } else {
            lexer.nextToken();
        }

        if (lexer.token != Symbol.OF) {
            error.show("Syntatic", "OF expected after token \"]\".");
        }
        lexer.nextToken();

        long size = n2 - n1 + 1;

        Type stdtype = stdType();
        stdtype.setSize(size, n1);

        return stdtype;
    }

    // subList ::= subDeclaration {subDeclaration}
    private ArrayList<Subroutine> subList() {
        ArrayList<Subroutine> subList = new ArrayList();
        subDeclaration(subList);
        while (lexer.token == Symbol.PROCEDURE || lexer.token == Symbol.FUNCTION) {
            subDeclaration(subList);
        }
        return subList;
    }

    // subDeclaration ::= subHead ';' subBody ';'
    // subHead::= FUNCTION pid args ':' stdtype | PROCEDURE pid args
    // args   ::= '(' [paramList] ')'
    private void subDeclaration(ArrayList<Subroutine> subList) {
        boolean isFunction;

        if (lexer.token == Symbol.PROCEDURE) {
            isFunction = false;
        } else if (lexer.token == Symbol.FUNCTION) {
            isFunction = true;
            notReturn = true;
        } else {
            // should never occur
            error.signal("Internal compiler error.");
            return;
        }
        lexer.nextToken();

        if (lexer.token != Symbol.IDENT) {
            if (isFunction) {
                error.signal("Syntatic", "Identifier expected after token \"FUNCTION\".");
            } else {
                error.signal("Syntatic", "Identifier expected after token \"PROCEDURE\".");
            }
        }
        String name = (String) lexer.getStringValue();
        /* 
         Symbol table now searches for an identifier in the scope order. First
         the local variables and parameters and then the procedures and functions.
         at this point, there should not be any local variables/parameters in the
         symbol table.
         */
        Subroutine s = (Subroutine) symbolTable.getInGlobal(name);

        // semantic analysis
        // identifier is in the symbol table
        if (s != null) {
            error.show("Semantic", "Subroutine " + name + " has already been declared");
        }
        lexer.nextToken();

        if (isFunction) {
            // currentFunction is used to store the function being compiled or null if it is a procedure
            currentFunction = new Function(name);
            s = currentFunction;
        } else {
            currentFunction = null;
            s = new Procedure(name);
        }

        // insert s in the symbol table
        symbolTable.putInGlobal(name, s);

        if (lexer.token != Symbol.LEFTPAR) {
            if (isFunction) {
                error.show("Syntatic", "\"(\" expected after function identifier.");
            } else {
                error.show("Syntatic", "\"(\" expected after procedure identifier.");
            }
            lexer.skipBraces();
        } else {
            lexer.nextToken();
        }

        s.setParamList(paramList());
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show("Syntatic", "\")\" expected to close parameter list.");
            lexer.skipBraces();
        } else {
            lexer.nextToken();
        }

        if (isFunction) {
            if (lexer.token != Symbol.COLON) {
                error.show("Syntatic", "\":\" expected after parameter list of function");
                lexer.skipPunctuation();
            } else {
                lexer.nextToken();
            }
            ((Function) s).setReturnType(stdType());
        }

        if (lexer.token != Symbol.SEMICOLON) {
            error.show("Syntatic", "\";\" expected after subroutine head");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }

        if (lexer.token == Symbol.VAR) {
            s.setLocalVarList(localVarDec());
        }

        s.setCompositeStatement(compositeStatement());
        
        if(isFunction && notReturn){
            error.show("Semantic", "Return expected.");
        }
        
        if (lexer.token != Symbol.SEMICOLON) {
            error.show("Syntatic", "\";\" expected after subroutine");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }

        subList.add(s);
        
        symbolTable.removeLocalIdent();
    }

    // localVarDec ::= varDecList2 {varDecList2}
    private LocalVarList localVarDec() {
        LocalVarList localVarList = new LocalVarList();

        lexer.nextToken(); // eat token "var"
        varDecList2(localVarList);
        while (lexer.token == Symbol.IDENT) {
            varDecList2(localVarList);
        }
        return localVarList;
    }

    // varDecList2 ::= id {',' id} ':' type ';'
    private void varDecList2(LocalVarList localVarList) {
        ArrayList<Variable> lastVarList = new ArrayList();

        while (true) {
            if (lexer.token != Symbol.IDENT) {
                error.signal("Syntatic", "Identifier expected");
            }

            // name of the identifier
            String name = (String) lexer.getStringValue();
            lexer.nextToken();

            // semantic analysis
            // if the name is in the symbol table and the scope of the name is local,
            // the variable is been declared twice.
            if (symbolTable.getInLocal(name) != null) {
                error.show("Semantic", "Variable " + name + " has already been declared");
            }

            // variable does not have a type yet
            Variable v = new Variable(name);

            // inserts the variable in the symbol table. The name is the key and an
            // object of class Variable is the value. Hash tables store a pair (key, value)
            // retrieved by the key.
            symbolTable.putInLocal(name, v);

            // list of the last variables declared. They don't have types yet
            lastVarList.add(v);
            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            } else {
                break;
            }
        }
        if (lexer.token != Symbol.COLON) {
            error.show("Syntatic", "\":\" expected after variables list");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }

        // get the type
        Type typeVar = type();
        Variable v;
        Enumeration e = Collections.enumeration(lastVarList);
        while (e.hasMoreElements()) {
            // add type to the variable
            v = (Variable) e.nextElement();
            v.setType(typeVar);
            v.setSize(typeVar.getSize());
            v.setRatio(typeVar.getRatio());
            // add variable to the list of variable declarations
            localVarList.add(v);
        }
        if (lexer.token != Symbol.SEMICOLON) {
            error.show("Syntatic", "\";\" expected after declarations");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }
    }

    // paramList ::= paramDec {paramDec}
    private ParamList paramList() {
        ParamList paramList = null;
        if (lexer.token == Symbol.IDENT) {
            paramList = new ParamList();
            paramDec(paramList);
            while (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();
                paramDec(paramList);
            }
        }
        return paramList;
    }

    // paramDec ::= Ident { ',' Ident } ':' Type
    private void paramDec(ParamList paramList) {
        ArrayList<Parameter> lastVarList = new ArrayList();

        while (true) {
            if (lexer.token != Symbol.IDENT) {
                error.signal("Syntatic", "Identifier expected after token \"(\".");
            }

            // name of the identifier
            String name = (String) lexer.getStringValue();
            lexer.nextToken();

            // semantic analysis
            // if the name is in the symbol table and the scope of the name is local,
            // the variable is been declared twice.
            if (symbolTable.getInLocal(name) != null) {
                error.show("Semantic", "Parameter " + name + " has already been declared");
            }

            // variable does not have a type yet
            Parameter v = new Parameter(name);

            // inserts the variable in the symbol table. The name is the key and an
            // object of class Variable is the value. Hash tables store a pair (key, value)
            // retrieved by the key.
            symbolTable.putInLocal(name, v);

            // list of the last variables declared. They don't have types yet
            lastVarList.add(v);
            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            } else {
                break;
            }
        }
        if (lexer.token != Symbol.COLON) {
            error.show("Syntatic", "\":\" expected after identifiers list");
            lexer.skipPunctuation();
        } else {
            lexer.nextToken();
        }

        // get the type
        Type typeVar = type();
        Parameter v;
        Enumeration e = Collections.enumeration(lastVarList);
        while (e.hasMoreElements()) {
            // add type to the variable
            v = (Parameter) e.nextElement();
            v.setType(typeVar);
            v.setSize(typeVar.getSize());
            v.setRatio(typeVar.getRatio());
            // add v to the list of parameter declarations
            paramList.add(v);
        }
    }

    // compositeStatement ::= BEGIN stmts END
    private CompositeStatement compositeStatement() {
        if (lexer.token != Symbol.BEGIN) {
            if (lexer.token == Symbol.FUNCTION || lexer.token == Symbol.PROCEDURE) {
                error.signal("Semantic", "Nested subroutines are not allowed.");
            } else {
                error.signal("Syntatic", "\"BEGIN\" expected.");
            }
            lexer.skipToNextStatement();
        } else {
            lexer.nextToken();
        }
        StatementList sl = statementList();
        if (lexer.token != Symbol.END) {
            error.show("Syntatic", "\"END\" expected.");
        } else {
            lexer.nextToken();
        }
        return new CompositeStatement(sl);
    }

    // statementList ::= statement {';' statement} ';'
    private StatementList statementList() {
        ArrayList v = new ArrayList();

        // statements always begin with an identifier, if, while, read, write or writeln
        while (lexer.token == Symbol.IDENT || lexer.token == Symbol.IF
                || lexer.token == Symbol.WHILE || lexer.token == Symbol.READ
                || lexer.token == Symbol.WRITE || lexer.token == Symbol.WRITELN
                || lexer.token == Symbol.RETURN) {
            v.add(statement());
            if (lexer.token != Symbol.SEMICOLON) {
                error.show("Syntatic", "\";\" expected at the end of each statement.");
                lexer.skipPunctuation();
            } else {
                lexer.nextToken();
            }
        }
        return new StatementList(v);
    }

    // stmt ::= ifstmt | whilestmt | assignstmt | compstmt | readstmt | writestmt | writelnstmt | 
    //          returnstmt | subroutinestmt
    private Statement statement() {
        switch (lexer.token) {
            case Symbol.IDENT:
                // if the identifier is in the symbol table, "symbolTable.get(...)"
                // will return the corresponding object. If it is a procedure,
                // we should call procedureCall(). Otherwise we have an assignment

                String name = lexer.getStringValue();
                // Is the identifier a procedure or function?
                if (symbolTable.get(name) instanceof Function) {
                    error.show("Semantic", "The value returned by function always must be assigned to a variable.");
                } else if (symbolTable.get(name) instanceof Procedure) {
                    return procedureCall();
                } else {
                    return assignmentStatement();
                }
            case Symbol.IF:
                return ifStatement();
            case Symbol.WHILE:
                return whileStatement();
            case Symbol.READ:
                return readStatement();
            case Symbol.WRITE:
                return writeStatement();
            case Symbol.WRITELN:
                return writelnStatement();
            case Symbol.RETURN:
                return returnStatement();
            default:
                // will never be executed
                error.show("Syntatic", "Statement expected");
        }
        return null;
    }

    // assignmentStatement ::= variable ':=' expression
    private AssignmentStatement assignmentStatement() {
        Variable v = variable();

        if (lexer.token != Symbol.ASSIGN) {
            error.show("Syntatic", "\":=\" expected after variable in Assignment Statement.");
            lexer.skipToNextStatement();
        } else {
            lexer.nextToken();
        }

        Expr right = expr();

        // semantic analysis
        // check if expression has the same type as variable
        if (v.getType() != right.getType()) {
            if (v.getType() == Type.integerType || v.getType() == Type.charType
                    || (right.getType() == Type.integerType && v.getType() != Type.realType)
                    || (right.getType() == Type.charType && v.getType() != Type.stringType)
                    || right.getType() == Type.realType || right.getType() == Type.stringType) {
                error.show("Semantic", "Type error in assignment:\n A " + v.getType().getName()
                        + " variable cannot receive a " + right.getType().getName() + " value.");
            }
        }

        return new AssignmentStatement(v, right, v.getCurrentSize(), v.getRatio());
    }

    // variable ::= id ['[' expr ']']
    private Variable variable() {
        String name = (String) lexer.getStringValue();
        /*
         * is the variable in the symbol table? Variables are inserted in the
         * symbol table when they are declared. It the variable is not there, it has
         * not been declared.
         */
        Variable v = (Variable) symbolTable.get(name);

        // was it in the symbol table?
        if (v == null) {
            error.show("Semantic", "Variable " + name + " was not declared");
        }
        lexer.nextToken();
        
        if (v != null && v.getSize() > 0) {
            if (lexer.token == Symbol.LEFTBRACKET) {
                lexer.nextToken();
                if (lexer.token != Symbol.NUMBER) {
                    error.show("Syntatic", "Number expected after \"[\".");
                    lexer.skipBraces();
                } else {
                    long n = lexer.getNumberValue();
                    long range = n - v.getRatio();
                    if (range >= v.getSize() || range < 0) {
                        error.show("Semantic", "Index out of range");
                    }
                    v.setCurrentSize(n);
                    lexer.nextToken();

                    if (lexer.token != Symbol.RIGHTBRACKET) {
                        error.show("Syntatic", "\"]\" expected after" + n + ".");
                        lexer.skipBraces();
                    } else {
                        lexer.nextToken();
                    }
                }
            } else {
                error.show("Semantic", "Invalid assignment between ARRAY and " + v.getType().getName() + ".");
            }
        }
        return v;
    }

    // expr  ::= simexp [relop expr]
    // relop ::= '=' | '<' | '>' | '<=' | '>=' | '<>'
    private Expr expr() {
        Expr left, right;
        left = simpleExpr();

        int relOp = lexer.token;
        if (relOp == Symbol.EQ || relOp == Symbol.LT || relOp == Symbol.GT
                || relOp == Symbol.LE || relOp == Symbol.GE || relOp == Symbol.NEQ) {
            lexer.nextToken();
            right = expr();

            // semantic analysis
            if (left.getType() == Type.integerType || left.getType() == Type.realType) {
                if (right.getType() != Type.integerType && right.getType() != Type.realType) {
                    error.show("Semantic", "Type error in expression:\nA \"" + left.getType().getName()
                            + "\" expression cannot be compared with \"" + right.getType().getName()
                            + "\" expression.");
                }
            } else {
                if (left.getType() != right.getType()) {
                    error.show("Semantic", "Type error in expression:\nA \"" + left.getType().getName()
                            + "\" expression cannot be compared with \"" + right.getType().getName()
                            + "\" expression.");
                }
            }

            return new CompositeExpr(left, relOp, right);
        }

        return left;
    }

    // simpleExpr ::= [unary] term {addop term}
    // unary  ::= '+' | '-' | NOT
    // addop  ::= '+' | '-' | OR
    private Expr simpleExpr() {

        Expr left, right;

        int unaryOp = lexer.token;
        if (unaryOp == Symbol.PLUS || unaryOp == Symbol.MINUS || unaryOp == Symbol.NOT) {
            lexer.nextToken();
        }

        left = term();

        int addOp = lexer.token;
        while (addOp == Symbol.PLUS || addOp == Symbol.MINUS || addOp == Symbol.OR) {
            lexer.nextToken();
            right = term();

            // semantic analysis
            if ((left.getType() != Type.integerType && left.getType() != Type.realType)
                    || (right.getType() != Type.integerType && right.getType() != Type.realType)) {
                error.show("Semantic", "Expression of type \"INTEGER\" or \"REAL\" expected.");
            }
            left = new CompositeExpr(left, addOp, right);

            addOp = lexer.token;
        }
        if (unaryOp == Symbol.PLUS || unaryOp == Symbol.MINUS || unaryOp == Symbol.NOT) {
            return new UnaryExpr(left, unaryOp);
        }
        return left;
    }

    // term  ::= factor {mulop factor}
    // mulop ::= '*' | '/' | AND | MOD | DIV
    private Expr term() {
        Expr left, right;

        left = factor();

        int multOp = lexer.token;
        while (multOp == Symbol.MULT || multOp == Symbol.REALDIV || multOp == Symbol.AND
                || multOp == Symbol.MOD || multOp == Symbol.DIV) {
            lexer.nextToken();
            right = factor();

            // semantic analysis
            if (multOp == Symbol.DIV || multOp == Symbol.MOD) {
                if (left.getType() != Type.integerType || right.getType() != Type.integerType) {
                    error.show("Semantic", "Expression of type \"INTEGER\" expected.");
                }
            } else if ((left.getType() != Type.integerType && left.getType() != Type.realType)
                    || (right.getType() != Type.integerType && right.getType() != Type.realType)) {
                error.show("Semantic", "Expression of type \"INTEGER\" or \"REAL\" expected.");
            }

            left = new CompositeExpr(left, multOp, right);
            multOp = lexer.token;
        }

        return left;
    }

    // factor ::= variable | number | '(' expr ')' | '"'.'"' | subroutinestmt
    private Expr factor() {
        Expr e;

        switch (lexer.token) {
            case Symbol.IDENT:
                long n = 0;
                // an identifier
                if (lexer.token != Symbol.IDENT) {
                    error.show("Syntatic", "Identifier expected.");
                }
                String name = (String) lexer.getStringValue();

                if (symbolTable.get(name) instanceof Procedure) {
                    error.show("Semantic", "Procedures don't return any value.");
                } else if (symbolTable.get(name) instanceof Function) {
                    return functionCall();
                }
                lexer.nextToken();
                Variable v = (Variable) symbolTable.get(name);

                // semantic analysis
                // was the variable declared?
                if (v == null) {
                    error.show("Semantic", "Variable " + name + " was not declared");
                }
                
                if (v != null && v.getSize() > 0) {
                    if (lexer.token == Symbol.LEFTBRACKET) {
                        lexer.nextToken();
                        if (lexer.token != Symbol.NUMBER) {
                            error.signal("Syntatic", "Number expected after \"[\".");
                        }
                        n = lexer.getNumberValue();
                        long range = n - v.getRatio();
                        if (range >= v.getSize() || range < 0) {
                            error.signal("Semantic", "Index out of range");
                        }
                        lexer.nextToken();

                        if (lexer.token != Symbol.RIGHTBRACKET) {
                            error.signal("Syntatic", "\"]\" expected after " + n + ".");
                        }
                        lexer.nextToken();
                    } else {
                        error.signal("Semantic", "Invalid assignment between ARRAY and " + v.getType().getName() + ".");
                    }
                }
                
                return new VariableExpr(v, n, v.getRatio());
            case Symbol.NUMBER:
                return number();
            case Symbol.LEFTPAR:
                lexer.nextToken();
                e = expr();
                if (lexer.token != Symbol.RIGHTPAR) {
                    error.show("Syntatic", "\")\" expected after exrpession.");
                }
                lexer.nextToken();
                return e;                
            default:
                if (lexer.token != Symbol.CHARACTER) {
                    error.show("Syntatic","Unexpected error.");
                }
                lexer.nextToken();
                // get the token with getToken.
                // then get the value of it, with has the type Object
                // convert the object to type Character using a cast
                // call method charValue to get the character inside the object

                String res = lexer.getStringValue();
                char[] ch = res.toCharArray();

                if (res.length() == 1) {
                    return new CharExpr(ch[0]);
                } else if (res.length() > 1) {
                    return new StringExpr(res);
                }
        }
        return null;
    }

    // number ::= ['+' | '-'] digit ['.']  {digit}
    private NumberExpr number() {
        NumberExpr e = null;

        // the number value is stored in lexer.getToken().value as an object of Integer.
        // Method intValue returns that value as an value of type int.
        long value = lexer.getNumberValue();
        lexer.nextToken();

        if (lexer.token == Symbol.DOT) {
            lexer.nextToken();

            if (lexer.token != Symbol.NUMBER) {
                error.signal("Syntatic", "Number expected.");
            }
            lexer.nextToken();

            long value2 = lexer.getNumberValue();

            String v = Long.toString(value) + "." + Long.toString(value2);
            double val = Double.parseDouble(v);

            return new RealExpr(val);
        }
        return new IntegerExpr(value);
    }

    // ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
    private IfStatement ifStatement() {
        lexer.nextToken();
        Expr e = expr();

        if (lexer.token != Symbol.THEN) {
            error.signal("Syntatic", "THEN expected after expression \"" + e.getType().getName() + "\".");
        }
        lexer.nextToken();

        StatementList thenPart = statementList();

        StatementList elsePart = null;
        if (lexer.token == Symbol.ELSE) {
            lexer.nextToken();
            elsePart = statementList();
        }
        if (lexer.token != Symbol.ENDIF) {
            error.signal("Syntatic", "\"" + lexer.getStringValue() + "\" found instead of \"ENDIF\".");
        }
        lexer.nextToken();
        return new IfStatement(e, thenPart, elsePart);
    }

    // whilestmt ::= WHILE expr DO stmts ENDWHILE
    private WhileStatement whileStatement() {
        lexer.nextToken();

        Expr e = expr();

        if (lexer.token != Symbol.DO) {
            error.signal("Syntatic", "DO expected after expression \"" + e.getType().getName() + "\".");
        }
        lexer.nextToken();

        StatementList doPart = statementList();

        if (lexer.token != Symbol.ENDWHILE) {
            error.signal("Syntatic", "\"" + lexer.getStringValue() + "\" found instead of \"ENDWHILE\".");
        }
        lexer.nextToken();

        return new WhileStatement(e, doPart);
    }

    // readstmt ::= READ '(' vblist ')'
    // vblist ::= variable {',' variable}
    private ReadStatement readStatement() {
        lexer.nextToken();

        if (lexer.token != Symbol.LEFTPAR) {
            error.signal("Syntatic", "\"(\" expected after \"READ\" statement.");
        }
        lexer.nextToken();

        ArrayList vbList = new ArrayList();
        ArrayList sizes = new ArrayList();
        while (true) {
            if (lexer.token != Symbol.IDENT) {
                error.signal("Syntatic", "Identifier expected after \"(\"");
            }
            Variable v = variable();
            vbList.add(v);
            sizes.add(v.getCurrentSize() - v.getRatio());

            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            } else {
                break;
            }
        }

        if (lexer.token != Symbol.RIGHTPAR) {
            error.signal("Syntatic", "\")\" expected after variable to close the \"READ\" statement");
        }
        lexer.nextToken();

        return new ReadStatement(vbList, sizes);
    }

    // writestmt ::= WRITE '(' exprlist ')'
    // exprlist ::= expr {',' expr}
    private WriteStatement writeStatement() {
        lexer.nextToken();

        if (lexer.token != Symbol.LEFTPAR) {
            error.signal("Syntatic", "\"(\" expected after \"WRITE\" statement.");
        }
        lexer.nextToken();

        ExprList exList = new ExprList();
        while (true) {
            Expr expr = expr();
            if (expr == null) {
                error.signal("Syntatic", "Expression expected after \"(\"");
            }
            exList.add(expr);

            if (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
            } else {
                break;
            }
        }
        if (lexer.token != Symbol.RIGHTPAR) {
            error.signal("Syntatic", "\")\" expected after variable to close the \"WRITE\" statement");
        }
        lexer.nextToken();

        return new WriteStatement(exList);
    }

    // writelnstmt ::= WRITELN '(' [exprlist] ')'
    private WritelnStatement writelnStatement() {
        lexer.nextToken();

        if (lexer.token != Symbol.LEFTPAR) {
            error.signal("Syntatic", "\"(\" expected after \"WRITELN\" statement.");
        }
        lexer.nextToken();
        ExprList exList = new ExprList();
        if (lexer.token == Symbol.RIGHTPAR) {
            lexer.nextToken();
        } else {
            while (true) {
                Expr expr = expr();
                exList.add(expr);

                if (lexer.token == Symbol.COMMA) {
                    lexer.nextToken();
                } else {
                    break;
                }
            }
            if (lexer.token != Symbol.RIGHTPAR) {
                error.signal("Syntatic", "\")\" expected after variable to close the \"WRITELN\" statement");
            }
            lexer.nextToken();
        }
        return new WritelnStatement(exList);
    }

    // procedureCall ::= pid '(' [exprlist] ')'
    private ProcedureCall procedureCall() {
        // We already know the identifier is a procedure. 
        // So we need not to check it again.

        ExprList anExprList = null;
        String name = (String) lexer.getStringValue();
        lexer.nextToken();
        Procedure p = (Procedure) symbolTable.get(name);
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("Syntatic", "\"(\" expected after procedure identifier");
            lexer.skipBraces();
        } else {
            lexer.nextToken();
        }
        if (lexer.token != Symbol.RIGHTPAR) {

            // The parameter list is used to check if the arguments to the
            // procedure have the correct types
            anExprList = exprList(p.getParamList());
            if (lexer.token != Symbol.RIGHTPAR) {
                error.show("Syntatic", "Error in expression, \")\" expected");
            } else {
                lexer.nextToken();
            }
        } else {
            // semantic analysis
            // does the procedure has no parameter?
            if (p.getParamList() != null && p.getParamList().getSize() != 0) {
                error.signal("Semantic", "Parameter expected");
            }
            lexer.nextToken();
        }
        return new ProcedureCall(p, anExprList);
    }

    // functionCall ::= pid '(' [exprlist] ')'
    private FunctionCall functionCall() {
        // We already know the identifier is a function. 
        // So we need not to check it again.

        ExprList anExprList = null;
        String name = (String) lexer.getStringValue();
        lexer.nextToken();

        Function p = (Function) symbolTable.get(name);
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("Syntatic", "\"(\" expected after function identifier");
            lexer.skipBraces();
        } else {
            lexer.nextToken();
        }
        if (lexer.token != Symbol.RIGHTPAR) {

            // The parameter list is used to check if the arguments to the
            // procedure have the correct types
            anExprList = exprList(p.getParamList());
            if (lexer.token != Symbol.RIGHTPAR) {
                error.show("Semantic", "Error in expression, \")\" expected");
            } else {
                lexer.nextToken();
            }
        } else {

            // semantic analysis
            // does the procedure has no parameter?
            if (p.getParamList() != null && p.getParamList().getSize() != 0) {
                error.show("Semaantic", "Parameter expected");
            }
            lexer.nextToken();
        }
        return new FunctionCall(p, anExprList);
    }

    // returnstmt ::= RETURN [expr]
    private ReturnStatement returnStatement() {
        lexer.nextToken();
        if(lexer.token == Symbol.SEMICOLON){
            return new ReturnStatement();
        }
        Expr e = expr();

        // semantic analysis
        // Are we inside a function?
        if (currentFunction == null) {
            error.show("Semantic", "Procedures should not return an expression.");
        } else if (currentFunction.getReturnType() != e.getType()) {
            if(!(currentFunction.getReturnType() == Type.realType && e.getType() == Type.integerType))
            error.show("Return type does not match function type.");
        }
        notReturn = false;
        
        return new ReturnStatement(e);
    }

    // exprList ::= expr {',' expr}
    private ExprList exprList(ParamList paramList) {
        ExprList anExprList;
        boolean firstErrorMessage = true;
        if (lexer.token == Symbol.RIGHTPAR) {
            return null;
        } else {
            Parameter parameter;
            int sizeParamList = paramList.getSize();
            Enumeration e = paramList.elements();
            anExprList = new ExprList();
            while (true) {
                parameter = (Parameter) e.nextElement();
                
                // semantic analysis
                // does the procedure has one more parameter?
                if (sizeParamList < 1 && firstErrorMessage) {
                    error.show("Semantic", "Wrong number of parameters in call");
                    firstErrorMessage = false;
                }
                sizeParamList--;
                Expr anExpr = expr();
                if (parameter.getType() != anExpr.getType()) {
                    if (!(parameter.getType() == Type.realType && anExpr.getType() == Type.integerType)) {
                        error.show("Semantic", "Type error in parameter passing");
                    }
                }
                anExprList.add(anExpr);
                if (lexer.token == Symbol.COMMA) {
                    lexer.nextToken();
                } else {
                    break;
                }
            }
            // semantic analysis
            // the procedure may need more parameters
            if (sizeParamList > 0 && firstErrorMessage) {
                error.show("Semantic", "Wrong number of parameters");
            }
            return anExprList;
        }
    }
}

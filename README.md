# Compiler Project - Simplified Pascal
Simplified Pascal compiler for C Language.

## 3rd Phase - Compiler

### Grammar:

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
        statement ::= ifStatement | whileStatement | assignStatement | readStatement | 
                 writeStatement | writelnStatement | returnStatement | procedureCall | compositeStatement

        ifStatement ::= IF expr THEN statementList [ELSE statementList] ENDIF
        whileStatement ::= WHILE expr DO statementList ENDWHILE
        assignStatement ::= variable ':=' expr
        readStatement ::= READ '(' vbList ')'
        writeStatement ::= WRITE ’(’ exprList ’)’
        writelnStatement ::= WRITELN ’(’ [exprList] ’)’
        returnStatement ::= RETURN [expr]
        procedureCall ::= pid '(' [exprList] ')'
        
        vbList ::= variable {',' variable}
        variable ::= id ['[' expr ']']

        exprList ::= expr {',' expr}
        expr ::= simpleExpr [relOp expr]
        simpleExpr ::= [unary] term {addop term}
        term ::= factor {multOp factor}
        factor ::= variable | number | '(' expr ')' | '"'string'"' | functionCall
        number ::= ['+' | '-'] {digit} ['.'] {digit}
        functionCall ::= pid '(' [exprList] ')'

        id ::= letter {letter | digit}
        pid ::= letter {letter | digit}
        intnum ::= digit {digit}
        relOp ::= '=' | '<' | '>' | '<=' | '>=' | '<>'
        addOp ::= '+' | '-' | OR
        multOp ::= '*' | '/' | AND | MOD | DIV
        unary ::= '+' | '-' | NOT

## Credits
Compiler based on:
    ### Pratice: [Learning Compiler Construction by Examples](http://www.cyan-lang.org/jose/courses/02-1/cc/comp.pdf)
    ### Theory: [Compiladores - Teoria](http://www.cyan-lang.org/jose/courses/10-1/compiladores/compiladores-teoria.pdf)
    ### Professor: [José Guimarães, PhD](http://www.cyan-lang.org/jose)

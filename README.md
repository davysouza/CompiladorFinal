# CompilerProject_SimplifiedPascal
Simplified Pascal compiler for C Language.

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

        ifstmt ::= IF expr THEN stmts [ELSE stmts] ENDIF
        whilestmt ::= WHILE expr DO stmts ENDWHILE
        assignstmt ::= variable ':=' expr
        readstmt ::= READ '(' vblist ')'
        writestmt ::= WRITE ’(’ exprlist ’)’
        writelnstmt ::= WRITELN ’(’ [exprlist] ’)’
        returnstmt ::= RETURN [expr]
        procedureCall ::= pid '(' [exprlist] ')'
        
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
        relOp ::= '=' | '<' | '>' | '<=' | '>=' | '<>'
        addOp ::= '+' | '-' | OR
        multOp ::= '*' | '/' | AND | MOD | DIV
        unary ::= '+' | '-' | NOT

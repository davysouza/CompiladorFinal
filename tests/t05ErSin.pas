PROGRAM t05;

VAR
    medidalado: REAL;
    numlados: INTEGER;
    
    PROCEDURE qualpoligono( mlado: REAL; nlados: INTEGER; );
    VAR
        mensagem: STRING;
        calculo: REAL;
    
    BEGIN
        mensagem := 'pentagono';
        IF ( nlados = 3 ) THEN
            calculo := nlados * mlado;
        ELSE 
            IF ( nlados = 4 < 5 ) THEN { # operadores de comparacao nao devem ser utilizados em seguida }
                calculo := mlado * mlado;
            ELSE
                WRITELN(mensagem);
            ENDIF;
        ENDIF;
        WRITELN(calculo);
    END;

BEGIN
    READ(numlados);
    READ(medidalado);
    qualpoligono(medidalado, numlados);
END.

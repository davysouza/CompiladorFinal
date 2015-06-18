PROGRAM t02;

VAR
    medida0lado: REAL;
    numlados: INTEGER;
    
    PROCEDURE qualpoligono( mlado: REAL; nlados: INTEGER );
    VAR
        mensagem: STRING;
        calculo: REAL;
    
    BEGIN
        mensagem := 'pentagono';
        IF ( nlados = 3 ) THEN
            calculo := nlados * mlado;
        ELSE 
            IF ( nlados = 4 ) THEN
                calculo := mlado * mlado;
            ELSE
                WRITELN(mensagem);
            ENDIF;
        ENDIF;
        WRITELN(calculo);
    END;

BEGIN
    READ(numlados);
    READ(medida0lado);
    qualpoligono(medida0lado, numlados) { # falta ; }
END.

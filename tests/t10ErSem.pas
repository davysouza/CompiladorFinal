PROGRAM t10;

VAR
	a : REAL;
	p : REAL;
	raio : REAL;

	FUNCTION factorial( n : INTEGER ) : INTEGER;
	VAR
		i, product : INTEGER;

		FUNCTION area( r : REAL ) : REAL; { # declaracao de funcoes aninhadas nao eh permitido }
		BEGIN
		    RETURN 3.14 * r * r;
		END;
	
	BEGIN
		product := 1;
		i := 2;
		WHILE i <= n DO
			product := product * i;
		ENDWHILE;
	END;

	FUNCTION perimetro( r : REAL ) : REAL;
	BEGIN
	    RETURN 3.14 * 2 * r;
	END;

BEGIN
 	WRITELN("Informe o raio: ");
 	READ(raio);
 	a := area(raio);
 	p := perimetro(raio);
	WRITELN(a);
	WRITELN(p);
END.

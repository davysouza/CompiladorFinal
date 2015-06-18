PROGRAM t08;

VAR
	a : REAL;
	p : REAL;
	raio : REAL;

	FUNCTION area( r : REAL ) : REAL;
	BEGIN
	    RETURN 3.14 * r * r;
	END;

	FUNCTION perimetro( r : REAL ) : REAL;
	BEGIN
	    RETURN 3.14 * 2 * r;
	END;

BEGIN
 	WRITELN('Informe o raio: ');
 	READ(raio);
 	a := area(raio);
 	area(a); { # chamada a funcao nunca deve ser um statement, pois sempre tem valor de retorno }
 	p := perimetro(raio);
	WRITELN(a);
	WRITELN(p);
END.

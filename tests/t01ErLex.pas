PROGRAM t01;

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
 	p : perimetro(raio); { # simbolo errado de atribuicao }
	WRITELN(a);
	WRITELN(p);
END.

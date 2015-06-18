PROGRAM t16;

VAR
	a, b : INTEGER;
	c, d : REAL;
	e, f : ARRAY [0 .. 5] OF STRING;
	g, h : ARRAY [0 .. 5] OF INTEGER;
	i, j : ARRAY [7 .. 12] OF REAL;

	PROCEDURE som(a, b : INTEGER);
	BEGIN
		RETURN;
	END;

	FUNCTION soma(x, y : REAL) : REAL;
	BEGIN
		RETURN 4;
	END;

	FUNCTION sub(x, y : REAL) : STRING;
	BEGIN
		RETURN f[5]; { Retornando Array }
	END;

BEGIN
	c := soma(g[0], b); { Array como  parametro }

	WRITELN(soma(c,d), sub(a,j[7])); { Passando funcoes como parametro }
END.
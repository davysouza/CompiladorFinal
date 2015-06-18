PROGRAM t17;

VAR
	a, b : INTEGER;
	c, d : REAL;
	e, f : ARRAY [0 .. 5] OF STRING;
	g, h : ARRAY [0 .. 5] OF INTEGER;
	i, j : ARRAY [7 .. 12] OF REAL;

	PROCEDURE som(a, b : INTEGER; ); { Erro: ';' no lugar errado. A gramatica nao aceita. }
	BEGIN
		RETURN;
	END;

	FUNCTION soma(x, y : REAL) : REAL;
	BEGIN
		RETURN 4;
	END;

	FUNCTION sub(x, y : REAL) : STRING;
	BEGIN
		RETURN f[5];
	END;

BEGIN
	a := som(a, b);

	c := soma(g[0], b);

	WRITELN(soma(c,d), sub(a,j[7]));
END.
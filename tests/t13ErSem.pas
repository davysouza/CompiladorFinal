PROGRAM t13;

VAR
	a, b : INTEGER;
	c, d : REAL;

	PROCEDURE som(a, b : INTEGER);
	BEGIN
		RETURN;
	END;

	FUNCTION soma(x, y : REAL) : REAL;
	BEGIN
		
	END; {# Erro: Funcao sem retorno}

BEGIN
	c := soma(a, b);
	WRITELN(a, b);
END.
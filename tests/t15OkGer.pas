PROGRAM t15;

VAR
	a, b : INTEGER;
	c, d : REAL;

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
		RETURN 'ha ha eee';
	END;

BEGIN
	c := soma(a, b);
	WRITELN(soma(c,d), sub(a,b)); 
END.
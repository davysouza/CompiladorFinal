PROGRAM t07;

VAR
	x : INTEGER;
	q : INTEGER;

	PROCEDURE proc( n : INTEGER );
	BEGIN
		WRITELN(n, q);
		RETURN n; { # procedimentos nao devem retornar expressao }
	END;

BEGIN	
	x := 10;
END.

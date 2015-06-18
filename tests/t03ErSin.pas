PROGRAM t03;

VAR
	x : INTEGER;
	q : INTEGER;

	PROCEDURE proc( n : INTEGER );
	BEGIN
		WRITELN(n, q);
		RETURN;
	END;

BEGIN	
	x := 1 AND 0;
	q := 5 DIV 2;
    proc(x);
    WRITELN(x, q, ); { # falta id }
END.

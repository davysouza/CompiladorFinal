PROGRAM t11;

VAR
	apple, pear, orange : INTEGER;
	text : STRING;
	number : REAL;
	a, b : CHAR;

	PROCEDURE writentimes( text: STRING; n : INTEGER );
	VAR
		i : INTEGER;
	
	BEGIN
		i := 1;
		WHILE i <= n DO
			WRITE(text);
		ENDWHILE;
	END;

	FUNCTION factorial( n : INTEGER ) : INTEGER;
	VAR
		i, product : INTEGER;
	
	BEGIN
		product := 1;
		i := 2;
		WHILE i <= n DO
			product := product * i;
		ENDWHILE;
	END;

BEGIN
	apple := 5;
	a := 'A';
	pear := factorial (apple);
	orange := 10;
	number := 4.2;
	text := 'Hello, world!';
	WRITE(text);
	READ(text);
	WRITELN(text);
END.

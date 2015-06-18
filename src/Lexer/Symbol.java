package Lexer;

/**
 *
 * @author Davy
 */
public class Symbol {

    public final static int EOF = 0,
            PROGRAM = 1,
            PROCEDURE = 2,
            FUNCTION = 3,
            BEGIN = 4,
            END = 5,
            // Variables and types
            VAR = 6,
            IDENT = 7,
            NUMBER = 8,
            INTEGER = 9,
            REAL = 10,
            CHAR = 11,
            STRING = 12,
            ARRAY = 13,
            OF = 14,
            // Statements
            IF = 15,
            THEN = 16,
            ELSE = 17,
            ENDIF = 18,
            WHILE = 19,
            DO = 20,
            ENDWHILE = 21,
            READ = 22,
            WRITE = 23,
            WRITELN = 24,
            ASSIGN = 25,
            RETURN = 26,
            // Operators
            EQ = 27,
            NEQ = 28,
            LE = 29,
            LT = 30,
            GE = 31,
            GT = 32,
            TRUE = 33,
            FALSE = 34,
            AND = 35,
            OR = 36,
            NOT = 37,
            PLUS = 38,
            MINUS = 39,
            MULT = 40,
            DIV = 41,
            REALDIV = 42,
            MOD = 43,
            // Punctuations            
            COMMA = 44,
            COLON = 45,
            SEMICOLON = 46,
            DOT = 47,
            DOUBLEDOT = 48,
            // Braces
            LEFTBRACKET = 49,
            RIGHTBRACKET = 50,
            LEFTPAR = 51,
            RIGHTPAR = 52,
            LEFTCURLYBRACKET  = 53,
            RIGHTCURLYBRACKET  = 54,
            CHARACTER = 55,
            // new symbols should be added before LastSymbol, which is used as the number
            // of constantes in this class
            LastSymbol = 56;
}

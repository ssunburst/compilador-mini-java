package LexicalModule;

public class Token
{
    private int tokenID;
    private String lexeme;
    private int line;

    public static final int EOF = -1;
    public static final int IDENTIFIER_CLASS = 0;
    public static final int IDENTIFIER_METHOD_VARIABLE = 1;
    public static final int LITERAL_CHAR = 2;
    public static final int LITERAL_INT = 3;
    public static final int LITERAL_STRING = 4;
    public static final int OPERATOR_AND = 5;
    public static final int OPERATOR_DIVISION = 6;
    public static final int OPERATOR_EQUALS = 7;
    public static final int OPERATOR_GREATER = 8;
    public static final int OPERATOR_GREATER_OR_EQUAL = 9;
    public static final int OPERATOR_LESSER = 10;
    public static final int OPERATOR_LESSER_OR_EQUAL = 11;
    public static final int OPERATOR_MINUS = 12;
    public static final int OPERATOR_MODULO = 13;
    public static final int OPERATOR_MULTIPLICATION = 14;
    public static final int OPERATOR_NOT = 15;
    public static final int OPERATOR_NOT_EQUALS = 16;
    public static final int OPERATOR_OR = 17;
    public static final int OPERATOR_PLUS = 18;
    public static final int PUNCTUATION_CLOSING_BRACKET = 19;
    public static final int PUNCTUATION_CLOSING_PARENTHESIS = 20;
    public static final int PUNCTUATION_COMMA = 21;
    public static final int PUNCTUATION_OPENING_BRACKET = 22;
    public static final int PUNCTUATION_OPENING_PARENTHESIS = 23;
    public static final int PUNCTUATION_PERIOD = 24;
    public static final int PUNCTUATION_SEMICOLON = 25;
    public static final int KEYWORD_CLASS = 26;
    public static final int KEYWORD_EXTENDS = 27;
    public static final int KEYWORD_STATIC = 28;
    public static final int KEYWORD_DYNAMIC = 29;
    public static final int KEYWORD_VOID = 30;
    public static final int KEYWORD_BOOLEAN = 31;
    public static final int KEYWORD_CHAR = 32;
    public static final int KEYWORD_INT = 33;
    public static final int KEYWORD_STRING = 34;
    public static final int KEYWORD_PUBLIC = 35;
    public static final int KEYWORD_PRIVATE = 36;
    public static final int KEYWORD_IF = 37;
    public static final int KEYWORD_ELSE = 38;
    public static final int KEYWORD_FOR = 39;
    public static final int KEYWORD_RETURN = 40;
    public static final int KEYWORD_THIS = 41;
    public static final int KEYWORD_NEW = 42;
    public static final int KEYWORD_NULL = 43;
    public static final int KEYWORD_TRUE = 44;
    public static final int KEYWORD_FALSE = 45;
    public static final int OPERATOR_ASSIGN = 46;
    public static final int OPERATOR_INCREMENT = 47;
    public static final int OPERATOR_DECREMENT = 48;
    public static final int PUNCTUATION_COLON = 49;


    public Token(int tokenID, String lexeme, int line)
    {
        this.tokenID = tokenID;
        this.lexeme = lexeme;
        this.line = line;
    }

    public int getTokenID()
    {
        return tokenID;
    }

    public String getLexeme()
    {
        return lexeme;
    }

    public int getLine()
    {
        return line;
    }

    @Override
    public String toString()
    {
        TokenIndex index = TokenIndex.getInstance();
        String tokenString = index.getTokenString(tokenID);
        return "(" + tokenString + ", " + lexeme + ", " + line + ")";
    }
}

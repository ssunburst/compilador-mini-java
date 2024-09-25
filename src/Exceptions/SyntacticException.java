package Exceptions;

public class SyntacticException extends CompilerException
{
    String expected;

    public SyntacticException(String expected, String lexeme, int line)
    {
        super(lexeme, line);
        this.expected = expected;
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
    public String getMessage()
    {
        return "Syntactic error on line " + line + ".\n\nExpected:\t\t" + expected + "\nEncountered:\t" + lexeme;
    }

    public String getCode()
    {
        return "[Error:" + lexeme + "|" + line + "]";
    }
}

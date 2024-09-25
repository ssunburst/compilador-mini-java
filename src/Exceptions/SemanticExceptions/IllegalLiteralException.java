package Exceptions.SemanticExceptions;

public class IllegalLiteralException extends SemanticException
{
    public IllegalLiteralException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public IllegalLiteralException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

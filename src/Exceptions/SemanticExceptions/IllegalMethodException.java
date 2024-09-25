package Exceptions.SemanticExceptions;

public class IllegalMethodException extends SemanticException
{
    public IllegalMethodException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public IllegalMethodException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

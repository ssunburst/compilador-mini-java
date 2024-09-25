package Exceptions.SemanticExceptions;

public class InvalidOperatorException extends SemanticException
{
    public InvalidOperatorException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public InvalidOperatorException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

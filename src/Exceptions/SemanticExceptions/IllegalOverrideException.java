package Exceptions.SemanticExceptions;

public class IllegalOverrideException extends SemanticException
{
    public IllegalOverrideException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public IllegalOverrideException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

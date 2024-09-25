package Exceptions.SemanticExceptions;

public class IllegalCastingException extends SemanticException
{
    public IllegalCastingException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public IllegalCastingException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

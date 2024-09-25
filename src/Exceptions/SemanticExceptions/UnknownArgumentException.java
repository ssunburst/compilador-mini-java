package Exceptions.SemanticExceptions;

public class UnknownArgumentException extends SemanticException
{
    public UnknownArgumentException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public UnknownArgumentException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

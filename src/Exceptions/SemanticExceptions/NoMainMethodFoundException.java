package Exceptions.SemanticExceptions;

public class NoMainMethodFoundException extends SemanticException
{
    public NoMainMethodFoundException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public NoMainMethodFoundException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

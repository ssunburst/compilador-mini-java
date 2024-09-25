package Exceptions.SemanticExceptions;

public class IllegalChainedAccessException extends SemanticException
{
    public IllegalChainedAccessException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public IllegalChainedAccessException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

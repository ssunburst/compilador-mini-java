package Exceptions.SemanticExceptions;

public class IllegalConstructorException extends SemanticException
{
    public IllegalConstructorException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public IllegalConstructorException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

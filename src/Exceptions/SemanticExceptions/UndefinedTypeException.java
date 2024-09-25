package Exceptions.SemanticExceptions;

public class UndefinedTypeException extends SemanticException
{

    public UndefinedTypeException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public UndefinedTypeException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

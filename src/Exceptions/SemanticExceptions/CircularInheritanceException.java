package Exceptions.SemanticExceptions;

public class CircularInheritanceException extends SemanticException
{
    public CircularInheritanceException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public CircularInheritanceException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

package Exceptions.SemanticExceptions;

public class PreexistingClassException extends SemanticException
{
    public PreexistingClassException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public PreexistingClassException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

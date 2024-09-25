package Exceptions.SemanticExceptions;

public class RepeatedAttributeNameException extends SemanticException
{
    public RepeatedAttributeNameException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public RepeatedAttributeNameException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

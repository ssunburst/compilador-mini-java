package Exceptions.SemanticExceptions;

public class RepeatedParameterNameException extends SemanticException
{
    public RepeatedParameterNameException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public RepeatedParameterNameException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

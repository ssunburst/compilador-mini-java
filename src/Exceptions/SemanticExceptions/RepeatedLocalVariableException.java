package Exceptions.SemanticExceptions;

public class RepeatedLocalVariableException extends SemanticException
{
    public RepeatedLocalVariableException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public RepeatedLocalVariableException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }
}

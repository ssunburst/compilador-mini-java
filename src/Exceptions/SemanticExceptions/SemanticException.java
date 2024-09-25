package Exceptions.SemanticExceptions;

import Exceptions.CompilerException;

public class SemanticException extends CompilerException
{
    public SemanticException(String lexeme, int line)
    {
        super(lexeme, line);
    }

    public SemanticException(String lexeme, int line, String message)
    {
        super(lexeme, line, message);
    }

    @Override
    public String getMessage()
    {
        return "Semantic exception on line " + line + ": " + super.getMessage();
    }
}

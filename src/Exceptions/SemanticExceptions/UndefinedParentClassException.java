package Exceptions.SemanticExceptions;

public class UndefinedParentClassException extends SemanticException
{
    public UndefinedParentClassException(String lexeme, int line) {
        super(lexeme, line);
    }

    public UndefinedParentClassException(String lexeme, int line, String message) {
        super(lexeme, line, message);
    }
}

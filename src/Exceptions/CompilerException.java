package Exceptions;

public abstract class CompilerException extends Exception
{
    protected String lexeme;
    protected int line;

    public CompilerException(String lexeme, int line)
    {
        super();
        this.lexeme = lexeme;
        this.line = line;
    }

    public CompilerException(String lexeme, int line, String message)
    {
        super(message);
        this.lexeme = lexeme;
        this.line = line;
    }

    public String getLexeme()
    {
        return lexeme;
    }

    public int getLine()
    {
        return line;
    }

    @Override
    public String toString()
    {
        return "[Error:" + lexeme + "|" + line + "]";
    }
}

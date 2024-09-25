package Exceptions;

public class LexicalException extends CompilerException
{
    String extract;
    int index;

    public LexicalException(String lexeme, int line, int index, String message, String extract)
    {
        super(lexeme, line, message);
        this.extract = extract;
        this.index = index;
    }

    public String getLexeme()
    {
        return lexeme;
    }

    public int getLine()
    {
        return line;
    }

    public String getExtract()
    {
        return extract;
    }

    public int getIndex()
    {
        return index;
    }

    @Override
    public String getMessage()
    {
        return "Lexical error on line " + line + ", column " + (index + 1) + ": " + super.getMessage();
    }

    @Override
    public String toString()
    {
        return "[Error:" + lexeme + "|" + line + "]";
    }
}

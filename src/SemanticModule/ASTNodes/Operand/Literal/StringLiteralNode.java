package SemanticModule.ASTNodes.Operand.Literal;

import Exceptions.SemanticExceptions.IllegalLiteralException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class StringLiteralNode extends LiteralOperandNode
{
    public StringLiteralNode()
    {
        super();
    }

    public StringLiteralNode(Token literalToken)
    {
        super(literalToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (literalToken.getTokenID() != Token.LITERAL_STRING)
            throw new IllegalLiteralException(literalToken.getLexeme(), literalToken.getLine(), "Invalid String literal.");
        return Types.STRING;
    }

    @Override
    public void generateCode()
    {
        String string = literalToken.getLexeme();
        string = string.substring(1, string.length()-1);
        int length = string.length();
        SymbolTable.appendCodeLines("RMEM 1", "PUSH " + (length + 1), "PUSH " + SymbolTable.MALLOC_TAG, "CALL");
        int i, c;
        for (i = 0; i < length; i++)
        {
            SymbolTable.appendCodeLines("DUP");
            if (i > 0)
                SymbolTable.appendCodeLines("PUSH " + i, "ADD");
            c = string.charAt(i);
            SymbolTable.appendCodeLines("PUSH " + c, "STOREREF 0");
        }
        SymbolTable.appendCodeLines("DUP", "PUSH " + i, "ADD", "PUSH 0", "STOREREF 0");
    }
}

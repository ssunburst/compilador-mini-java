package SemanticModule.ASTNodes.Operand.Literal;

import Exceptions.SemanticExceptions.IllegalLiteralException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class CharLiteralNode extends LiteralOperandNode
{
    public CharLiteralNode() {}

    public CharLiteralNode(Token literalToken)
    {
        super(literalToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (literalToken.getTokenID() != Token.LITERAL_CHAR)
            throw new IllegalLiteralException(literalToken.getLexeme(), literalToken.getLine(), "Illegal character literal.");
        return Types.CHAR;
    }

    @Override
    public void generateCode()
    {
        int n = 0;
        if (literalToken.getLexeme().length() == 3)
            n = literalToken.getLexeme().charAt(1);
        SymbolTable.appendCodeLines("PUSH " + n);
    }
}

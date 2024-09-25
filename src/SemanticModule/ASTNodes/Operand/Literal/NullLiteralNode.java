package SemanticModule.ASTNodes.Operand.Literal;

import Exceptions.SemanticExceptions.IllegalLiteralException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class NullLiteralNode extends LiteralOperandNode
{
    public NullLiteralNode()
    {
    }

    public NullLiteralNode(Token literalToken)
    {
        super(literalToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (literalToken.getTokenID() != Token.KEYWORD_NULL)
            throw new IllegalLiteralException(literalToken.getLexeme(), literalToken.getLine(), "Expected 'null', but encountered " + literalToken.getLexeme() + ".");
        return Types.NULL;
    }

    @Override
    public void generateCode()
    {
        SymbolTable.appendCodeLines("PUSH 0");
    }
}

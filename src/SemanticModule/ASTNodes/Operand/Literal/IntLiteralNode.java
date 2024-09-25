package SemanticModule.ASTNodes.Operand.Literal;

import Exceptions.SemanticExceptions.IllegalLiteralException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class IntLiteralNode extends LiteralOperandNode
{
    public IntLiteralNode()
    {
        super();
    }

    public IntLiteralNode(Token literalToken)
    {
        super(literalToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (literalToken.getTokenID() != Token.LITERAL_INT)
            throw new IllegalLiteralException(literalToken.getLexeme(), literalToken.getLine(), "Illegal integer literal.");
        return Types.INT;
    }

    @Override
    public void generateCode()
    {
        String integer = literalToken.getLexeme();
        SymbolTable.appendCodeLines("PUSH " + integer);
    }
}

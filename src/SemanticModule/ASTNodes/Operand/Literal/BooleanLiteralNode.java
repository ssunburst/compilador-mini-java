package SemanticModule.ASTNodes.Operand.Literal;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.Types;
import Utilities.TokenInspector;

public abstract class BooleanLiteralNode extends LiteralOperandNode
{
    public BooleanLiteralNode()
    {}

    public BooleanLiteralNode(Token literal)
    {
        super(literal);
    }

    @Override
    public String check() throws SemanticException
    {
        if (!TokenInspector.isTokenAmong(literalToken, Token.KEYWORD_FALSE, Token.KEYWORD_TRUE))
            throw new SemanticException(literalToken.getLexeme(), literalToken.getLine(), "Illegal boolean literal");
        return Types.BOOLEAN;
    }
}

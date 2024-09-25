package SemanticModule.ASTNodes.Operand.Literal;

import LexicalModule.Token;
import SemanticModule.ASTNodes.Operand.OperandNode;

public abstract class LiteralOperandNode extends OperandNode
{
    protected Token literalToken;

    public LiteralOperandNode() {}

    public LiteralOperandNode(Token literalToken)
    {
        super();
        this.literalToken = literalToken;
    }

    public Token getLiteralToken()
    {
        return literalToken;
    }

    public void setLiteralToken(Token literalToken)
    {
        this.literalToken = literalToken;
    }
}

package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;

public class ExpressionInParenthesisLinkNode extends LinkNode
{
    protected ExpressionNode expression;

    public ExpressionInParenthesisLinkNode()
    {
        super();
        this.assignable = false;
        this.callable = false;
    }

    public ExpressionInParenthesisLinkNode(ExpressionNode expression)
    {
        this();
        this.expression = expression;
    }

    public ExpressionNode getExpression()
    {
        return expression;
    }

    public void setExpression(ExpressionNode expression)
    {
        this.expression = expression;
    }

    @Override
    protected String specificChecks() throws SemanticException
    {
        return expression.check();
    }

    @Override
    protected void specificCode()
    {
        expression.generateCode();
    }
}

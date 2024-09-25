package SemanticModule.ASTNodes.Expression.BinaryExpression;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;

public abstract class BinaryExpressionNode extends ExpressionNode
{
    protected ExpressionNode leftSide;
    protected Token binaryOperatorToken;
    protected ExpressionNode rightSide;

    public BinaryExpressionNode()
    {
    }

    public BinaryExpressionNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        this.leftSide = leftSide;
        this.binaryOperatorToken = binaryOperator;
        this.rightSide = rightSide;
    }

    public ExpressionNode getLeftSide()
    {
        return leftSide;
    }

    public void setLeftSide(ExpressionNode leftSide)
    {
        this.leftSide = leftSide;
    }

    public Token getBinaryOperatorToken()
    {
        return binaryOperatorToken;
    }

    public void setBinaryOperatorToken(Token binaryOperatorToken)
    {
        this.binaryOperatorToken = binaryOperatorToken;
    }

    public ExpressionNode getRightSide()
    {
        return rightSide;
    }

    public void setRightSide(ExpressionNode rightSide)
    {
        this.rightSide = rightSide;
    }

    @Override
    public String check() throws SemanticException
    {
        String leftType = leftSide.check();
        String rightType = rightSide.check();
        return operate(leftType, rightType);
    }

    protected abstract String operate(String leftType, String rightType) throws SemanticException;

    @Override
    public void generateCode()
    {
        leftSide.generateCode();
        rightSide.generateCode();
    }
}

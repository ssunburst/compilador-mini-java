package SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BinaryExpressionNode;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Types;

public abstract class BooleanBinaryOperationNode extends BinaryExpressionNode
{
    public BooleanBinaryOperationNode()
    {
    }

    public BooleanBinaryOperationNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        super(leftSide, binaryOperator, rightSide);
    }

    @Override
    protected String operate(String leftType, String rightType) throws SemanticException
    {
        return Types.BOOLEAN;
    }
}

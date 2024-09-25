package SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.LogicalOperation;

import Exceptions.SemanticExceptions.InvalidOperatorException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.BooleanBinaryOperationNode;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Types;

public abstract class LogicalOperationNode extends BooleanBinaryOperationNode
{
    public LogicalOperationNode()
    {
    }

    public LogicalOperationNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        super(leftSide, binaryOperator, rightSide);
    }

    @Override
    protected String operate(String leftType, String rightType) throws SemanticException
    {
        if (!Types.conformsWith(leftType, Types.BOOLEAN) || !Types.conformsWith(rightType, Types.BOOLEAN))
            throw new InvalidOperatorException(binaryOperatorToken.getLexeme(), binaryOperatorToken.getLine(), "Unable to apply " + binaryOperatorToken.getLexeme() + " operator - both operands must conform with boolean." );
        return super.operate(leftType, rightType);
    }
}

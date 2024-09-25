package SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.EqualityOperation;

import Exceptions.SemanticExceptions.InvalidOperatorException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.BooleanBinaryOperationNode;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Types;

public abstract class EqualityOperationNode extends BooleanBinaryOperationNode
{
    public EqualityOperationNode()
    {
    }

    public EqualityOperationNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        super(leftSide, binaryOperator, rightSide);
    }

    @Override
    protected String operate(String leftType, String rightType) throws SemanticException
    {
        if (!Types.conformsWith(leftType, rightType) && !Types.conformsWith(rightType, leftType))
            throw new InvalidOperatorException(binaryOperatorToken.getLexeme(), binaryOperatorToken.getLine(), "Unable to apply " + binaryOperatorToken.getLexeme() + " operator - Incompatible expressions.");
        return super.operate(leftType, rightType);
    }
}

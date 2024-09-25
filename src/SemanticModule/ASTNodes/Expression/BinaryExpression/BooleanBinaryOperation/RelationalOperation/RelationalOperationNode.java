package SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.RelationalOperation;

import Exceptions.SemanticExceptions.InvalidOperatorException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.BooleanBinaryOperationNode;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Types;

public abstract class RelationalOperationNode extends BooleanBinaryOperationNode
{
    public RelationalOperationNode()
    {
    }

    public RelationalOperationNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        super(leftSide, binaryOperator, rightSide);
    }

    @Override
    protected String operate(String leftType, String rightType) throws SemanticException
    {
        if (!Types.conformsWith(leftType, Types.INT) || !Types.conformsWith(rightType, Types.INT))
            throw new InvalidOperatorException(binaryOperatorToken.getLexeme(), binaryOperatorToken.getLine(), "Unable to apply " + binaryOperatorToken.getLexeme() + " operator - both operands must conform with integer." );
        return super.operate(leftType, rightType);
    }
}

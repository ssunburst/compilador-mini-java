package SemanticModule.ASTNodes.Expression.BinaryExpression.IntegerBinaryOperation;

import Exceptions.SemanticExceptions.InvalidOperatorException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BinaryExpressionNode;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Types;


public abstract class IntegerBinaryOperationNode extends BinaryExpressionNode
{
    public IntegerBinaryOperationNode()
    {
        super();
    }

    public IntegerBinaryOperationNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        super(leftSide, binaryOperator, rightSide);
    }

    @Override
    protected String operate(String leftType, String rightType) throws SemanticException
    {
        if (!Types.conformsWith(leftType, Types.INT) || !Types.conformsWith(rightType, Types.INT))
            throw new InvalidOperatorException(binaryOperatorToken.getLexeme(), binaryOperatorToken.getLine(), "Unable to use operator " + binaryOperatorToken.getLexeme() + " - both operands must be compatible with integer." );
        return Types.INT;
    }
}

package SemanticModule.ASTNodes.Expression.UnaryExpression;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Operand.OperandNode;
import SemanticModule.Types;
import Utilities.TokenInspector;

public class UnaryPlusNode extends UnaryExpressionNode
{
    Token plusSignToken;

    public UnaryPlusNode()
    {
        super();
    }

    public UnaryPlusNode(OperandNode operand)
    {
        super(operand);
    }

    public UnaryPlusNode(OperandNode operand, Token plusSignToken)
    {
        super(operand);
        this.plusSignToken = plusSignToken;
    }

    @Override
    public String check() throws SemanticException
    {
        if (!TokenInspector.isTokenAmong(plusSignToken, Token.OPERATOR_PLUS))
            throw new SemanticException(plusSignToken.getLexeme(), plusSignToken.getLine(), "Expected '+', but encountered" + plusSignToken.getLexeme() + ".");
        String operandType = super.check();
        if (!Types.conformsWith(operandType, Types.INT))
            throw new SemanticException(plusSignToken.getLexeme(), plusSignToken.getLine(), "Cannot apply '+' operator to " + operandType + ".");
        return Types.INT;
    }
}

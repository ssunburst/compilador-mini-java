package SemanticModule.ASTNodes.Expression.UnaryExpression;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.ASTNodes.Operand.OperandNode;

public class UnaryExpressionNode extends ExpressionNode
{
    protected OperandNode operand;

    public UnaryExpressionNode(OperandNode operand)
    {
        this();
        this.operand = operand;
    }

    public UnaryExpressionNode()
    {
        super();
    }

    public OperandNode getOperand()
    {
        return operand;
    }

    public void setOperand(OperandNode operand)
    {
        this.operand = operand;
    }

    @Override
    public String check() throws SemanticException
    {
        return operand.check();
    }

    @Override
    public void generateCode()
    {
        operand.generateCode();
    }
}

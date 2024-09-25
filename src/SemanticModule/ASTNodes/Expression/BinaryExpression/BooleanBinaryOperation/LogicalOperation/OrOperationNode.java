package SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.LogicalOperation;

import Exceptions.SemanticExceptions.InvalidOperatorException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.SymbolTable;
import Utilities.TokenInspector;

public class OrOperationNode extends LogicalOperationNode
{
    public OrOperationNode()
    {
    }

    public OrOperationNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        super(leftSide, binaryOperator, rightSide);
    }

    @Override
    protected String operate(String leftType, String rightType) throws SemanticException
    {
        if (binaryOperatorToken.getTokenID() != Token.OPERATOR_OR)
            throw new InvalidOperatorException(binaryOperatorToken.getLexeme(), binaryOperatorToken.getLine(), "Expected '||' operator.");
        return super.operate(leftType, rightType);
    }

    @Override
    public void generateCode()
    {
        super.generateCode();
        SymbolTable.appendCodeLines("OR");
    }
}

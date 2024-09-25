package SemanticModule.ASTNodes.Expression.BinaryExpression.IntegerBinaryOperation;

import Exceptions.SemanticExceptions.InvalidOperatorException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.SymbolTable;
import Utilities.TokenInspector;

public class ModuloOperationNode extends IntegerBinaryOperationNode
{
    public ModuloOperationNode(ExpressionNode leftSide, Token binaryOperator, ExpressionNode rightSide)
    {
        super(leftSide, binaryOperator, rightSide);
    }

    @Override
    public String check() throws SemanticException
    {
        if (!TokenInspector.isTokenAmong(binaryOperatorToken, Token.OPERATOR_MODULO))
            throw new InvalidOperatorException(binaryOperatorToken.getLexeme(), binaryOperatorToken.getLine(), "Expected '%' operator.");
        return super.check();
    }

    @Override
    public void generateCode()
    {
        super.generateCode();
        SymbolTable.appendCodeLines("MOD");
    }
}
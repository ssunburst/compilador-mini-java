package SemanticModule.ASTNodes.Expression.UnaryExpression;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Operand.OperandNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;
import Utilities.TokenInspector;

public class UnaryMinusNode extends UnaryExpressionNode
{
        Token minusSignToken;

        public UnaryMinusNode()
        {
            super();
        }

        public UnaryMinusNode(OperandNode operand)
        {
            super(operand);
        }

        public UnaryMinusNode(OperandNode operand, Token minusSignToken)
        {
            super(operand);
            this.minusSignToken = minusSignToken;
        }

        @Override
        public String check() throws SemanticException
        {
            if (!TokenInspector.isTokenAmong(minusSignToken, Token.OPERATOR_MINUS))
                throw new SemanticException(minusSignToken.getLexeme(), minusSignToken.getLine(), "Expected '-', but encountered" + minusSignToken.getLexeme() + ".");
            String operandType = super.check();
            if (!Types.conformsWith(operandType, Types.INT))
                throw new SemanticException(minusSignToken.getLexeme(), minusSignToken.getLine(), "Cannot apply '-' operator to " + operandType + ".");
            return Types.INT;
        }

    @Override
    public void generateCode()
    {
        super.generateCode();
        SymbolTable.appendCodeLines("NEG");
    }
}

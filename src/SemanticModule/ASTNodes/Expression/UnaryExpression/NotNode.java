package SemanticModule.ASTNodes.Expression.UnaryExpression;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Operand.OperandNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;
import Utilities.TokenInspector;

public class NotNode extends UnaryExpressionNode
{
    protected Token notSignToken;

    public NotNode(OperandNode operand)
    {
        super(operand);
    }

    public NotNode()
    {
        super();
    }

    public NotNode(OperandNode operand, Token notSignToken)
    {
        super(operand);
        this.notSignToken = notSignToken;
    }

    public NotNode(Token notSignToken)
    {
        this.notSignToken = notSignToken;
    }

    public Token getNotSignToken()
    {
        return notSignToken;
    }

    public void setNotSignToken(Token notSignToken)
    {
        this.notSignToken = notSignToken;
    }

    @Override
    public String check() throws SemanticException
    {
        if (!TokenInspector.isTokenAmong(notSignToken, Token.OPERATOR_NOT))
            throw new SemanticException(notSignToken.getLexeme(), notSignToken.getLine(), "Expected '!', but encountered" + notSignToken.getLexeme() + ".");
        String operandType = super.check();
        if (!Types.conformsWith(operandType, Types.BOOLEAN))
            throw new SemanticException(notSignToken.getLexeme(), notSignToken.getLine(), "Cannot apply '!' operator to " + operandType + ".");
        return Types.BOOLEAN;
    }

    @Override
    public void generateCode()
    {
        super.generateCode();
        SymbolTable.appendCodeLines("NOT");
    }
}

package SemanticModule.ASTNodes.Statement;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.ASTNodes.Statement.Block.LocalVariable;
import SemanticModule.ASTNodes.VariableType.VariableTypeNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class LocalVariableNode extends StatementNode
{
    protected VariableTypeNode type;
    protected Token idToken;
    protected ExpressionNode expression;
    protected LocalVariable variable;

    public LocalVariableNode() {}

    public LocalVariableNode(VariableTypeNode type, Token idToken)
    {
        this.type = type;
        this.idToken = idToken;
        variable = null;
    }

    public LocalVariableNode(VariableTypeNode type, Token idToken, ExpressionNode expression)
    {
        this(type, idToken);
        this.expression = expression;
    }

    public VariableTypeNode getType()
    {
        return type;
    }

    public void setType(VariableTypeNode type)
    {
        this.type = type;
    }

    public Token getIdToken()
    {
        return idToken;
    }

    public void setIdToken(Token idToken)
    {
        this.idToken = idToken;
    }

    public ExpressionNode getExpression()
    {
        return expression;
    }

    public void setExpression(ExpressionNode expression)
    {
        this.expression = expression;
    }

    @Override
    public void check() throws SemanticException
    {
        super.check();
        String variableType = type.check();
        if (SymbolTable.currentBlock().resolveName(idToken.getLexeme()) != null)
            throw new SemanticException(idToken.getLexeme(), idToken.getLine(), "LocalVariable with name " + idToken.getLexeme() + " already declared in " + SymbolTable.currentUnit() + ".");
        else
            variable = SymbolTable.currentBlock().addVariable(variableType, idToken.getLexeme());
        if (expression != null)
        {
            String expressionType = expression.check();
            if (!Types.conformsWith(expressionType, variableType))
                throw new SemanticException("=", idToken.getLine(), "Cannot assign " + expressionType + " value to " + variableType + " variable.");
        }
    }

    @Override
    public void generateCode()
    {
        if (expression != null)
            expression.generateCode();
        else
            SymbolTable.appendCodeLines("PUSH 0");
        variable.generateLeftSideCode(false);
    }
}

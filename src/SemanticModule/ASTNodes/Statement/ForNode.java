package SemanticModule.ASTNodes.Statement;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.ASTNodes.Statement.Assignmnent.AssignmentNode;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class ForNode extends BlockNode
{
    protected Token forToken;
    protected LocalVariableNode localVariable;
    protected ExpressionNode booleanExpression;
    protected AssignmentNode assignment;

    public ForNode(Token forToken)
    {
        this.forToken = forToken;
    }

    public ForNode(Token forToken, LocalVariableNode localVariable, ExpressionNode booleanExpression, AssignmentNode assignment)
    {
        this(forToken);
        this.localVariable = localVariable;
        this.booleanExpression = booleanExpression;
        this.assignment = assignment;
    }

    public LocalVariableNode getLocalVariable()
    {
        return localVariable;
    }

    public void setLocalVariable(LocalVariableNode localVariable)
    {
        this.localVariable = localVariable;
    }

    public ExpressionNode getBooleanExpression()
    {
        return booleanExpression;
    }

    public void setBooleanExpression(ExpressionNode booleanExpression)
    {
        this.booleanExpression = booleanExpression;
    }

    public AssignmentNode getAssignment()
    {
        return assignment;
    }

    public void setAssignment(AssignmentNode assignment)
    {
        this.assignment = assignment;
    }

    @Override
    protected void specificChecks() throws SemanticException
    {
        localVariable.check();
        String expressionType = booleanExpression.check();
        if (!expressionType.equals(Types.BOOLEAN))
            throw new SemanticException(forToken.getLexeme(), forToken.getLine(), "Illegal for statement - Condition must be a boolean expression.");
        assignment.check();
        super.specificChecks();
    }

    @Override
    protected void specificCode()
    {
        int forCount = SymbolTable.forCount();
        String conditionTag = "forCondition$" + forCount;
        String exitTag = "forExit$" + forCount;

        SymbolTable. appendCodeLines("NOP # Variable declaration for for loop " + forCount);
        localVariable.generateCode();

        SymbolTable. appendCodeLines(conditionTag + ": NOP # Condition for for loop " + forCount);
        booleanExpression.generateCode();
        SymbolTable.appendCodeLines("BF " + exitTag);

        SymbolTable. appendCodeLines("NOP # Statement body for for loop " + forCount);
        super.specificCode();

        SymbolTable. appendCodeLines("NOP # Assignment statement for for loop " + forCount);
        assignment.generateCode();
        SymbolTable.appendCodeLines("JUMP " + conditionTag);

        SymbolTable.appendCodeLines(exitTag + ": NOP # Exit from for loop " + forCount);
    }
}

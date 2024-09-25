package SemanticModule.ASTNodes.Statement;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class IfNode extends StatementNode
{
    protected Token ifToken;
    protected ExpressionNode condition;
    protected BlockNode thenBlock;
    protected BlockNode elseBlock;

    public IfNode(Token ifToken)
    {
        this.ifToken = ifToken;
        thenBlock = new BlockNode();
        elseBlock = new BlockNode();
    }

    public IfNode(Token ifToken, ExpressionNode condition, StatementNode thenStatement)
    {
        this(ifToken);
        this.condition = condition;
        thenBlock.addStatement(thenStatement);
    }

    public IfNode(Token ifToken, ExpressionNode condition, StatementNode thenStatement, StatementNode elseStatement)
    {
        this(ifToken, condition, thenStatement);
        elseBlock.addStatement(elseStatement);
    }

    public Token getIfToken()
    {
        return ifToken;
    }

    public void setIfToken(Token ifToken)
    {
        this.ifToken = ifToken;
    }

    public ExpressionNode getCondition()
    {
        return condition;
    }

    public void setCondition(ExpressionNode condition)
    {
        this.condition = condition;
    }

    public void setThenStatement(StatementNode thenStatement)
    {
        if(thenBlock.containsStatements())
            thenBlock = new BlockNode();
        thenBlock.addStatement(thenStatement);
    }

    public void setElseStatement(StatementNode elseStatement)
    {
        if(elseBlock.containsStatements())
            elseBlock = new BlockNode();
        elseBlock.addStatement(elseStatement);
    }

    public void check() throws SemanticException
    {
        super.check();
        String conditionType = condition.check();
        if (!conditionType.equals(Types.BOOLEAN))
            throw new SemanticException(ifToken.getLexeme(), ifToken.getLine(), "Illegal if statement - Condition must be a boolean expression.");
        thenBlock.setParentBlock(SymbolTable.currentBlock());
        elseBlock.setParentBlock(SymbolTable.currentBlock());
        thenBlock.check();
        elseBlock.check();
    }

    public void generateCode()
    {
        int ifCount = SymbolTable.ifCount();
        String exitTag = "exitIf$" + ifCount;
        String elseTag = "elseIf$" + ifCount;
        SymbolTable.appendCodeLines("NOP # if statement " + ifCount);
        condition.generateCode();
        SymbolTable.appendCodeLines("BF " + (elseBlock.containsStatements()? elseTag : exitTag));
        thenBlock.generateCode();
        SymbolTable.appendCodeLines("JUMP " + exitTag);
        if (elseBlock.containsStatements())
        {
            SymbolTable.appendCodeLines(elseTag + ": NOP # else statement for if " + ifCount);
            elseBlock.generateCode();
        }
        SymbolTable.appendCodeLines(exitTag + ": NOP # exit from if statement " + ifCount);
    }
}

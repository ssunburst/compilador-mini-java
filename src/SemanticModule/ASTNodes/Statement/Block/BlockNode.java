package SemanticModule.ASTNodes.Statement.Block;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Statement.StatementNode;
import SemanticModule.IterableDictionary;
import SemanticModule.SymbolTable;
import SemanticModule.Variable;

import java.util.LinkedList;
import java.util.List;

public class BlockNode extends StatementNode
{
    protected BlockNode parentBlock;
    protected List<StatementNode> statements;
    protected IterableDictionary<String, LocalVariable> localVariables;
    protected boolean checked;
    protected Token returnToken;
    protected int variableOffset;

    public BlockNode()
    {
        statements = new LinkedList<>();
        localVariables = new IterableDictionary<>();
        checked = false;
        variableOffset = 0;
    }

    public BlockNode(BlockNode parentBlock)
    {
        this();
        this.parentBlock = parentBlock;
    }

    public BlockNode getParentBlock()
    {
        return parentBlock;
    }

    public void setParentBlock(BlockNode parentBlock)
    {
        this.parentBlock = parentBlock;
    }

    public Token getReturnToken()
    {
        return returnToken;
    }

    public boolean returnReached()
    {
        return returnToken != null;
    }

    public void setReturnToken(Token returnToken)
    {
        this.returnToken = returnToken;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public Iterable<StatementNode> statements()
    {
        return statements;
    }

    public void addStatement(StatementNode statement)
    {
        statements.add(statement);
    }

    public boolean containsStatements()
    {
        return !statements.isEmpty();
    }

    public LocalVariable addVariable(String type, String name)
    {
        return addVariable(type, name, variableOffset--);
    }

    public LocalVariable addVariable(String type, String name, int offset)
    {
        LocalVariable variable = new LocalVariable(name, type);
        variable.setOffset(offset);
        localVariables.insert(name, variable);
        return variable;
    }

    public int getVariableOffset()
    {
        return variableOffset;
    }

    public LocalVariable resolveName(String variableName)
    {
        LocalVariable variable = localVariables.get(variableName);
        if (variable == null && !isParentBlock())
            variable = parentBlock.resolveName(variableName);
        return variable;
    }

    @Override
    public void check() throws SemanticException
    {
        if (!checked)
        {
            SymbolTable.pushBlock(this);
            super.check();
            specificChecks();
            checked = true;
            SymbolTable.popBlock();
        }
    }

    protected void specificChecks() throws SemanticException
    {
        for (StatementNode statement : statements)
            statement.check();
    }

    public boolean isParentBlock()
    {
        return parentBlock == null;
    }

    @Override
    public void generateCode()
    {
        SymbolTable.pushBlock(this);
        updateOffsets();
        allocateLocalVariables();
        specificCode();
        freeLocalVariables();
        SymbolTable.popBlock();
    }

    protected void updateOffsets()
    {
        if (parentBlock != null)
        {
            int parentOffset = parentBlock.getVariableOffset();
            variableOffset = variableOffset + parentOffset;
            for (LocalVariable var : localVariables)
                var.setOffset(var.getOffset() + parentOffset);
        }
    }

    protected void allocateLocalVariables()
    {
        manageLocalVariables("RMEM");
    }

    protected void freeLocalVariables()
    {
        manageLocalVariables("FMEM");
    }

    protected void manageLocalVariables(String instruction)
    {
        int size = -variableOffset;
        if (parentBlock != null)
            size = size + parentBlock.getVariableOffset();
        if (size > 0)
            SymbolTable.appendCodeLines(instruction + " " + size);
    }

    protected void specificCode()
    {
        for (StatementNode statement : statements)
            statement.generateCode();
    }
}

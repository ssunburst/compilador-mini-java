package SemanticModule.ASTNodes.Statement.Block;

import SemanticModule.SymbolTable;
import SemanticModule.Variable;

public class LocalVariable implements Variable
{
    protected String name;
    protected String type;
    protected int offset;

    public LocalVariable(String name, String type)
    {
        offset = 0;
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public int getOffset()
    {
        return offset;
    }

    @Override
    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    @Override
    public void generateLeftSideCode(boolean chained)
    {
        SymbolTable.appendCodeLines("STORE " + offset);
    }

    @Override
    public void generateRightSideCode(boolean chained)
    {
        SymbolTable.appendCodeLines("LOAD " + offset);
    }
}

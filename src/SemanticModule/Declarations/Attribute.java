package SemanticModule.Declarations;

import SemanticModule.Declarations.Classes.Class;
import SemanticModule.SymbolTable;
import SemanticModule.Variable;

public class Attribute implements Declarable, Variable
{
    private boolean isStatic;
    private String name;
    private int line;
    private int offset;
    private String type;
    private boolean isPublic;
    private Class sourceClass;

    public Attribute(String name, String type)
    {
        this.name = name;
        this.type = type;
        this.line = SymbolTable.PREDEFINED;
        this.isPublic = true;
    }

    public Attribute(String name, String type, int line)
    {
        this(name, type);
        this.line = line;
    }

    public Attribute(String name, String type, boolean isPublic)
    {
        this(name, type);
        this.isPublic = isPublic;
    }

    public Attribute(String name, String type, int line, boolean isPublic)
    {
        this(name, type, line);
        this.isPublic = isPublic;
    }

    public Attribute(boolean isStatic, String name, String type, int line)
    {
        this(name, type, line);
        this.isStatic = isStatic;
    }

    public Attribute(boolean isStatic, String name, String type, int line, boolean isPublic)
    {
        this (name, type, line, isPublic);
        this.isStatic = isStatic;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int getLine()
    {
        return line;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setLine(int line)
    {
        this.line = line;
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
        if (isStatic)
            SymbolTable.appendCodeLines("PUSH " + sourceClass.getClassRecordTag());
        else
            loadThis(chained);
        SymbolTable.appendCodeLines("SWAP", "STOREREF " + offset);
    }

    @Override
    public void generateRightSideCode(boolean chained)
    {
        if (isStatic)
            SymbolTable.appendCodeLines("PUSH " + sourceClass.getClassRecordTag());
        else
            loadThis(chained);
        SymbolTable.appendCodeLines("LOADREF " + offset);
    }


    public Class getSourceClass()
    {
        return sourceClass;
    }

    public void setSourceClass(Class sourceClass)
    {
        this.sourceClass = sourceClass;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public boolean isPrivate()
    {
        return !isPublic;
    }

    public boolean isStatic()
    {
        return isStatic;
    }

    @Override
    public String toString()
    {
        return name + ": " + type;
    }

    protected void loadThis(boolean chained)
    {
        if (!chained)
            SymbolTable.appendCodeLines("LOAD 3");
    }
}

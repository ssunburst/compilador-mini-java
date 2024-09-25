package SemanticModule.Declarations;

import SemanticModule.Declarations.Units.Unit;
import SemanticModule.SymbolTable;

public class Parameter implements Declarable
{
    private int line;
    private String name;
    private String type;
    private Unit associatedUnit;

    public Parameter()
    {
        line = SymbolTable.PREDEFINED;
    }

    public Parameter(String type, String name)
    {
        this();
        this.type = type;
        this.name = name;
    }

    public Parameter(String type, String name, int line)
    {
        this(type, name);
        this.line = line;
    }

    public Parameter(Unit associatedUnit, String type, String name)
    {
        this();
        this.associatedUnit = associatedUnit;
        this.type = type;
        this.name = name;
    }

    public Parameter(Unit associatedUnit, String type, String name, int line)
    {
        this(associatedUnit, type, name);
        this.line = line;
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

    public String getType()
    {
        return type;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setLine(int line)
    {
        this.line = line;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Unit getAssociatedUnit()
    {
        return associatedUnit;
    }

    public void setAssociatedUnit(Unit associatedUnit)
    {
        this.associatedUnit = associatedUnit;
    }

    // Un parámetro es igual a otro si comparten el mismo tipo.
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        else if(obj instanceof Parameter)
        {
            Parameter otherParameter = (Parameter) obj;
            return type.equals(otherParameter.getType());
        }
        else
            return false;
    }

    @Override
    public String toString()
    {
        return type + " " + name;
    }
}

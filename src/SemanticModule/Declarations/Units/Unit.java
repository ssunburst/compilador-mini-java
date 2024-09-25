package SemanticModule.Declarations.Units;

import Exceptions.SemanticExceptions.PreexistingKeyException;
import Exceptions.SemanticExceptions.RepeatedParameterNameException;
import Exceptions.SemanticExceptions.UndefinedTypeException;
import SemanticModule.Declarations.Declarable;
import SemanticModule.Types;
import SemanticModule.IterableDictionary;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.Declarations.Parameter;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.SymbolTable;
import java.util.Iterator;

public abstract class Unit implements Declarable
{
    protected String name;
    protected int line;
    protected String returnType;
    protected Class sourceClass;
    protected IterableDictionary<String, Parameter> parameters;
    protected BlockNode block;
    protected boolean generated;

    protected Unit()
    {
        parameters = new IterableDictionary<>();
        line = SymbolTable.PREDEFINED;
        returnType = Types.VOID;
        block = new BlockNode();
    }

    public Unit(String name)
    {
        this();
        this.name = name;
    }

    public Unit(String name, int line)
    {
        this(name);
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

    public void setLine(int line)
    {
        this.line = line;
    }

    public Class getSourceClass()
    {
        return sourceClass;
    }

    public void setSourceClass(Class sourceClass)
    {
        this.sourceClass = sourceClass;
    }

    public String getReturnType()
    {
        return returnType;
    }

    public void addParameter(Parameter parameter) throws RepeatedParameterNameException
    {
        try {
            parameters.secureInsert(parameter.getName(), parameter);
        } catch (PreexistingKeyException e) {
            throw new RepeatedParameterNameException(parameter.getName(), parameter.getLine(), "Method o constructor " + name + " already has an argument of name " + parameter.getName() + ".");
        }
    }

    public Parameter getParameter(String parameterName)
    {
        return parameters.get(parameterName);
    }

    public Iterable<Parameter> parameters()
    {
        return parameters;
    }

    public int parameterSize()
    {
        return parameters.size();
    }

    public boolean demandsReturnStatement()
    {
        return false;
    }

    public BlockNode getBlock()
    {
        return block;
    }

    public void setBlock(BlockNode block)
    {
        this.block = block;
    }

    public abstract boolean isStatic();

    public boolean isDynamic()
    {
        return !isStatic();
    }

    // Equals resulta verdadero si dos unidades presentan la misma signatura.
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Unit))
            return false;
        else
        {
            Unit otherUnit = (Unit) obj;
            return name.equals(otherUnit.getName()) && equalParameters(otherUnit);
        }
    }

    private boolean equalParameters(Unit otherUnit)
    {
        Iterator<Parameter> localParameters = parameters.iterator();
        Iterator<Parameter> targetParameters = otherUnit.parameters().iterator();

        boolean equals = true;
        Parameter localParameter, targetParameter;

        while((localParameters.hasNext() && targetParameters.hasNext()) && equals)
        {
            localParameter = localParameters.next();
            targetParameter = targetParameters.next();
            equals = localParameter.equals(targetParameter);
        }

        equals = equals && !localParameters.hasNext() && !targetParameters.hasNext();
        return equals;
    }

    public void check() throws UndefinedTypeException
    {
        if (!Types.isValidReturnType(returnType))
            throw new UndefinedTypeException(returnType, line, "Return type " + returnType + " for " + sourceClass + "." + this + " is unknown.");
        for(Parameter p : parameters)
        {
            if (!Types.isValid(p.getType()))
                throw new UndefinedTypeException(p.getType(), p.getLine(), "Type " + p.getType() + " for argument " + p.getName() + " for " + sourceClass + "." + this + " is unknown.");
        }
    }

    @Override
    public String toString()
    {
        String string = name + "(";
        Iterator<Parameter> it = parameters.iterator();

        while (it.hasNext())
        {
            string += it.next();
            if(it.hasNext())
                string += ", ";
        }

        string += ")";
        return string;
    }

    public String getTag()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        for (Parameter parameter : parameters)
            builder.append("$" + parameter.getType());;
        return builder.toString();
    }

    public boolean alreadyGenerated()
    {
        return generated;
    }

    public void generateCode()
    {
        SymbolTable.setCurrentUnit(this);
        SymbolTable.appendCodeLines(getTag() + ": NOP # " + toString());
        SymbolTable.appendCodeLines("LOADFP", "LOADSP", "STOREFP");
        block.generateCode();
        int returnSize = parameters.size();
        if(isDynamic())
            returnSize++;
        SymbolTable.appendCodeLines("STOREFP", "RET " + returnSize);
        generated = true;
    }
}

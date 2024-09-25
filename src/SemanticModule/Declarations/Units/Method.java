package SemanticModule.Declarations.Units;

import SemanticModule.Declarations.Parameter;
import SemanticModule.Types;
import SemanticModule.Declarations.Classes.Class;


public class Method extends Unit
{
    protected boolean staticMethod;
    protected int offset;
    protected boolean offsetSet;

    // Constructores

    public Method(String name)
    {
        super(name);
        offsetSet = false;
    }

    public Method(String name, int line)
    {
        super(name, line);
    }

    public Method(String name, String returnType, Class sourceClass)
    {
        super(name);
        this.returnType = returnType;
    }

    public Method(String name, int line, String returnType, Class sourceClass)
    {
        super(name, line);
        this.returnType = returnType;
    }

    public Method(String name, Class sourceClass, boolean staticMethod)
    {
        super(name);
        this.staticMethod = staticMethod;
    }

    public Method(String name, Class sourceClass, int line, boolean staticMethod)
    {
        super(name, line);
        this.staticMethod = staticMethod;
    }

    public Method(String name, String returnType, Class sourceClass, boolean staticMethod)
    {
        super(name);
        this.staticMethod = staticMethod;
        this.returnType = returnType;
    }

    public Method(String name, int line, String returnType, Class sourceClass, boolean staticMethod)
    {
        super(name, line);
        this.staticMethod = staticMethod;
        this.returnType = returnType;
    }

    // Métodos

    public void setName(String name)
    {
        this.name = name;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
        offsetSet = true;
    }

    public boolean isOffsetSet()
    {
        return offsetSet;
    }

    public void setReturnType(String type)
    {
        this.returnType = type;
    }

    @Override
    public boolean demandsReturnStatement()
    {
        return !this.returnType.equals(Types.VOID);
    }

    @Override
    public boolean isStatic()
    {
        return staticMethod;
    }

    public void setStatic(boolean staticMode)
    {
        staticMethod = staticMode;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        if (!returnType.equals(Types.VOID))
            builder.append(": " + returnType);
        builder.append(" from class " + sourceClass.getName());
        return builder.toString();
    }

    @Override
    public String getTag()
    {
        return "m" + super.getTag() + "@" + sourceClass.getName();
    }
}

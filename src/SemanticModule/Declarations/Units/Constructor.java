package SemanticModule.Declarations.Units;

public class Constructor extends Unit
{
    public Constructor(String name)
    {
        super(name);
    }

    public Constructor(String name, int line)
    {
        super(name, line);
    }

    @Override
    public boolean isStatic()
    {
        return false;
    }

    @Override
    public String getTag()
    {
        return "Constructor" + super.getTag();
    }
}

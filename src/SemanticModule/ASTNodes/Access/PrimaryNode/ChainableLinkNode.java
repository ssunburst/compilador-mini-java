package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.SymbolTable;

public abstract class ChainableLinkNode extends LinkNode
{
    public String check(Class sourceClass) throws SemanticException
    {
        String type = specificChecks(sourceClass);
        if (hasNext())
            type = next.check(SymbolTable.getClass(type));
        return type;
    }

    protected abstract String specificChecks(Class sourceClass) throws SemanticException;

    public void generateChainedCode()
    {
        specificCode(true);
        if (next != null)
            next.generateChainedCode();
    }

    protected abstract void specificCode(boolean chained);
}

package SemanticModule.Declarations.Classes;

import Exceptions.SemanticExceptions.IllegalMethodException;
import Exceptions.SemanticExceptions.RepeatedParameterNameException;
import Exceptions.SemanticExceptions.UndefinedParentClassException;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;
import SemanticModule.Declarations.Parameter;
import SemanticModule.Declarations.Units.Method;

public class ObjectClass extends Class
{
    private static ObjectClass instance;
    public static final String OBJECT = "Object";

    private ObjectClass()
    {
        super();
        this.name = OBJECT;
        this.consolidated = true;
        try
        {
            Method debugPrint = new Method("debugPrint", this, true);
            addMethod(debugPrint);
            debugPrint.setSourceClass(this);
            debugPrint.addParameter(new Parameter(Types.INT, "i"));
            debugPrint.setBlock(new DebugPrintBlock());
        } catch (RepeatedParameterNameException | IllegalMethodException e) {}
    }


    public static ObjectClass getInstance()
    {
        if (instance == null)
            instance = new ObjectClass();
        return instance;
    }

    @Override
    protected void checkDefaultParent() {}

    private class DebugPrintBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "IPRINT", "PRNLN");
        }
    }
}

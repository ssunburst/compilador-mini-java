package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.Declarations.Parameter;
import SemanticModule.Declarations.Units.Constructor;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConstructorLinkNode extends LinkNode
{
    Constructor constructor;
    List<ExpressionNode> arguments;

    public ConstructorLinkNode()
    {
        super();
        this.callable = true;
        this.assignable = false;
        arguments = new LinkedList<>();
    }

    public ConstructorLinkNode(Token token)
    {
        this();
        this.token = token;
    }

    @Override
    public boolean isCallable()
    {
        return true;
    }

    @Override
    public boolean isAssignable()
    {
        return false;
    }

    public void addArgument(ExpressionNode argument)
    {
        arguments.add(argument);
    }

    @Override
    protected String specificChecks() throws SemanticException
    {
        Class sourceClass = SymbolTable.getClass(token.getLexeme());

        if (sourceClass == null)
            throw new SemanticException(token.getLexeme(), token.getLine(), "Illegal constructor: class " + token.getLexeme() + " has not been declared.");

        for (Constructor constructor : sourceClass.constructors())
        {
            if(argumentsConformWithParameters(constructor))
            {
                this.constructor = constructor;
                return sourceClass.getName();
            }
        }

        throw new SemanticException(token.getLexeme(), token.getLine(), "No matching constructor.");
    }

    private boolean argumentsConformWithParameters(Constructor constructor) throws SemanticException
    {
        Iterator<Parameter> parameterIterator = constructor.parameters().iterator();
        Iterator<ExpressionNode> argumentsIterator = arguments.iterator();
        boolean matches = parameterIterator.hasNext() == argumentsIterator.hasNext();
        Parameter currentParameter;
        ExpressionNode currentArgument;

        while (parameterIterator.hasNext() && matches)
        {
            currentParameter = parameterIterator.next();
            currentArgument = argumentsIterator.next();

            matches = (parameterIterator.hasNext() == argumentsIterator.hasNext()) && Types.conformsWith(currentArgument.check(), currentParameter.getType());
        }

        return matches;
    }

    @Override
    protected void specificCode()
    {
        SymbolTable.appendCodeLines("RMEM 1");
        int allocSize = constructor.getSourceClass().getAttributeOffset();
        SymbolTable.appendCodeLines("PUSH " + allocSize, "PUSH " + SymbolTable.MALLOC_TAG, "CALL", "DUP",
                "PUSH " + constructor.getSourceClass().getVirtualTableTag(), "STOREREF 0", "DUP");
        for (ExpressionNode expression : arguments)
        {
            expression.generateCode();
            SymbolTable.appendCodeLines("SWAP");
        }
        SymbolTable.appendCodeLines("PUSH " + constructor.getTag(), "CALL");
    }
}

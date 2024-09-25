package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.Declarations.Parameter;
import SemanticModule.Declarations.Units.Method;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MethodLinkNode extends ChainableLinkNode
{
    protected Method method;
    protected List<ExpressionNode> arguments;

    public MethodLinkNode()
    {
        arguments = new LinkedList<>();
        this.callable = true;
        this.assignable = false;
    }

    public MethodLinkNode(Token methodToken)
    {
        this();
        this.token = methodToken;
    }

    public void addArgument(ExpressionNode argument)
    {
        arguments.add(argument);
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

    @Override
    protected String specificChecks() throws SemanticException
    {
        return specificChecks(SymbolTable.currentClass(), SymbolTable.currentUnit().isStatic());
    }


    @Override
    protected String specificChecks(Class sourceClass) throws SemanticException
    {
        return specificChecks(sourceClass, false);
    }

    protected String specificChecks(Class sourceClass, boolean mustBeStatic) throws SemanticException
    {
        Iterable<Method> methods = sourceClass.getMethod(token.getLexeme());
        if (methods == null)
            throw new SemanticException(token.getLexeme(), token.getLine(), "No method " + getToken().getLexeme() + " in class " + sourceClass.getName() + " that matches call.");
        Iterator<Method> methodsIterator = methods.iterator();

        Method currentMethod = null;
        boolean matchingSignature = false;

        while (methodsIterator.hasNext() && !matchingSignature)
        {
            currentMethod = methodsIterator.next();
            matchingSignature = compareSignature(currentMethod);
        }

        if (currentMethod == null || !matchingSignature)
            throw new SemanticException(token.getLexeme(), token.getLine(), "No matching method for " + token.getLexeme() + " in class " + sourceClass.getName() + ".");
        else
        {
            if (currentMethod.isDynamic() && mustBeStatic)
                throw new SemanticException(token.getLexeme(), token.getLine(), "Illegal call - " + currentMethod + " on class " + sourceClass + " is not static.");
            else if (!Types.isClassType(currentMethod.getReturnType()) && next != null)
                throw new SemanticException(".", token.getLine(), "Cannot chain access to method that returns void or basic type.");
        }

        method = currentMethod;
        return currentMethod.getReturnType();
    }

    @Override
    protected void specificCode()
    {
        specificCode(false);
    }

    @Override
    protected void specificCode(boolean chained)
    {
        processThis(chained);
        allocateReturn();
        swapThis();
        for (ExpressionNode argument : arguments)
        {
            argument.generateCode();
            swapThis();
        }
        loadMethodAddress();
        SymbolTable.appendCodeLines("CALL");
    }

    private void allocateReturn()
    {
        if (!method.getReturnType().equals(Types.VOID))
            SymbolTable.appendCodeLines("RMEM 1");
    }

    private void processThis(boolean chained)
    {
        if(chained)
        {
            if(method.isStatic())
                SymbolTable.appendCodeLines("POP\t# Removing target from the stack");
        }
        else if (method.isDynamic())
            SymbolTable.appendCodeLines("LOAD 3");
    }

    private void swapThis()
    {
        if (method.isDynamic())
            SymbolTable.appendCodeLines("SWAP");
    }

    private void loadMethodAddress()
    {
        if (method.isDynamic())
            SymbolTable.appendCodeLines("DUP", "LOADREF 0", "LOADREF " + method.getOffset());
        else
            SymbolTable.appendCodeLines("PUSH " + method.getTag());
    }

    private boolean compareSignature(Method method) throws SemanticException
    {
        Iterator<ExpressionNode> expressionIterator = arguments.iterator();
        Iterator<Parameter> parametersIterator = method.parameters().iterator();
        boolean matchingSignature = true;
        ExpressionNode argument; Parameter formalParameter;

        while (expressionIterator.hasNext() && parametersIterator.hasNext() && matchingSignature)
        {
            argument = expressionIterator.next();
            formalParameter = parametersIterator.next();
            matchingSignature = Types.conformsWith(argument.check(), formalParameter.getType());
        }

        if (expressionIterator.hasNext() || parametersIterator.hasNext())
            matchingSignature = false;

        return matchingSignature;
    }
}

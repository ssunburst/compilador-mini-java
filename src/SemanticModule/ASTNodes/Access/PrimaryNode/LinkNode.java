package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public abstract class LinkNode
{
    protected Token token;
    protected ChainableLinkNode next;
    protected boolean callable;
    protected boolean assignable;
    protected boolean leftSideOfAssignment;

    public LinkNode()
    {
        leftSideOfAssignment = false;
    }

    public boolean isCallable()
    {
        if (hasNext())
            return next.isCallable();
        else
            return callable;
    }

    public boolean isAssignable()
    {
        if (hasNext())
            return next.isAssignable();
        else
            return assignable;
    }

    public Token getToken()
    {
        return token;
    }

    public void setToken(Token token)
    {
        this.token = token;
    }

    public LinkNode chainLink(ChainableLinkNode next)
    {
        this.next = next;
        return next;
    }

    public boolean hasNext()
    {
        return next != null;
    }

    public ChainableLinkNode getNext()
    {
        return next;
    }

    public void setAsLeftSideOfAssignment(boolean leftSide)
    {
        if (next != null)
            next.setAsLeftSideOfAssignment(true);
        else
            leftSideOfAssignment = leftSide;
    }

    public String check() throws SemanticException
    {
        String type = specificChecks();
        if (hasNext()) {
            Class sourceClass = SymbolTable.getClass(type);
            if (sourceClass != null)
                type = next.check(sourceClass);
            else
            {
                String message = "Cannot chain attribute or method access ";
                message += type.equals(Types.VOID)? "- Method returns '" + type + "'." : "to type "+ type + ".";
                throw new SemanticException(".", getToken().getLine(), message);
            }
        }
        return type;
    }

    protected abstract String specificChecks() throws SemanticException;

    public void generateCode()
    {
        specificCode();
        if (next != null)
            next.generateChainedCode();
    }

    protected abstract void specificCode();
}

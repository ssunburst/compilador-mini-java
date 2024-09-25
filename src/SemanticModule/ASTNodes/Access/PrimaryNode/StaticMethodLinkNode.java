package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;

public class StaticMethodLinkNode extends ClassLinkNode
{
    protected MethodLinkNode method;

    public StaticMethodLinkNode()
    {
        super();
        this.callable = true;
    }

    public StaticMethodLinkNode(Token classToken, MethodLinkNode method)
    {
        super(classToken);
        this.callable = true;
        this.method = method;
    }

    public MethodLinkNode getMethod()
    {
        return method;
    }

    public void setMethod(MethodLinkNode method)
    {
        this.method = method;
    }

    @Override
    public Token getToken()
    {
        return method.getToken();
    }

    @Override
    public void setToken(Token token)
    {
        method.setToken(token);
    }

    @Override
    protected String specificChecks() throws SemanticException
    {
        String type = super.specificChecks();
        if (method == null)
            throw new SemanticException(token.getLexeme(), token.getLine(), "Illegal access.");
        type = method.specificChecks(SymbolTable.getClass(type), true);
        return type;
    }

    @Override
    protected void specificCode()
    {
        method.generateCode();
    }
}

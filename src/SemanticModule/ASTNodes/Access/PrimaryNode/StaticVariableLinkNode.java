package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;

public class StaticVariableLinkNode extends ClassLinkNode
{
    protected VariableLinkNode variable;

    public StaticVariableLinkNode()
    {
        super();
        this.assignable = true;
    }

    public StaticVariableLinkNode(Token classToken, VariableLinkNode variable)
    {
        super(classToken);
        this.variable = variable;
        this.assignable = true;
    }

    public VariableLinkNode getVariable()
    {
        return variable;
    }

    public void setVariable(VariableLinkNode variable)
    {
        this.variable = variable;
    }

    @Override
    public Token getToken()
    {
        return variable.getToken();
    }

    @Override
    public void setToken(Token token)
    {
        variable.setToken(token);
    }

    @Override
    protected String specificChecks() throws SemanticException
    {
        String type = super.specificChecks();
        if (variable == null)
            throw new SemanticException(token.getLexeme(), token.getLine(), "Illegal access.");
        type = variable.specificChecks(SymbolTable.getClass(type), true);
        return type;
    }

    @Override
    protected void specificCode()
    {
        variable.generateCode();
    }
}

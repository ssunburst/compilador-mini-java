package SemanticModule.ASTNodes.Statement;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Access.AccessNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class CallNode extends StatementNode
{
    protected Token semicolonToken;
    protected AccessNode access;

    public CallNode()
    {
    }

    public CallNode(Token semicolonToken, AccessNode access)
    {
        this.semicolonToken = semicolonToken;
        this.access = access;
    }

    public CallNode(AccessNode access)
    {
        this.access = access;
    }

    public AccessNode getAccess()
    {
        return access;
    }

    public void setAccess(AccessNode access)
    {
        this.access = access;
    }

    @Override
    public void check() throws SemanticException
    {
        super.check();
        access.check();
        if (!access.isCallable())
            throw new SemanticException(";", access.getToken().getLine(), "Not a constructor o method call.");
    }

    @Override
    public void generateCode()
    {
        access.generateCode();
        if (!access.getType().equals(Types.VOID))
            SymbolTable.appendCodeLines("POP\t# Discarding unused return object");
    }
}

package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;

public class ThisLinkNode extends LinkNode
{
    public ThisLinkNode(Token thisToken)
    {
        this();
        this.token = thisToken;
        this.assignable = false;
        this.callable = false;
    }

    public ThisLinkNode()
    {
        super();
    }

    @Override
    protected String specificChecks() throws SemanticException
    {
        if (SymbolTable.currentUnit().isStatic())
            throw new SemanticException(token.getLexeme(), token.getLine(), "Cannot access 'this' in static method.");
        return SymbolTable.currentClass().getName();
    }

    @Override
    protected void specificCode()
    {
        SymbolTable.appendCodeLines("LOAD 3");
    }
}

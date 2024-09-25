package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.SymbolTable;

public abstract class ClassLinkNode extends LinkNode
{
    public ClassLinkNode()
    {
        super();
    }

    public ClassLinkNode(Token classToken)
    {
        this.token = classToken;
    }

    public Token getClassToken()
    {
        return token;
    }

    public void setClassToken(Token classToken)
    {
        this.token = classToken;
    }

    @Override
    protected String specificChecks() throws SemanticException
    {
        String className = token.getLexeme();
        Class actualClass = SymbolTable.getClass(className);
        if (actualClass == null)
            throw new SemanticException(className, token.getLine(), className + " is not a valid class name.");
        return className;
    }
}

package SemanticModule.ASTNodes.VariableType;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;

public abstract class VariableTypeNode
{
    Token typeToken;

    public VariableTypeNode(Token typeToken)
    {
        this.typeToken = typeToken;
    }

    public Token getTypeToken()
    {
        return typeToken;
    }

    public void setTypeToken(Token typeToken)
    {
        this.typeToken = typeToken;
    }

    public abstract String check() throws SemanticException;
}

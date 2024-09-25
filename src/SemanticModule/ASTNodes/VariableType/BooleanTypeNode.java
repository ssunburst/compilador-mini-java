package SemanticModule.ASTNodes.VariableType;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.Types;

public class BooleanTypeNode extends VariableTypeNode
{
    public BooleanTypeNode(Token typeToken)
    {
        super(typeToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (typeToken.getTokenID() != Token.KEYWORD_BOOLEAN)
            throw new SemanticException(typeToken.getLexeme(), typeToken.getLine(), "Expected 'boolean' keyword.");
        return Types.BOOLEAN;
    }
}

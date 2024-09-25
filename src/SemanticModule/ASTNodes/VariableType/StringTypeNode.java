package SemanticModule.ASTNodes.VariableType;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.Types;

public class StringTypeNode extends VariableTypeNode
{
    public StringTypeNode(Token typeToken)
    {
        super(typeToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (typeToken.getTokenID() != Token.KEYWORD_STRING)
            throw new SemanticException(typeToken.getLexeme(), typeToken.getLine(), "Expected 'String' keyword.");
        return Types.STRING;
    }
}

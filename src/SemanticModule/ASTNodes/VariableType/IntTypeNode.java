package SemanticModule.ASTNodes.VariableType;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.Types;

public class IntTypeNode extends VariableTypeNode
{
    public IntTypeNode(Token typeToken)
    {
        super(typeToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (typeToken.getTokenID() != Token.KEYWORD_INT)
            throw new SemanticException(typeToken.getLexeme(), typeToken.getLine(), "Expected 'int' keyword.");
        return Types.INT;
    }
}

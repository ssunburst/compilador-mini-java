package SemanticModule.ASTNodes.VariableType;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.Types;

public class CharTypeNode extends VariableTypeNode
{
    public CharTypeNode(Token typeToken)
    {
        super(typeToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (typeToken.getTokenID() != Token.KEYWORD_CHAR)
            throw new SemanticException(typeToken.getLexeme(), typeToken.getLine(), "Expected 'char' keyword.");
        return Types.CHAR;
    }
}

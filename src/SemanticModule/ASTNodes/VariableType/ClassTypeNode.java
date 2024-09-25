package SemanticModule.ASTNodes.VariableType;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;

public class ClassTypeNode extends VariableTypeNode
{
    public ClassTypeNode(Token typeToken)
    {
        super(typeToken);
    }

    @Override
    public String check() throws SemanticException
    {
        if (SymbolTable.getClass(typeToken.getLexeme()) == null)
            throw new SemanticException(typeToken.getLexeme(), typeToken.getLine(), "Unknown type " + typeToken.getLexeme() + ".");
        return typeToken.getLexeme();
    }
}

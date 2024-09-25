package SemanticModule.ASTNodes.Access;

import Exceptions.SemanticExceptions.SemanticException;
import Exceptions.SemanticExceptions.UndefinedTypeException;
import LexicalModule.Token;
import SemanticModule.SymbolTable;

public class CastingNode
{
    Token castedClass;
    
    public CastingNode(Token castedClass)
    {
        super();
        this.castedClass = castedClass;
    }

    public Token getToken()
    {
        return castedClass;
    }

    public String check() throws SemanticException
    {
        if (SymbolTable.getClass(castedClass.getLexeme()) == null)
            throw new UndefinedTypeException(castedClass.getLexeme(), castedClass.getLine(), "Unable to cast to class " + castedClass.getLexeme() +
                    " - Class has not been declared.");
        return castedClass.getLexeme();
    }
}

package SemanticModule.ASTNodes.Expression;


import Exceptions.SemanticExceptions.SemanticException;

public abstract class ExpressionNode
{
    public abstract String check() throws SemanticException;

    public abstract void generateCode();
}

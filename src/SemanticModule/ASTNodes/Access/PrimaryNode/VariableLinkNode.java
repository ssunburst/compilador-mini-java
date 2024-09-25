package SemanticModule.ASTNodes.Access.PrimaryNode;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Statement.Block.LocalVariable;
import SemanticModule.Declarations.Attribute;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.SymbolTable;
import SemanticModule.Types;
import SemanticModule.Variable;

import java.util.Iterator;

public class VariableLinkNode extends ChainableLinkNode
{
    protected Variable variable;

    public VariableLinkNode()
    {
        super();
    }

    public VariableLinkNode(Token variableToken)
    {
        this.token = variableToken;
        this.callable = false;
        this.assignable = true;
    }

    @Override
    protected String specificChecks() throws SemanticException
    {
        String type;
        LocalVariable variable = SymbolTable.currentBlock().resolveName(token.getLexeme());
         if (variable != null)
         {
             this.variable = variable;
             type = variable.getType();
         }
        else
            type = specificChecks(SymbolTable.currentClass(), SymbolTable.currentUnit().isStatic());
        return type;
    }

    @Override
    protected String specificChecks(Class sourceClass) throws SemanticException
    {
        return specificChecks(sourceClass, false);
    }

    public String specificChecks(Class sourceClass, boolean mustBeStatic) throws SemanticException
    {
        Iterator<Attribute> attributeIterator = sourceClass.getAttribute(token.getLexeme()).iterator();
        Class currentClass = sourceClass;
        Attribute currentAttribute, correctAttribute = null;

        while(attributeIterator.hasNext() && correctAttribute == null)
        {
            currentAttribute = attributeIterator.next();
            if (currentAttribute.getName().equals(token.getLexeme()) && currentAttribute.getSourceClass() == currentClass)
                correctAttribute = currentAttribute;
            else if (!attributeIterator.hasNext() && !currentClass.isTopClass())
            {
                currentClass = currentClass.getParent();
                attributeIterator = sourceClass.getAttribute(token.getLexeme()).iterator();
            }
        }

        if (correctAttribute == null)
            throw new SemanticException(token.getLexeme(), token.getLine(), "Cannot resolve name " + token.getLexeme() + ".");

        boolean mustBePublic = currentClass != SymbolTable.currentClass();

        if (mustBePublic && correctAttribute.isPrivate())
            throw new SemanticException(token.getLexeme(), token.getLine(), "Attribute " + token.getLexeme() + " from class " + currentClass +" is private.");
        else if (mustBeStatic && !correctAttribute.isStatic())
            throw new SemanticException(token.getLexeme(), token.getLine(), "Cannot access attribute " + token.getLexeme() + " in class " + sourceClass + " - attribute is not static.");

        String returnType = correctAttribute.getType();
        if (Types.isBasicType(returnType) && (next != null))
            throw new SemanticException(".", token.getLine(), "Cannot append access to a variable of type " + returnType + ".");

        variable = correctAttribute;
        return returnType;
    }

    @Override
    protected void specificCode()
    {
        specificCode(false);
    }

    @Override
    protected void specificCode(boolean chained)
    {
        if (leftSideOfAssignment)
            variable.generateLeftSideCode(chained);
        else
            variable.generateRightSideCode(chained);
    }
}

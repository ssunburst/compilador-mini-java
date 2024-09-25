package SemanticModule.ASTNodes.Access;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Access.PrimaryNode.ChainableLinkNode;
import SemanticModule.ASTNodes.Access.PrimaryNode.LinkNode;
import SemanticModule.ASTNodes.Operand.OperandNode;
import SemanticModule.Types;

public class AccessNode extends OperandNode
{
    LinkNode first;
    LinkNode last;
    String type;

    public AccessNode()
    {
        super();
        type = Types.VOID;
    }

    public AccessNode(LinkNode first)
    {
        this();
        this.first = first;
        this.last = first;
    }

    public String getType()
    {
        return type;
    }

    public boolean isAssignable()
    {
        return last.isAssignable();
    }

    public boolean isCallable()
    {
        return last.isCallable();
    }

    public LinkNode getFirst()
    {
        return first;
    }

    public void setFirst(LinkNode primary)
    {
        this.first = primary;
        this.last = primary;
    }

    public LinkNode getLast()
    {
        return last;
    }

    public Token getToken()
    {
        return last.getToken();
    }

    public ChainableLinkNode chainAccess(ChainableLinkNode link)
    {
        last.chainLink(link);
        last = link;
        return link;
    }

    public void setAsLeftSideOfAssignment(boolean leftSide)
    {
        first.setAsLeftSideOfAssignment(leftSide);
    }

    public String check() throws SemanticException
    {
        type = first.check();
        return type;
    }

    @Override
    public void generateCode()
    {
        first.generateCode();
    }
}

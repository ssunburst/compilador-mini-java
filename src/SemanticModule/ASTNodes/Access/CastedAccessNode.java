package SemanticModule.ASTNodes.Access;

import Exceptions.SemanticExceptions.IllegalCastingException;
import Exceptions.SemanticExceptions.SemanticException;
import SemanticModule.ASTNodes.Access.PrimaryNode.LinkNode;
import SemanticModule.Types;

public class CastedAccessNode extends AccessNode
{
    protected CastingNode casting;

    public CastedAccessNode()
    {
        super();
    }

    public CastedAccessNode(LinkNode primary)
    {
        super(primary);
    }

    public CastedAccessNode(CastingNode casting, LinkNode primaryNode)
    {
        super(primaryNode);
        this.casting = casting;
    }

    public CastingNode getCasting()
    {
        return casting;
    }

    public void setCasting(CastingNode casting)
    {
        this.casting = casting;
    }

    @Override
    public String check() throws SemanticException
    {
        String accessType = super.check();
        String castingType = casting.check();
        if (!Types.conformsWith(castingType, accessType))
            throw new IllegalCastingException(castingType, casting.getToken().getLine(), "Unable to cast from " + accessType + " to " + castingType + ".");
        return castingType;
    }
}

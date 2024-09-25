package SemanticModule.ASTNodes.Statement.Assignmnent;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Access.AccessNode;
import SemanticModule.ASTNodes.Statement.StatementNode;

public abstract class AssignmentNode extends StatementNode
{
    protected AccessNode leftSide;
    protected Token assignmentToken;

    public AssignmentNode()
    {
        super();
    }

    public AssignmentNode(AccessNode leftSide, Token assignmentToken)
    {
        this.leftSide = leftSide;
        this.assignmentToken = assignmentToken;
    }

    public AccessNode getLeftSide()
    {
        return leftSide;
    }

    public void setLeftSide(AccessNode leftSide)
    {
        this.leftSide = leftSide;
    }

    public Token getAssignmentToken()
    {
        return assignmentToken;
    }

    public void setAssignmentToken(Token assignmentToken)
    {
        this.assignmentToken = assignmentToken;
    }

    @Override
    public void check() throws SemanticException
    {
        super.check();
        String accessType = checkAccess();
        validateAssignment(accessType);
    }

    protected String checkAccess() throws SemanticException
    {
        String accessType = leftSide.check();
        if (!leftSide.isAssignable())
            throw new SemanticException(assignmentToken.getLexeme(), assignmentToken.getLine(), "Illegal assignment.");
        return accessType;
    }

    protected abstract void validateAssignment(String accessType) throws SemanticException;

    public void generateCode()
    {
        generateRightSide();
        leftSide.setAsLeftSideOfAssignment(true);
        leftSide.generateCode();
    }

    protected abstract void generateRightSide();
}

package SemanticModule.ASTNodes.Statement.Assignmnent;

import Exceptions.SemanticExceptions.InvalidOperatorException;
import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Access.AccessNode;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Types;

public class ClassicAssignmentNode extends AssignmentNode
{
    protected ExpressionNode rightSide;

    public ClassicAssignmentNode()
    {
        super();
    }

    public ClassicAssignmentNode(AccessNode access, Token assignmentToken, ExpressionNode rightSide)
    {
        super(access, assignmentToken);
        this.rightSide = rightSide;
    }

    public ExpressionNode getRightSide()
    {
        return rightSide;
    }

    public void setRightSide(ExpressionNode rightSide)
    {
        this.rightSide = rightSide;
    }

    @Override
    protected void validateAssignment(String accessType) throws SemanticException
    {
        if (assignmentToken.getTokenID() != Token.OPERATOR_ASSIGN)
            throw new InvalidOperatorException(assignmentToken.getLexeme(), assignmentToken.getLine(), "Expected '==' operator.");

        String rightType = rightSide.check();
        if (!Types.conformsWith(rightType, accessType))
            throw new SemanticException(assignmentToken.getLexeme(), assignmentToken.getLine(), "Cannot assign " + rightType + " to " + accessType + ".");
    }

    @Override
    protected void generateRightSide()
    {
        rightSide.generateCode();
    }
}

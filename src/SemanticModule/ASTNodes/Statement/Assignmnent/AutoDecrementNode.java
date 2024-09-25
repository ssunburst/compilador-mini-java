package SemanticModule.ASTNodes.Statement.Assignmnent;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Access.AccessNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class AutoDecrementNode extends AssignmentNode
{
    public AutoDecrementNode()
    {
    }

    public AutoDecrementNode(AccessNode access, Token assignmentToken)
    {
        super(access, assignmentToken);
    }

    @Override
    protected void validateAssignment(String accessType) throws SemanticException
    {
        if (assignmentToken.getTokenID() != Token.OPERATOR_DECREMENT)
            throw new SemanticException(assignmentToken.getLexeme(), assignmentToken.getLine(), "Expected '--' operator");
        else if (!Types.conformsWith(accessType, Types.INT))
            throw new SemanticException(assignmentToken.getLexeme(), assignmentToken.getLine(), "Unable to apply '--' to anything other than integer.");
    }

    @Override
    protected void generateRightSide()
    {
        leftSide.generateCode();
        SymbolTable.appendCodeLines("PUSH 1", "SUB");
    }
}

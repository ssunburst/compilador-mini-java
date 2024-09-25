package SemanticModule.ASTNodes.Statement.Assignmnent;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Access.AccessNode;
import SemanticModule.SymbolTable;
import SemanticModule.Types;

public class AutoIncrementNode extends AssignmentNode
{
    public AutoIncrementNode()
    {
    }

    public AutoIncrementNode(AccessNode access, Token assignmentToken)
    {
        super(access, assignmentToken);
    }

    @Override
    public void validateAssignment(String accessType) throws SemanticException
    {
        if (assignmentToken.getTokenID() != Token.OPERATOR_INCREMENT)
            throw new SemanticException(assignmentToken.getLexeme(), assignmentToken.getLine(), "Expected '++' operator");
        else if (!Types.conformsWith(accessType, Types.INT))
            throw new SemanticException(assignmentToken.getLexeme(), assignmentToken.getLine(), "Unable to apply '++' to anything other than integer.");
    }

    @Override
    protected void generateRightSide()
    {
        leftSide.generateCode();
        SymbolTable.appendCodeLines("PUSH 1", "ADD");
    }
}

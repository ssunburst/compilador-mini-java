package SemanticModule.ASTNodes.Statement;

import Exceptions.SemanticExceptions.SemanticException;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.SymbolTable;

public class StatementNode
{
    public void check() throws SemanticException
    {
        BlockNode currentBlock = SymbolTable.currentBlock();
        if (!currentBlock.isChecked() && currentBlock.returnReached())
            throw new SemanticException(currentBlock.getReturnToken().getLexeme(), currentBlock.getReturnToken().getLine(), "Dead code - return statement already reached.");
    }

    public void generateCode()
    {
    }
}

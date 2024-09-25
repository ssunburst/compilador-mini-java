package SemanticModule.ASTNodes.Statement;

import Exceptions.SemanticExceptions.SemanticException;
import LexicalModule.Token;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.Declarations.Units.Unit;
import SemanticModule.SymbolTable;
import SemanticModule.Types;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;

public class ReturnNode extends StatementNode
{

    Token returnToken;
    ExpressionNode returnExpression;

    public ReturnNode() {}

    public ReturnNode(Token returnToken)
    {
        this.returnToken = returnToken;
    }

    public ReturnNode(Token returnToken, ExpressionNode returnExpression)
    {
        this.returnToken = returnToken;
        this.returnExpression = returnExpression;
    }

    public Token getReturnToken()
    {
        return returnToken;
    }

    public void setReturnToken(Token returnToken)
    {
        this.returnToken = returnToken;
    }

    public ExpressionNode getReturnExpression()
    {
        return returnExpression;
    }

    public void setReturnExpression(ExpressionNode returnExpression)
    {
        this.returnExpression = returnExpression;
    }

    @Override
    public void check() throws SemanticException
    {
        super.check();
        checkReturnExpression();
        SymbolTable.currentBlock().setReturnToken(returnToken);
    }

    private void checkReturnExpression() throws SemanticException
    {
        String returnType = SymbolTable.currentUnit().getReturnType();
        if (returnExpression == null) {
            if (!returnType.equals(Types.VOID))
                throw new SemanticException(returnToken.getLexeme(), returnToken.getLine(), "Method " + returnToken.getLexeme() + " must return " + returnType + ".");
        }
        else
        {
            if (returnType.equals(Types.VOID))
                throw new SemanticException(returnToken.getLexeme(), returnToken.getLine(), "Illegal return - Method output type is void.");
            String expressionType = returnExpression.check();
            if (!Types.conformsWith(expressionType, returnType))
                throw new SemanticException(returnToken.getLexeme(), returnToken.getLine(), "Return type " + expressionType + " does not conform with expected type " + returnType + ".");
        }
    }

    @Override
    public void generateCode()
    {
        BlockNode sourceBlock = SymbolTable.currentBlock();
        int freeSize = -sourceBlock.getVariableOffset();

        Unit sourceUnit = SymbolTable.currentUnit();
        int returnSize = sourceUnit.parameterSize() + (sourceUnit.isDynamic()? 1 : 0);

        if (returnExpression != null)
        {
            returnExpression.generateCode();
            int returnValueOffset = 3 + returnSize;
            SymbolTable.appendCodeLines("STORE " + returnValueOffset, "FMEM " + freeSize);
        }

        SymbolTable.appendCodeLines("STOREFP", "RET " + returnSize);
    }
}

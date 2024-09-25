package SemanticModule.ASTNodes.Operand.Literal;

import LexicalModule.Token;
import SemanticModule.SymbolTable;

public class FalseLiteralNode extends BooleanLiteralNode
{
    public FalseLiteralNode()
    {
    }

    public FalseLiteralNode(Token literalToken)
    {
        super(literalToken);
    }

    @Override
    public void generateCode()
    {
        SymbolTable.appendCodeLines("PUSH 0");
    }
}

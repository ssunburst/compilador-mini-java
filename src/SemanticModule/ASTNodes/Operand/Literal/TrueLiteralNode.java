package SemanticModule.ASTNodes.Operand.Literal;

import LexicalModule.Token;
import SemanticModule.SymbolTable;

public class TrueLiteralNode extends BooleanLiteralNode
{
    public TrueLiteralNode()
    {
    }

    public TrueLiteralNode(Token literalToken)
    {
        super(literalToken);
    }

    @Override
    public void generateCode()
    {
        SymbolTable.appendCodeLines("PUSH 1");
    }
}

package Utilities;

import LexicalModule.Token;

public class TokenInspector
{
    public static boolean isMathematicaOperator(Token token)
    {
        return isTokenAmong(token, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_MULTIPLICATION,
                Token.OPERATOR_DIVISION, Token.OPERATOR_MODULO);
    }

    public static boolean isBooleanOperator(Token token)
    {
        return isTokenAmong(token, Token.OPERATOR_AND, Token.OPERATOR_OR);
    }

    public static boolean isEqualityOperator(Token token)
    {
        return isTokenAmong(token, Token.OPERATOR_EQUALS, Token.OPERATOR_NOT_EQUALS);
    }

    public static boolean isRelationalOperator(Token token)
    {
        return isTokenAmong(token, Token.OPERATOR_GREATER, Token.OPERATOR_GREATER_OR_EQUAL, Token.OPERATOR_LESSER, Token.OPERATOR_LESSER_OR_EQUAL);
    }
    
    public static boolean isTokenAmong(Token token, int... ids)
    {
        for (int id : ids)
        {
            if (token.getTokenID() == id)
                return true;
        }
        return false;
    }
}

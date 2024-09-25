package LexicalModule;

import java.util.HashMap;
import java.util.Map;

public class KeywordTable
{
    private Map<String, Integer> table;
    private static KeywordTable instance;

    private KeywordTable()
    {
        table = new HashMap<String, Integer>();

        table.put("class", Token.KEYWORD_CLASS);
        table.put("extends", Token.KEYWORD_EXTENDS);
        table.put("static", Token.KEYWORD_STATIC);
        table.put("dynamic", Token.KEYWORD_DYNAMIC);
        table.put("void", Token.KEYWORD_VOID);
        table.put("boolean", Token.KEYWORD_BOOLEAN);
        table.put("char", Token.KEYWORD_CHAR);
        table.put("int", Token.KEYWORD_INT);
        table.put("String", Token.KEYWORD_STRING);
        table.put("public", Token.KEYWORD_PUBLIC);
        table.put("private", Token.KEYWORD_PRIVATE);
        table.put("if", Token.KEYWORD_IF);
        table.put("else", Token.KEYWORD_ELSE);
        table.put("for", Token.KEYWORD_FOR);
        table.put("return", Token.KEYWORD_RETURN);
        table.put("this", Token.KEYWORD_THIS);
        table.put("new", Token.KEYWORD_NEW);
        table.put("null", Token.KEYWORD_NULL);
        table.put("true", Token.KEYWORD_TRUE);
        table.put("false", Token.KEYWORD_FALSE);
    }

    public static KeywordTable getInstance()
    {
        if(instance == null)
            instance = new KeywordTable();
        return instance;
    }

    public Integer getToken(String lexeme)
    {
        return table.get(lexeme);
    }

}

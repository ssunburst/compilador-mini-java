package LexicalModule;

import java.util.HashMap;
import java.util.Map;

public class TokenIndex
{
    private Map<Integer, String> index;
    private static TokenIndex instance;

    private TokenIndex()
    {
        index = new HashMap<Integer, String>();

        index.put(-1, "End of File");
        index.put(0, "Class identifier");
        index.put(1, "Method or variable identifier");
        index.put(2, "char literal");
        index.put(3, "int literal");
        index.put(4, "String literal");
        index.put(5, "&&");
        index.put(6, "/");
        index.put(7, "==");
        index.put(8, ">");
        index.put(9, ">=");
        index.put(10,"<");
        index.put(11, "<=");
        index.put(12, "-");
        index.put(13, "%");
        index.put(14, "*");
        index.put(15, "!");
        index.put(16, "!=");
        index.put(17, "||");
        index.put(18, "+");
        index.put(19, "}");
        index.put(20, ")");
        index.put(21, ",");
        index.put(22, "{");
        index.put(23, "(");
        index.put(24, ".");
        index.put(25, ";");
        index.put(26, "class");
        index.put(27, "extends");
        index.put(28, "static");
        index.put(29, "dynamic");
        index.put(30, "void");
        index.put(31, "boolean");
        index.put(32, "char");
        index.put(33, "int");
        index.put(34, "String");
        index.put(35, "public");
        index.put(36, "private");
        index.put(37, "if");
        index.put(38, "else");
        index.put(39, "for");
        index.put(40, "return");
        index.put(41, "this");
        index.put(42, "new");
        index.put(43, "null");
        index.put(44, "true");
        index.put(45, "false");
        index.put(46, "=");
        index.put(47, "++");
        index.put(48, "--");
        index.put(49, ":");
    }

    public static TokenIndex getInstance()
    {
        if(instance == null)
            instance = new TokenIndex();
        return instance;
    }

    public String getTokenString(int tokenCode)
    {
        return index.get(tokenCode);
    }
}

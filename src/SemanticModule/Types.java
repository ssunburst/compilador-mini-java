package SemanticModule;

import Exceptions.SemanticExceptions.UndefinedParentClassException;
import Exceptions.SemanticExceptions.UndefinedTypeException;
import SemanticModule.Declarations.Classes.Class;

import java.util.*;

public class Types
{
    private static Types instance;

    private static Map<String, List<String>> basicTypes = new HashMap<>();
    private static boolean mapFilled = false;

    public static final String INT = "int";
    public static final String CHAR = "char";
    public static final String BOOLEAN = "boolean";
    public static final String STRING = "String";
    public static final String VOID = "void";
    public static final String NULL = "null";
    public static final String UNKNOWN = "-";

    public static boolean conformsWith(String givenType, String expectedType) throws UndefinedTypeException, UndefinedParentClassException {
        boolean conforms;
        if(isBasicType(givenType) || isBasicType(expectedType))
        {
            List<String> admissibleTypes = basicTypes.get(givenType);
            if (admissibleTypes == null)
                conforms = false;
            else
                conforms = admissibleTypes.contains(expectedType);
        }
        else if (givenType.equals(NULL) || expectedType.equals(NULL))
            conforms = true;
        else
        {
            Class givenClass = SymbolTable.getClass(givenType);
            Class expectedClass = SymbolTable.getClass(expectedType);
            conforms = (givenClass != null) && (expectedClass != null) && givenClass.isSubtypeOf(expectedClass);
        }
        return conforms;
    }

    public static boolean isClassType(String type)
    {
        return SymbolTable.getClass(type) != null;
    }

    public static boolean isValid(String type)
    {
        return isBasicType(type) || SymbolTable.getClass(type) != null;
    }

    public static boolean isValidReturnType(String type)
    {
        return type.equals(Types.VOID) || isValid(type);
    }

    public static boolean isBasicType(String type)
    {
        checkMapStatus();
        return basicTypes.get(type) != null;
    }

    private static void checkMapStatus()
    {
        if (!mapFilled)
        {
            fillMapWithAdmissibleTypes(INT, INT);
            fillMapWithAdmissibleTypes(CHAR, CHAR);
            fillMapWithAdmissibleTypes(STRING, STRING);
            fillMapWithAdmissibleTypes(BOOLEAN, BOOLEAN);
        }
        mapFilled = true;
    }

    private static void fillMapWithAdmissibleTypes(String type, String... types)
    {
        List<String> admissibleByType = new LinkedList<>();
        for (String admissibleType : types)
            admissibleByType.add(admissibleType);
        basicTypes.put(type, admissibleByType);
    }

}

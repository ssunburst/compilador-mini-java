package SemanticModule;

import Exceptions.SemanticExceptions.*;
import Exceptions.SemanticExceptions.CircularInheritanceException;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.ASTNodes.Statement.Block.LocalVariable;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.Declarations.Classes.ObjectClass;
import SemanticModule.Declarations.Parameter;
import SemanticModule.Declarations.Units.Constructor;
import SemanticModule.Declarations.Units.Method;
import SemanticModule.Declarations.Units.Unit;
import SemanticModule.Declarations.Classes.SystemClass;

import java.util.Iterator;
import java.util.Stack;

public class SymbolTable
{
    private static Class currentClass;
    private static Unit currentUnit;
    private static Stack<BlockNode> blockStack = new Stack<>();
    private static IterableDictionary<String, Class> classes = new IterableDictionary<>();

    public static final int PREDEFINED = 0;
    public static final String HEAP_INIT_TAG = "simple_heap_init";
    public static final String MALLOC_TAG = "simple_malloc";

    private static Method mainMethod;

    private static int ifCount = 0;
    private static int forCount = 0;

    private static StringBuilder code = new StringBuilder();

    // Métodos

    public static Class currentClass()
    {
        return currentClass;
    }

    public static void setCurrentClass(Class newCurrentClass)
    {
        currentClass = newCurrentClass;
    }

    public static Unit currentUnit()
    {
        return currentUnit;
    }

    public static void setCurrentUnit(Unit newCurrentUnit)
    {
        currentUnit = newCurrentUnit;
    }

    public static BlockNode currentBlock()
    {
        BlockNode currentBlock = null;
        if (!blockStack.isEmpty())
            currentBlock = blockStack.peek();
        return currentBlock;
    }

    public static void pushBlock(BlockNode block)
    {
        blockStack.push(block);
    }

    public static void popBlock()
    {
        blockStack.pop();
    }

    public static void flushBlocks()
    {
        blockStack = new Stack<>();
    }

    public static Class getClass(String className)
    {
        return classes.get(className);
    }

    public static void addClass(Class newClass) throws PreexistingClassException
    {
        try {
            classes.secureInsert(newClass.getName(), newClass);
        } catch (PreexistingKeyException e) {
            Class previousClass = classes.get(newClass.getName());
            throw new PreexistingClassException(newClass.getName(), newClass.getLine(), "Class " + newClass.getName() + " has already been declared in line " + previousClass.getLine() + ".");
        }
    }

    public static Iterable<Class> classes()
    {
        return classes;
    }

    public static void addPredefinedClasses()
    {
        classes.insert(ObjectClass.getInstance().getName(), ObjectClass.getInstance());
        classes.insert(SystemClass.getInstance().getName(), SystemClass.getInstance());
    }

    public static void checkDefinition() throws CircularInheritanceException, UndefinedTypeException, UndefinedParentClassException
    {
        for (Class c : classes)
            c.checkDefinition();
    }

    public static void consolidate() throws UndefinedTypeException, IllegalOverrideException, UndefinedParentClassException
    {
        for (Class c : classes)
            c.consolidate();
    }

    public static void checkMainMethod() throws NoMainMethodFoundException
    {
        Iterator<Class> classIterator = classes.iterator();
        Iterator<Method> methodIterator;
        Method currentMethod = null;
        Class currentClass = null;
        boolean mainMethodFound = false;

        while (classIterator.hasNext() && !mainMethodFound) {
            currentClass = classIterator.next();
            methodIterator = currentClass.methods().iterator();
            while (methodIterator.hasNext() && !mainMethodFound) {
                currentMethod = methodIterator.next();
                mainMethodFound = currentMethod.getName().equals("main") && currentMethod.parameterSize() == 0 && currentMethod.isStatic() && currentMethod.getReturnType().equals(Types.VOID);
            }
        }

        if (!mainMethodFound)
            throw new NoMainMethodFoundException("main", PREDEFINED, "No main method found.");
        else
            mainMethod = currentMethod;
    }

    public static void checkStatements() throws SemanticException
    {
        for (Class currentClass : classes) {
            setCurrentClass(currentClass);
            for (Constructor currentConstructor : currentClass.constructors())
                checkUnitBlocks(currentConstructor);
            for (Method method : currentClass.methods())
                checkUnitBlocks(method);
        }
    }

    private static void checkUnitBlocks(Unit unit) throws SemanticException
    {
        flushBlocks();
        setCurrentUnit(unit);
        BlockNode block = unit.getBlock();
        int parameterOffset = unit.isStatic()? 3 : 4;
        for (Parameter parameter : unit.parameters())
            block.addVariable(parameter.getType(), parameter.getName(), parameterOffset++);
        block.check();
    }

    public static String generateCode()
    {
        appendCodeLines(".CODE");
        generateStartupCode();
        generateHeapInitialization();
        generateMalloc();
        for (Class currentClass : classes)
            currentClass.generateCode();
        return code.toString();
    }

    private static void generateHeapInitialization()
    {
        appendCodeLines(HEAP_INIT_TAG + ": RET 0");
    }

    private static void generateMalloc()
    {
        appendCodeLines(MALLOC_TAG + ": LOADFP", "LOADSP", "STOREFP", "LOADHL", "DUP", "PUSH 1", "ADD", "STORE 4", "LOAD 3", "ADD",
                "STOREHL", "STOREFP", "RET 1");
    }

    private static void generateStartupCode()
    {
        appendCodeLines("PUSH " + HEAP_INIT_TAG, "CALL", "PUSH " + mainMethod.getTag(), "CALL", "HALT");
    }

    public static void appendCodeLines(String... lines)
    {
        for (String line : lines)
            code.append(line + "\n");
    }

    public static void flush()
    {
        currentClass = null;
        currentUnit = null;
        blockStack = new Stack<>();
        mainMethod = null;
        ifCount = 0;
        forCount = 0;
        classes = new IterableDictionary<>();
    }

    public static int ifCount()
    {
        return ++ifCount;
    }

    public static int forCount()
    {
        return ++forCount;
    }
}

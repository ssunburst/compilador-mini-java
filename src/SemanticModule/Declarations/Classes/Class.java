package SemanticModule.Declarations.Classes;

import Exceptions.SemanticExceptions.*;
import SemanticModule.Declarations.Attribute;
import SemanticModule.Declarations.Declarable;
import SemanticModule.Types;
import SemanticModule.Declarations.Units.Constructor;
import SemanticModule.Declarations.Units.Method;
import SemanticModule.IterableDictionary;
import SemanticModule.SymbolTable;

import java.util.LinkedList;
import java.util.List;

public class Class implements Declarable
{
    protected String name;
    protected int line;
    protected String parent;

    protected IterableDictionary<String, Attribute> attributes;
    protected IterableDictionary<String, Constructor> constructors;
    protected IterableDictionary<String, Method> methods;

    protected int attributeOffset;
    protected int classVariableOffset;
    protected int methodOffset;

    protected boolean consolidated;

    // Constructores de clase

    protected Class()
    {
        line = SymbolTable.PREDEFINED;

        attributes = new IterableDictionary<>();
        constructors = new IterableDictionary<>();
        methods = new IterableDictionary<>();

        attributeOffset = 1;
        classVariableOffset = 0;
        methodOffset = 0;

        consolidated = false;
    }

    public Class(String name, int line)
    {
        this(name, line, ObjectClass.getInstance().getName());
    }

    public Class(String name, int line, String parent)
    {
        this();
        this.name = name;
        this.line = line;
        this.parent = parent;
    }

    @Override
    public int getLine()
    {
        return line;
    }

    public void setLine(int line)
    {
        this.line = line;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Class getParent() throws UndefinedParentClassException
    {
        Class parentClass = null;
        if (!isTopClass())
        {
            parentClass = SymbolTable.getClass(parent);
            if (parentClass == null)
                throw new UndefinedParentClassException(parent, line, "Parent class for " + name + " has not been declared.");
        }
        return parentClass;
    }

    public void setParent(String parent)
    {
        this.parent = parent;
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------

    // Atributos

    public Iterable<Attribute> getAttribute(String attributeName)
    {
        return attributes.getAll(attributeName);
    }

    public void addAttribute(Attribute attribute) throws RepeatedAttributeNameException
    {
        try {
            attributes.secureInsert(attribute.getName(), attribute);
            attribute.setSourceClass(this);
        } catch (PreexistingKeyException e) {
            throw new RepeatedAttributeNameException(attribute.getName(), attribute.getLine(), "Class " + name + " already has an attribute with name " + attribute.getName() + ".");
        }
    }

    public Iterable<Attribute> attributes()
    {
        return attributes;
    }

    public int attributeSize()
    {
        return attributes.size();
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------

    // Constructores

    public Iterable<Constructor> constructors()
    {
        return constructors.getAll(name);
    }

    public void addConstructor(Constructor constructor) throws IllegalConstructorException
    {
        if (!constructor.getName().equals(name))
            throw new IllegalConstructorException(constructor.getName(), constructor.getLine(), "Constructor must have the same name as the class it belongs to.");
        for (Constructor previousConstructor : constructors)
        {
            if (constructor.equals(previousConstructor))
                throw new IllegalConstructorException(constructor.getName(), constructor.getLine(), "Class " + name + " already has a consutructor with the same amount and type of arguments.");
        }
        constructors.insert(name, constructor);
        constructor.setSourceClass(this);
    }

    public int constructorSize()
    {
        return constructors.size();
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------

    // Métodos

    public Iterable<Method> getMethod(String methodName)
    {
        if (methods.amountOf(methodName) == 0)
            return null;
        else
            return methods.getAll(methodName);
    }

    public void addMethod(Method method) throws IllegalMethodException
    {
        Iterable<Method> presentMethods = methods.getAll(method.getName());
        for (Method pm : methods.getAll(method.getName()))
        {
            if (method.equals(pm))
                throw new IllegalMethodException(method.getName(), method.getLine(), "Method " + method + " has identical signature to " + pm + " in class " + pm.getSourceClass() + ".");
        }
        methods.insert(method.getName(), method);
        method.setSourceClass(this);
    }

    public Iterable<Method> methods()
    {
        return methods;
    }

    public int methodSize()
    {
        return methods.size();
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------


    public int getAttributeOffset()
    {
        return attributeOffset;
    }

    public int getMethodOffset()
    {
        return methodOffset;
    }

    protected void updateOffsets() throws UndefinedParentClassException
    {
        if (!isTopClass())
        {
            attributeOffset = getParent().getAttributeOffset();
            methodOffset = getParent().getMethodOffset();
        }
    }

    public boolean isTopClass()
    {
        return parent == null;
    }

    public void checkDefinition() throws UndefinedParentClassException, CircularInheritanceException, UndefinedTypeException
    {
        if (!isTopClass())
        {
            if (SymbolTable.getClass(parent) == null)
                throw new UndefinedParentClassException(parent, line, "Parent class " + parent + " for " + name + " has not been declared.");
        }
        else
            checkDefaultParent();

        checkCircularInheritance();
        checkConstructorExistence();
        checkAttributes();
        checkUnits();
    }

    protected void checkDefaultParent()
    {
        if (parent == null)
            parent = ObjectClass.OBJECT;
    }

    private void checkConstructorExistence()
    {
        if(constructors.size() == 0)
        {
            Constructor defaultConstructor = new Constructor(name, SymbolTable.PREDEFINED);
            defaultConstructor.setSourceClass(this);
            constructors.insert(defaultConstructor.getName(), defaultConstructor);
        }
    }

    public boolean isSubtypeOf(Class superclass) throws UndefinedParentClassException
    {
        if (equals(superclass))
            return true;
        else if (isTopClass())
            return false;
        else
        {
            Class parentClass = getParent();
            return parentClass.isSubtypeOf(superclass);
        }
    }

    private void checkCircularInheritance() throws CircularInheritanceException, UndefinedParentClassException
    {
        if (!isTopClass())
        {
            Class queriedClass = this;
            do {
                queriedClass = queriedClass.getParent();
                if (queriedClass != null & equals(queriedClass))
                    throw new CircularInheritanceException(name, line, "Circular inheritance detected for class " + name + " declared on line " + line + ".");
            } while(!queriedClass.isTopClass());
        }
    }

    private void checkAttributes() throws UndefinedTypeException
    {
        for (Attribute a : attributes)
            if (!Types.isValid(a.getType()))
                throw new UndefinedTypeException(a.getType(), a.getLine(), "Type " + a.getType() + " associated to attribute " + a.getName() + " is unknown.");
    }

    private void checkUnits() throws UndefinedTypeException
    {
        for (Method m : methods)
            m.check();
        for (Constructor c : constructors)
            c.check();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Class)
        {
            Class otherClass = (Class) obj;
            return name.equals(otherClass.getName());
        }
        else
            return false;
    }

    public boolean isConsolidated()
    {
        return consolidated;
    }

    public void consolidate() throws UndefinedTypeException, IllegalOverrideException, UndefinedParentClassException
    {
        Class parentClass = getParent();

        if (!(parentClass == null) && !parentClass.isConsolidated())
            parentClass.consolidate();

        updateOffsets();
        consolidateAttributes();
        consolidateMethods();

        consolidated = true;
    }

    private void consolidateAttributes() throws UndefinedParentClassException
    {
        for (Attribute attribute : attributes)
        {
            if (attribute.isStatic())
                attribute.setOffset(classVariableOffset++);
            else
                attribute.setOffset(attributeOffset++);
        }

        if (!isTopClass())
        {
            Class parentClass = getParent();
            if (parentClass == null)
                throw new UndefinedParentClassException(parent, line, "Parent class for " + name + " has not been declared.");
            for (Attribute a : parentClass.attributes())
                attributes.insert(a.getName(), a);
        }
    }

    private void consolidateMethods() throws IllegalOverrideException, UndefinedParentClassException, UndefinedTypeException
    {
        if (!isTopClass())
        {
            Class parentClass = getParent();
            List<Method> inheritedMethods = new LinkedList<>();
            for (Method parentMethod : parentClass.methods())
            {
                boolean foundEquals = false;
                for (Method localMethod : methods.getAll(parentMethod.getName()))
                {
                    if (localMethod.equals(parentMethod))
                    {
                        if (localMethod.isStatic() != parentMethod.isStatic())
                            throw new IllegalOverrideException(localMethod.getName(), localMethod.getLine(), "Illegal override of method " + parentMethod + " in class " + this + ".\nCannot change from " +
                                    (parentMethod.isStatic() ? "static" : "dynamic") + " to " + (localMethod.isStatic() ? "static" : "dynamic") + ".");

                        String parentReturn = parentMethod.getReturnType();
                        String localReturn = localMethod.getReturnType();

                        if (!Types.conformsWith(localReturn, parentReturn) && !(localReturn.equals(Types.VOID) && (parentReturn.equals(Types.VOID))))
                            throw new IllegalOverrideException(localMethod.getName(), localMethod.getLine(), "Illegal override of method " + parentMethod + " from class " + parentClass + " in class " + this +
                                    " - cannot change return type form " + parentReturn + " to " + localReturn + ".");

                        foundEquals = true;

                        if(localMethod.isDynamic())
                            localMethod.setOffset(parentMethod.getOffset());
                        break;
                    }
                }
                if (!foundEquals)
                    inheritedMethods.add(parentMethod);
            }
            for (Method m : inheritedMethods)
                methods.insert(m.getName(), m);

            for (Method method : methods)
            {
                if(!method.isOffsetSet() && method.isDynamic())
                    method.setOffset(methodOffset++);
            }
        }
    }

    public void generateCode()
    {
        SymbolTable.setCurrentClass(this);
        SymbolTable.appendCodeLines(".DATA");
        generateVirtualTable();
        generateClassRecord();
        SymbolTable.appendCodeLines(".CODE");
        for (Constructor constructor : constructors)
            constructor.generateCode();
        for (Method method : methods)
        {
            if (!method.alreadyGenerated())
                method.generateCode();
        }
    }

    private void generateVirtualTable()
    {
        String dynamicTags = dynamicTags();
        SymbolTable.appendCodeLines(getVirtualTableTag() + ": DW " + (dynamicTags().isEmpty()? "0" : dynamicTags)  + " # Virtual table for class " + name);
    }

    private void generateClassRecord()
    {
        if (classVariableOffset > 0)
            SymbolTable.appendCodeLines(getClassRecordTag() + ": DW " + classVariableOffset + " DUP(0)");
    }

    private String dynamicTags()
    {
        List<Method> dynamicMethods = new LinkedList<>();
        for (Method method : methods)
        {
            if (method.isDynamic())
                dynamicMethods.add(method);
        }

        String[] dynamicTags = new String[dynamicMethods.size()];

        for (Method dynamicMethod : dynamicMethods)
            dynamicTags[dynamicMethod.getOffset()] = dynamicMethod.getTag();

        StringBuilder tagBuilder = new StringBuilder();

        for (int i = 0; i < dynamicTags.length; i ++)
        {
            tagBuilder.append(dynamicTags[i]);
            if (i != dynamicTags.length - 1)
                tagBuilder.append(", ");
        }

        return tagBuilder.toString();
    }

    public String getVirtualTableTag()
    {
        return "VT" + name;
    }

    public String getClassRecordTag()
    {
        return "CR" + name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

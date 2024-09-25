package SyntacticModule;

import Exceptions.LexicalException;
import Exceptions.SemanticExceptions.*;
import Exceptions.SyntacticException;
import LexicalModule.LexicalAnalyzer;
import LexicalModule.Token;
import LexicalModule.TokenIndex;
import SemanticModule.ASTNodes.Access.AccessNode;
import SemanticModule.ASTNodes.Access.CastedAccessNode;
import SemanticModule.ASTNodes.Access.CastingNode;
import SemanticModule.ASTNodes.Access.PrimaryNode.*;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BinaryExpressionNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.EqualityOperation.EqualToNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.EqualityOperation.NotEqualToNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.LogicalOperation.AndOperationNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.LogicalOperation.OrOperationNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.RelationalOperation.GreaterThanNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.RelationalOperation.GreaterOrEqualThanNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.RelationalOperation.LesserOrEqualThanNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.BooleanBinaryOperation.RelationalOperation.LesserThanNode;
import SemanticModule.ASTNodes.Expression.BinaryExpression.IntegerBinaryOperation.*;
import SemanticModule.ASTNodes.Expression.ExpressionNode;
import SemanticModule.ASTNodes.Expression.UnaryExpression.NotNode;
import SemanticModule.ASTNodes.Expression.UnaryExpression.UnaryExpressionNode;
import SemanticModule.ASTNodes.Expression.UnaryExpression.UnaryMinusNode;
import SemanticModule.ASTNodes.Expression.UnaryExpression.UnaryPlusNode;
import SemanticModule.ASTNodes.Operand.Literal.*;
import SemanticModule.ASTNodes.Operand.OperandNode;
import SemanticModule.ASTNodes.Statement.Assignmnent.AssignmentNode;
import SemanticModule.ASTNodes.Statement.Assignmnent.AutoDecrementNode;
import SemanticModule.ASTNodes.Statement.Assignmnent.AutoIncrementNode;
import SemanticModule.ASTNodes.Statement.Assignmnent.ClassicAssignmentNode;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.ASTNodes.Statement.CallNode;
import SemanticModule.ASTNodes.Statement.ForNode;
import SemanticModule.ASTNodes.Statement.IfNode;
import SemanticModule.ASTNodes.Statement.LocalVariableNode;
import SemanticModule.ASTNodes.Statement.ReturnNode;
import SemanticModule.ASTNodes.Statement.StatementNode;
import SemanticModule.ASTNodes.VariableType.*;
import SemanticModule.Declarations.Attribute;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.Declarations.Parameter;
import SemanticModule.SymbolTable;
import SemanticModule.Types;
import SemanticModule.Declarations.Units.Constructor;
import SemanticModule.Declarations.Units.Method;
import Utilities.TokenInspector;

import java.util.LinkedList;
import java.util.List;

public class SyntacticAnalyzer
{
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer)
    {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void start() throws LexicalException, SyntacticException, RepeatedAttributeNameException, IllegalMethodException, RepeatedParameterNameException, PreexistingClassException, IllegalConstructorException
    {
        advanceToken();
        inicial();
        match(Token.EOF);
    }

    private void inicial() throws LexicalException, SyntacticException, RepeatedAttributeNameException, IllegalMethodException, RepeatedParameterNameException, PreexistingClassException, IllegalConstructorException
    {
        listaClases();
    }

    private void listaClases() throws SyntacticException, LexicalException, RepeatedAttributeNameException, IllegalMethodException, RepeatedParameterNameException, PreexistingClassException, IllegalConstructorException
    {
        clase();
        demasClases();
    }

    private void clase() throws LexicalException, SyntacticException, RepeatedAttributeNameException, IllegalMethodException, RepeatedParameterNameException, PreexistingClassException, IllegalConstructorException
    {
        int line = currentToken.getLine();
        match(Token.KEYWORD_CLASS);
        String className = currentToken.getLexeme();
        match(Token.IDENTIFIER_CLASS);
        Class newClass = new Class(className, line);
        SymbolTable.setCurrentClass(newClass);

        genericidad();

        herencia();

        match(Token.PUNCTUATION_OPENING_BRACKET);
        listaMiembros();
        match(Token.PUNCTUATION_CLOSING_BRACKET);

        SymbolTable.addClass(SymbolTable.currentClass());
    }

    private void genericidad() throws LexicalException, SyntacticException
    {
        if (TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_LESSER))
        {
            match(Token.OPERATOR_LESSER);
            entreDiamante();
            match(Token.OPERATOR_GREATER);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_EXTENDS, Token.PUNCTUATION_OPENING_PARENTHESIS, Token.PUNCTUATION_CLOSING_PARENTHESIS, Token.PUNCTUATION_OPENING_BRACKET, Token.OPERATOR_GREATER, Token.PUNCTUATION_COMMA,
                    Token.IDENTIFIER_METHOD_VARIABLE))
        {
            // SIGUIENTES: extends ) { , > [Method or variable identifier]
        }
        else
            launchSyntacticException("extends ( { , >");
    }

    private void entreDiamante() throws LexicalException, SyntacticException
    {
        if (TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
        {
            match(Token.IDENTIFIER_CLASS);
            genericidad();
            restoGenericidad();
        }
        else
            launchSyntacticException("[Class identifier]");
    }

    private void restoGenericidad() throws LexicalException, SyntacticException
    {
        if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_COMMA))
        {
            match(Token.PUNCTUATION_COMMA);
            match(Token.IDENTIFIER_CLASS);
            genericidad();
            restoGenericidad();
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_GREATER))
        {
            // SIGUIENTES: >
        }
        else
            launchSyntacticException(", >");
    }

    private void demasClases() throws LexicalException, SyntacticException, RepeatedAttributeNameException, IllegalMethodException, RepeatedParameterNameException, PreexistingClassException, IllegalConstructorException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_CLASS))
            listaClases();
        // SIGUIENTES:
    }

    private void listaMiembros() throws SyntacticException, LexicalException, RepeatedAttributeNameException, IllegalMethodException, RepeatedParameterNameException, IllegalConstructorException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_PUBLIC, Token.KEYWORD_PRIVATE, Token.KEYWORD_BOOLEAN, Token.KEYWORD_STRING, Token.KEYWORD_INT,
                Token.KEYWORD_CHAR, Token.IDENTIFIER_CLASS, Token.KEYWORD_STATIC, Token.KEYWORD_DYNAMIC))
        {
            miembro();
            listaMiembros();
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_CLOSING_BRACKET))
        {
            // SIGUIENTES: }
        }
        else
            launchSyntacticException("}");
    }

    private void herencia() throws LexicalException, SyntacticException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_EXTENDS))
        {
            match(Token.KEYWORD_EXTENDS);
            Token parentToken = currentToken;
            match(Token.IDENTIFIER_CLASS);
            SymbolTable.currentClass().setParent(parentToken.getLexeme());
            genericidad();
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_BRACKET))
        {
            // SIGUIENTES: {
        }
        else
            launchSyntacticException("{");
    }

    private void miembro() throws SyntacticException, LexicalException, RepeatedAttributeNameException, IllegalMethodException, RepeatedParameterNameException, IllegalConstructorException
    {
        int line = currentToken.getLine();
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_PUBLIC, Token.KEYWORD_PRIVATE))
            atributoConVisibilidad(line);
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_BOOLEAN, Token.KEYWORD_CHAR, Token.KEYWORD_INT, Token.KEYWORD_STRING))
        {
            String type = tipoPrimitivo().getTypeToken().getLexeme();
            List<String> attributeNames = finAtributo();
            for (String name : attributeNames)
                SymbolTable.currentClass().addAttribute(new Attribute(name, type, line));
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
        {
            String classId = currentToken.getLexeme();
            match(Token.IDENTIFIER_CLASS);
            constructorOAtributo(classId, line);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_DYNAMIC))
        {
            match(Token.KEYWORD_DYNAMIC);
            finMetodoDinamico(line);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_STATIC))
        {
            match(Token.KEYWORD_STATIC);
            metodoOAtributoEstatico(line);
        }
        else
            launchSyntacticException("public private [Class identifier] static dynamic");
    }

    private boolean staticOVacio() throws LexicalException, SyntacticException
    {
        boolean isStatic = false;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_STATIC))
        {
            match(Token.KEYWORD_STATIC);
            isStatic = true;
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS, Token.KEYWORD_CHAR, Token.KEYWORD_STRING, Token.KEYWORD_BOOLEAN, Token.KEYWORD_INT))
            isStatic = false;
        else
            launchSyntacticException("int char boolean String [Class identifier]");
        return isStatic;
    }

    private void metodoOAtributoEstatico(int line) throws LexicalException, SyntacticException, RepeatedParameterNameException, RepeatedAttributeNameException, IllegalMethodException
    {
        if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_VOID))
        {
            match(Token.KEYWORD_VOID);
            finMetodo(line);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS, Token.KEYWORD_CHAR, Token.KEYWORD_STRING, Token.KEYWORD_BOOLEAN, Token.KEYWORD_INT))
        {
            String type = currentToken.getLexeme();
            tipo();
            String name = currentToken.getLexeme();
            match(Token.IDENTIFIER_METHOD_VARIABLE);
            finMetodoOAtributoEstatico(line, name, type);
        }
    }

    private void finMetodoOAtributoEstatico(int line, String name, String type) throws LexicalException, SyntacticException, RepeatedAttributeNameException, RepeatedParameterNameException, IllegalMethodException
    {
        if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_COMMA, Token.PUNCTUATION_SEMICOLON))
        {
            SymbolTable.currentClass().addAttribute(new Attribute(true, name, type, line, true));
            List<String> attributeNames = new LinkedList<>();
            demasAtributos(attributeNames);
            match(Token.PUNCTUATION_SEMICOLON);
            for (String attributeName : attributeNames)
                SymbolTable.currentClass().addAttribute(new Attribute(true, attributeName, type, line, true));
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
        {
            List<Parameter> parameterList = argsFormales();
            Method method = new Method(name,SymbolTable.currentClass(), line, true);
            method.setReturnType(type);
            for (Parameter p : parameterList)
            {
                p.setAssociatedUnit(method);
                method.addParameter(p);
            }
            SymbolTable.currentClass().addMethod(method);
            SymbolTable.setCurrentUnit(method);
            bloque();
        }
        else
            launchSyntacticException(", ; (");
    }

    private void finMetodoDinamico(int line) throws LexicalException, SyntacticException, IllegalMethodException, RepeatedParameterNameException
    {
        String returnType = tipoMetodo();
        String name = currentToken.getLexeme();
        match(Token.IDENTIFIER_METHOD_VARIABLE);
        List<Parameter> parameterList = argsFormales();
        Method method = new Method(name, line, returnType, SymbolTable.currentClass(), false);
        for (Parameter p : parameterList)
            method.addParameter(p);
        SymbolTable.currentClass().addMethod(method);
        SymbolTable.setCurrentUnit(method);
        method.setBlock(bloque());
    }

    private void atributoConVisibilidad(int line) throws LexicalException, SyntacticException, RepeatedAttributeNameException
    {
        boolean publicAttribute = visibilidad();
        boolean staticAttribute = staticOVacio();
        String type = tipo().getTypeToken().getLexeme();
        List<String> attributeNames = finAtributo();
        for (String name : attributeNames)
            SymbolTable.currentClass().addAttribute(new Attribute(staticAttribute, name, type, line, publicAttribute));
    }

    private List<String> finAtributo() throws LexicalException, SyntacticException {
        List<String> attributeNames = new LinkedList<>();
        listaDecAtrs(attributeNames);
        match(Token.PUNCTUATION_SEMICOLON);
        return attributeNames;
    }
    
    private void finMetodo(int line) throws LexicalException, SyntacticException, RepeatedParameterNameException, IllegalMethodException
    {
        String methodName = currentToken.getLexeme();
        match(Token.IDENTIFIER_METHOD_VARIABLE);
        Method method = new Method(methodName, line, Types.VOID, SymbolTable.currentClass(), true);
        for (Parameter p : argsFormales())
        {
            p.setAssociatedUnit(method);
            method.addParameter(p);
        }
        SymbolTable.currentClass().addMethod(method);
        SymbolTable.setCurrentUnit(method);
        bloque();
    }
    
    private void constructorOAtributo(String classId, int line) throws LexicalException, SyntacticException, RepeatedParameterNameException, IllegalConstructorException, RepeatedAttributeNameException
    {
        if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
        {
            Constructor constructor = new Constructor(classId, line);
            List<Parameter> argumentList = argsFormales();
            for(Parameter p : argumentList)
                constructor.addParameter(p);
            SymbolTable.currentClass().addConstructor(constructor);
            SymbolTable.setCurrentUnit(constructor);
            constructor.setBlock(bloque());
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_METHOD_VARIABLE, Token.OPERATOR_LESSER))
        {
            genericidad();
            List<String> attributeNames = finAtributo();
            for (String name : attributeNames)
            {
                SymbolTable.currentClass().addAttribute(new Attribute(name, classId, line));
            }
        }
        else
            launchSyntacticException("( [Method or variable identifier] <");
    }
    
    private boolean visibilidad() throws LexicalException, SyntacticException
    {
        boolean visibility = false;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_PUBLIC))
        {
            match(Token.KEYWORD_PUBLIC);
            visibility = true;
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_PRIVATE)) {
            match(Token.KEYWORD_PRIVATE);
        }
        else
            launchSyntacticException("public private");
        return visibility;
    }
    
    private VariableTypeNode tipo() throws LexicalException, SyntacticException
    {
        VariableTypeNode typeNode = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_BOOLEAN, Token.KEYWORD_CHAR, Token.KEYWORD_INT, Token.KEYWORD_STRING))
            typeNode = tipoPrimitivo();
        else if (TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
        {
            Token typeToken = currentToken;
            match(Token.IDENTIFIER_CLASS);
            typeNode = new ClassTypeNode(typeToken);
            genericidad();
        }
        else
            launchSyntacticException("boolean char int String [Class identifier]");
        return typeNode;
    }

    private VariableTypeNode tipoPrimitivo() throws LexicalException, SyntacticException
    {
        VariableTypeNode typeNode = null;
        Token typeToken = currentToken;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_BOOLEAN)) {
            match(Token.KEYWORD_BOOLEAN);
            typeNode = new BooleanTypeNode(typeToken);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_CHAR)) {
            match(Token.KEYWORD_CHAR);
            typeNode = new CharTypeNode(typeToken);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_INT)) {
            match(Token.KEYWORD_INT);
            typeNode = new IntTypeNode(typeToken);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_STRING)) {
            match(Token.KEYWORD_STRING);
            typeNode = new StringTypeNode(typeToken);
        }
        else
            launchSyntacticException("boolean char int String");
        return typeNode;
    }

    private void listaDecAtrs(List<String> namesList) throws LexicalException, SyntacticException
    {
        namesList.add(currentToken.getLexeme());
        match(Token.IDENTIFIER_METHOD_VARIABLE);
        demasAtributos(namesList);
    }

    private void demasAtributos(List<String> namesList) throws LexicalException, SyntacticException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_COMMA))
        {
            match(Token.PUNCTUATION_COMMA);
            listaDecAtrs(namesList);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_SEMICOLON))
        {
            // SIGUIENTES: ;
        }
        else
            launchSyntacticException(";");
    }


    private String tipoMetodo() throws LexicalException, SyntacticException
    {
        String type = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_BOOLEAN, Token.KEYWORD_CHAR,
                Token.KEYWORD_INT, Token.KEYWORD_STRING, Token.IDENTIFIER_CLASS))
            type = tipo().getTypeToken().getLexeme();
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_VOID)) {
            type = Types.VOID;
            match(Token.KEYWORD_VOID);
        }
        else
            launchSyntacticException("boolean char int String class void");
        return type;
    }

    private List<Parameter> argsFormales() throws LexicalException, SyntacticException
    {
        match(Token.PUNCTUATION_OPENING_PARENTHESIS);
        List<Parameter> parameterList = new LinkedList<>();
        listaArgsFormalesOVacio(parameterList);
        match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
        return parameterList;
    }

    private void listaArgsFormalesOVacio(List<Parameter> parameterList) throws LexicalException, SyntacticException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_BOOLEAN, Token.KEYWORD_CHAR, Token.KEYWORD_INT, Token.KEYWORD_STRING,
                Token.IDENTIFIER_CLASS, Token.KEYWORD_VOID))
            listaArgsFormales(parameterList);
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_CLOSING_PARENTHESIS))
        {
            // SIGUIENTES: )
        }
        else
            launchSyntacticException(")");
    }

    private void listaArgsFormales(List<Parameter> parameterList) throws LexicalException, SyntacticException
    {
        argFormal(parameterList);
        demasArgsFormales(parameterList);
    }

    private void demasArgsFormales(List<Parameter> parameterList) throws LexicalException, SyntacticException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_COMMA))
        {
            match(Token.PUNCTUATION_COMMA);
            listaArgsFormales(parameterList);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_CLOSING_PARENTHESIS))
        {
            // SIGUIENTES: )
        }
        else
            launchSyntacticException(")");
    }

    private void argFormal(List<Parameter> parameterList) throws LexicalException, SyntacticException
    {
        int line = currentToken.getLine();
        String type = tipo().getTypeToken().getLexeme();
        String name = currentToken.getLexeme();
        match(Token.IDENTIFIER_METHOD_VARIABLE);
        parameterList.add(new Parameter(type, name, line));
    }

    private BlockNode bloque() throws LexicalException, SyntacticException
    {
        match(Token.PUNCTUATION_OPENING_BRACKET);
        BlockNode block;
        if (SymbolTable.currentBlock() != null)
            block = new BlockNode(SymbolTable.currentBlock());
        else
            block = SymbolTable.currentUnit().getBlock();
        SymbolTable.pushBlock(block);
        for (StatementNode statement : listaSentencias())
            block.addStatement(statement);
        SymbolTable.popBlock();
        match(Token.PUNCTUATION_CLOSING_BRACKET);
        return block;
    }

    private List<StatementNode> listaSentencias() throws LexicalException, SyntacticException
    {
        List<StatementNode> statementList = new LinkedList<>();
        if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_SEMICOLON, Token.KEYWORD_THIS, Token.IDENTIFIER_METHOD_VARIABLE, Token.KEYWORD_NEW, Token.PUNCTUATION_OPENING_PARENTHESIS,
                Token.KEYWORD_BOOLEAN, Token.KEYWORD_CHAR, Token.KEYWORD_INT, Token.KEYWORD_STRING, Token.IDENTIFIER_CLASS, Token.KEYWORD_RETURN,
                Token.KEYWORD_IF, Token.KEYWORD_FOR, Token.PUNCTUATION_OPENING_BRACKET))
        {
            statementList.add(sentencia());
            for (StatementNode statement : listaSentencias())
                statementList.add(statement);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_CLOSING_BRACKET))
        {
            // SIGUIENTES: }
        }
        else
            launchSyntacticException("}");
        return statementList;
    }

    private StatementNode sentencia() throws LexicalException, SyntacticException
    {
        StatementNode statement = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_BOOLEAN, Token.KEYWORD_CHAR, Token.KEYWORD_STRING, Token.KEYWORD_INT))
        {
            VariableTypeNode type = tipoPrimitivo();
            Token idToken = currentToken;
            match(Token.IDENTIFIER_METHOD_VARIABLE);
            ExpressionNode expression = igualExpresionOVacio();
            statement = new LocalVariableNode(type, idToken, expression);
            match(Token.PUNCTUATION_SEMICOLON);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
        {
            Token classToken = currentToken;
            match(Token.IDENTIFIER_CLASS);
            statement = accesoOVarLocal(classToken);
            match(Token.PUNCTUATION_SEMICOLON);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_THIS))
        {
            AccessNode access = new AccessNode(accesoThis());
            for (ChainableLinkNode link :encadenado())
                access.chainAccess(link);
            AssignmentNode assignment = tipoDeAsignacionOVacio();
            if (assignment == null)
                statement = new CallNode(access);
            else
            {
                assignment.setLeftSide(access);
                statement = assignment;
            }
            match(Token.PUNCTUATION_SEMICOLON);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_NEW))
        {
            AccessNode access = new AccessNode(accesoConstructor());
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
            AssignmentNode assignment = tipoDeAsignacionOVacio();
            if (assignment == null)
                statement = new CallNode(access);
            else
            {
                assignment.setLeftSide(access);
                statement = assignment;
            }
            match(Token.PUNCTUATION_SEMICOLON);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_METHOD_VARIABLE))
        {
            Token idToken = currentToken;
            match(Token.IDENTIFIER_METHOD_VARIABLE);
            AccessNode access = new AccessNode(argsActualesOVacio(idToken));
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
            AssignmentNode assignment = tipoDeAsignacionOVacio();
            if (assignment == null)
                statement = new CallNode(access);
            else
            {
                assignment.setLeftSide(access);
                statement = assignment;
            }
            match(Token.PUNCTUATION_SEMICOLON);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
        {
            Token parenthesisToken = currentToken;
            match(Token.PUNCTUATION_OPENING_PARENTHESIS);
            AccessNode access = castingOExpresionParentizada(parenthesisToken);
            AssignmentNode assignmentNode = tipoDeAsignacionOVacio();
            if (assignmentNode != null)
            {
                assignmentNode.setLeftSide(access);
                statement = assignmentNode;
            }
            else
                statement = new CallNode(access);
            match(Token.PUNCTUATION_SEMICOLON);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_RETURN))
        {
            statement = returnn();
            match(Token.PUNCTUATION_SEMICOLON);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_IF))
            statement = iff();
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_FOR))
            statement = forr();
        else if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_BRACKET))
            statement = bloque();
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_SEMICOLON))
        {
            match(Token.PUNCTUATION_SEMICOLON);
            statement = new StatementNode();
        }
        else
            launchSyntacticException("this method [LocalVariable identifier] new ) boolean char int String [Class identifier] return if for ; or {");
        return statement;
    }

    private StatementNode accesoOVarLocal(Token classToken) throws LexicalException, SyntacticException
    {
        StatementNode statement = null;
        if (TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_METHOD_VARIABLE, Token.OPERATOR_LESSER))
        {
            genericidad();
            Token idToken = currentToken;
            match(Token.IDENTIFIER_METHOD_VARIABLE);
            ExpressionNode expression = igualExpresionOVacio();
            statement = new LocalVariableNode(new ClassTypeNode(classToken), idToken, expression);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD))
        {
            AccessNode access = new AccessNode();
            match(Token.PUNCTUATION_PERIOD);
            Token varOrMethodToken = currentToken;
            match(Token.IDENTIFIER_METHOD_VARIABLE);
            ClassLinkNode classLink = null;

            // ArgsActualesOVacio
            if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
            {
                MethodLinkNode method = new MethodLinkNode(varOrMethodToken);
                for (ExpressionNode argument : argsActuales())
                    method.addArgument(argument);
                classLink = new StaticMethodLinkNode(classToken, method);
            }
            else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD, Token.OPERATOR_ASSIGN, Token.OPERATOR_INCREMENT, Token.OPERATOR_DECREMENT, Token.PUNCTUATION_SEMICOLON,
                    Token.OPERATOR_OR, Token.OPERATOR_AND, Token.OPERATOR_EQUALS, Token.OPERATOR_NOT_EQUALS, Token.OPERATOR_LESSER, Token.OPERATOR_LESSER_OR_EQUAL,
                    Token.OPERATOR_GREATER, Token.OPERATOR_GREATER_OR_EQUAL, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_MULTIPLICATION, Token.OPERATOR_DIVISION,
                    Token.OPERATOR_MODULO, Token.PUNCTUATION_CLOSING_PARENTHESIS, Token.PUNCTUATION_COMMA))
            {
                // SIGUIENTES:  . =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,
                VariableLinkNode variable = new VariableLinkNode(varOrMethodToken);
                classLink = new StaticVariableLinkNode(classToken, variable);
            }
            else
                launchSyntacticException(". =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,");

            access.setFirst(classLink);
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);

            AssignmentNode assignment = tipoDeAsignacionOVacio();
            if (assignment != null)
            {
                assignment.setLeftSide(access);
                statement = assignment;
            }
            else
                statement = new CallNode(access);
        }
        else
            launchSyntacticException("[Method or variable identifier] .");
        return statement;
    }

    private AssignmentNode tipoDeAsignacionOVacio() throws LexicalException, SyntacticException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_ASSIGN, Token.OPERATOR_INCREMENT, Token.OPERATOR_DECREMENT))
            return tipoDeAsignacion();
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_SEMICOLON))
        {
            // SIGUIENTES: ;
        }
        else
            launchSyntacticException(";");
        return null;
    }

    private AssignmentNode asignacion() throws LexicalException, SyntacticException
    {
        AccessNode access = acceso();
        AssignmentNode assignment = tipoDeAsignacion();
        assignment.setLeftSide(access);
        return assignment;
    }

    private AssignmentNode tipoDeAsignacion() throws LexicalException, SyntacticException
    {
        AssignmentNode assignment = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_ASSIGN))
        {
            Token assignmentToken = currentToken;
            match(Token.OPERATOR_ASSIGN);
            assignment = new ClassicAssignmentNode(null, assignmentToken, expresion());
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_INCREMENT))
        {
            Token incrementToken = currentToken;
            match(Token.OPERATOR_INCREMENT);
            assignment = new AutoIncrementNode(null, incrementToken);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_DECREMENT))
        {
            Token decrementToken = currentToken;
            match(Token.OPERATOR_DECREMENT);
            assignment = new AutoDecrementNode(null, decrementToken);
        }
        else
            launchSyntacticException("= ++ --");
        return assignment;
    }

    private ExpressionNode igualExpresionOVacio() throws LexicalException, SyntacticException
    {
        ExpressionNode expressionNode = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_ASSIGN))
        {
            match(Token.OPERATOR_ASSIGN);
            expressionNode = expresion();
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_SEMICOLON))
        {
            // SIGUIENTES: ;
        }
        else
            launchSyntacticException(";");
        return expressionNode;
    }

    private ReturnNode returnn() throws LexicalException, SyntacticException
    {
        Token returnToken = currentToken;
        match(Token.KEYWORD_RETURN);
        ReturnNode returnNode = new ReturnNode(returnToken, expresionOVacio());
        return returnNode;
    }

    private ExpressionNode expresionOVacio() throws LexicalException, SyntacticException
    {
        ExpressionNode expression = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_NOT, Token.KEYWORD_NULL, Token.KEYWORD_TRUE, Token.KEYWORD_FALSE,
                Token.LITERAL_INT, Token.LITERAL_CHAR, Token.LITERAL_STRING, Token.IDENTIFIER_METHOD_VARIABLE, Token.KEYWORD_THIS, Token.KEYWORD_NEW, Token.PUNCTUATION_OPENING_PARENTHESIS))
            expression = expresion();
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_SEMICOLON))
        {
            // SIGUIENTES: ;
        }
        else
            launchSyntacticException(";");
        return expression;
    }

    private IfNode iff() throws LexicalException, SyntacticException
    {
        Token ifToken = currentToken;
        match(Token.KEYWORD_IF);
        IfNode ifNode = new IfNode(ifToken);
        match(Token.PUNCTUATION_OPENING_PARENTHESIS);
        ifNode.setCondition(expresion());
        match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
        ifNode.setThenStatement(sentencia());
        StatementNode elseStatement = elseOVacio();
        if (elseStatement != null)
            ifNode.setElseStatement(elseStatement);
        return ifNode;
    }

    private StatementNode elseOVacio() throws LexicalException, SyntacticException
    {
        StatementNode elseStatement = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_ELSE))
        {
            match(Token.KEYWORD_ELSE);
            elseStatement = sentencia();
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_THIS, Token.IDENTIFIER_METHOD_VARIABLE, Token.KEYWORD_NEW, Token.PUNCTUATION_OPENING_PARENTHESIS, Token.KEYWORD_BOOLEAN,
                Token.KEYWORD_CHAR, Token.KEYWORD_INT, Token.KEYWORD_STRING, Token.IDENTIFIER_CLASS, Token.KEYWORD_RETURN, Token.KEYWORD_IF,
                Token.KEYWORD_FOR, Token.PUNCTUATION_CLOSING_BRACKET))
        {
            // SIGUIENTES: this idMetVar new ( boolean char int String idClase return if for } else
        }
        else
            launchSyntacticException("this [Method or variable Identifier] new ( boolean char int String [Class identifier] return if for } else");
        return elseStatement;
    }


    // for compatible con for each
    private StatementNode forr() throws LexicalException, SyntacticException
    {
        StatementNode statement = null;
        Token forToken = currentToken;
        match(Token.KEYWORD_FOR);
        match(Token.PUNCTUATION_OPENING_PARENTHESIS);
        VariableTypeNode type = tipo();
        Token variableIdToken = currentToken;
        match(Token.IDENTIFIER_METHOD_VARIABLE);
        LocalVariableNode localVariable = new LocalVariableNode(type, variableIdToken);
        if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_COLON))
            statement = new StatementNode();
        else
            statement = restoFor(localVariable, forToken);
        return statement;
    }

    private ForNode restoFor(LocalVariableNode localVariable, Token forToken) throws LexicalException, SyntacticException {
        ForNode forNode = null;
        // for
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_ASSIGN, Token.PUNCTUATION_SEMICOLON))
        {
            forNode = new ForNode(forToken);
            forNode.setParentBlock(SymbolTable.currentBlock());
            localVariable.setExpression(igualExpresionOVacio());
            forNode.setLocalVariable(localVariable);
            match(Token.PUNCTUATION_SEMICOLON);
            forNode.setBooleanExpression(expresion());
            match(Token.PUNCTUATION_SEMICOLON);
            forNode.setAssignment(asignacion());
            match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
            forNode.addStatement(sentencia());
        }
        // for each
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_COLON))
        {
            match(Token.PUNCTUATION_COLON);
            acceso();
            match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
            sentencia();
        }
        else
            launchSyntacticException("= ; :");
        return forNode;
    }

    private ExpressionNode expresion() throws LexicalException, SyntacticException
    {
        UnaryExpressionNode unaryExpression = expresionUnaria();
        return restoDeExpresion(unaryExpression);
    }

    private ExpressionNode restoDeExpresion(UnaryExpressionNode unaryExpression) throws LexicalException, SyntacticException
    {
        ExpressionNode expression = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_OR, Token.OPERATOR_AND, Token.OPERATOR_EQUALS, Token.OPERATOR_NOT_EQUALS, Token.OPERATOR_LESSER,
                Token.OPERATOR_GREATER, Token.OPERATOR_LESSER_OR_EQUAL, Token.OPERATOR_GREATER_OR_EQUAL, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS,
                Token.OPERATOR_MULTIPLICATION, Token.OPERATOR_DIVISION, Token.OPERATOR_MODULO))
        {
            BinaryExpressionNode binaryExpression = operadorBinario();
            binaryExpression.setLeftSide(unaryExpression);
//            expresionUnaria();
//            restoDeExpresion();
            binaryExpression.setRightSide(restoDeExpresion(expresionUnaria()));
            expression = binaryExpression;
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_SEMICOLON, Token.PUNCTUATION_CLOSING_PARENTHESIS, Token.PUNCTUATION_COMMA))
        {
            // SIGUIENTES:  ; ) ,
            expression = unaryExpression;
        }
        else
            launchSyntacticException("; ) ,");
        return expression;
    }

    private BinaryExpressionNode operadorBinario() throws LexicalException, SyntacticException
    {
        BinaryExpressionNode expression = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_OR)) {
            expression = new OrOperationNode(null, currentToken, null);
            match(Token.OPERATOR_OR);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_AND)) {
            expression = new AndOperationNode(null, currentToken, null);
            match(Token.OPERATOR_AND);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_EQUALS)) {
            expression = new EqualToNode(null, currentToken, null);
            match(Token.OPERATOR_EQUALS);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_NOT_EQUALS)) {
            expression = new NotEqualToNode(null, currentToken, null);
            match(Token.OPERATOR_NOT_EQUALS);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_LESSER)) {
            expression = new LesserThanNode(null, currentToken, null);
            match(Token.OPERATOR_LESSER);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_GREATER)) {
            expression = new GreaterThanNode(null, currentToken, null);
            match(Token.OPERATOR_GREATER);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_LESSER_OR_EQUAL)) {
            expression = new LesserOrEqualThanNode(null, currentToken, null);
            match(Token.OPERATOR_LESSER_OR_EQUAL);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_GREATER_OR_EQUAL)) {
            expression = new GreaterOrEqualThanNode(null, currentToken, null);
            match(Token.OPERATOR_GREATER_OR_EQUAL);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_PLUS)) {
            expression = new SumOperationNode(null, currentToken, null);
            match(Token.OPERATOR_PLUS);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_MINUS)) {
            expression = new SubtractionOperationNode(null, currentToken, null);
            match(Token.OPERATOR_MINUS);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_MULTIPLICATION)) {
            expression = new ProductOperationNode(null, currentToken, null);
            match(Token.OPERATOR_MULTIPLICATION);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_DIVISION)) {
            expression = new DivisionOperationNode(null, currentToken, null);
            match(Token.OPERATOR_DIVISION);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_MODULO))
        {
            expression = new ModuloOperationNode(null, currentToken, null);
            match(Token.OPERATOR_MODULO);
        }
        else
            launchSyntacticException("|| && == != < <= > >= + - * / %");
        return expression;
    }

    private UnaryExpressionNode expresionUnaria() throws LexicalException, SyntacticException
    {
        UnaryExpressionNode unaryExpression = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_NOT))
        {
            unaryExpression = operadorUnario();
            unaryExpression.setOperand(operando());
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_NULL, Token.KEYWORD_TRUE, Token.KEYWORD_FALSE, Token.LITERAL_INT, Token.LITERAL_CHAR,
                Token.LITERAL_STRING, Token.KEYWORD_THIS, Token.IDENTIFIER_METHOD_VARIABLE, Token.IDENTIFIER_CLASS, Token.KEYWORD_NEW, Token.PUNCTUATION_OPENING_PARENTHESIS))
            unaryExpression = new UnaryExpressionNode(operando());
        else
            launchSyntacticException("+ _ ! null true false [Integer literal] [Char literal] [String literal] this [Method or variable identifier] [Class identifier] new (");
        return unaryExpression;
    }

    private UnaryExpressionNode operadorUnario() throws LexicalException, SyntacticException
    {
        UnaryExpressionNode unaryExpression = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_PLUS))
        {
            unaryExpression = new UnaryPlusNode(null, currentToken);
            match(Token.OPERATOR_PLUS);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_MINUS)) {
            unaryExpression = new UnaryMinusNode(null, currentToken);
            match(Token.OPERATOR_MINUS);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_NOT)) {
            unaryExpression = new NotNode(null, currentToken);
            match(Token.OPERATOR_NOT);
        }
        else
            launchSyntacticException("+ - !");
        return unaryExpression;
    }

    private OperandNode operando() throws SyntacticException, LexicalException
    {
        OperandNode operand = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_NULL, Token.KEYWORD_TRUE, Token.KEYWORD_FALSE, Token.LITERAL_INT, Token.LITERAL_CHAR,
                Token.LITERAL_STRING))
            operand = literal();
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_THIS, Token.IDENTIFIER_METHOD_VARIABLE, Token.KEYWORD_NEW, Token.PUNCTUATION_OPENING_PARENTHESIS, Token.IDENTIFIER_CLASS))
            operand = acceso();
        else
            launchSyntacticException("null true false [int literal] [char literal] [String literal] this [Method or variable identifier] new (");
        return operand;
    }

    private LiteralOperandNode literal() throws LexicalException, SyntacticException
    {
        LiteralOperandNode literal = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_NULL)) {
            literal = new NullLiteralNode(currentToken);
            match(Token.KEYWORD_NULL);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_TRUE)) {
            literal = new TrueLiteralNode(currentToken);
            match(Token.KEYWORD_TRUE);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_FALSE)) {
            literal = new FalseLiteralNode(currentToken);
            match(Token.KEYWORD_FALSE);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.LITERAL_INT)) {
            literal = new IntLiteralNode(currentToken);
            match(Token.LITERAL_INT);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.LITERAL_CHAR)) {
            literal = new CharLiteralNode(currentToken);
            match(Token.LITERAL_CHAR);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.LITERAL_STRING)) {
            literal = new StringLiteralNode(currentToken);
            match(Token.LITERAL_STRING);
        }
        else
            launchSyntacticException("null true false [int literal] [char literal] [String literal]");
        return literal;
    }

    private AccessNode acceso() throws LexicalException, SyntacticException
    {
        AccessNode access = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
        {
            Token classToken = currentToken;
            match(Token.IDENTIFIER_CLASS);
            access = encadenadoForzoso(classToken);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_THIS, Token.IDENTIFIER_METHOD_VARIABLE, Token.KEYWORD_NEW))
        {
            access = new AccessNode(primario());
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
        {
            Token parenthesisToken = currentToken;
            match(Token.PUNCTUATION_OPENING_PARENTHESIS);
            access = castingOExpresionParentizada(parenthesisToken);
        }
        else
            launchSyntacticException("this [Method or variable identifier] [Class identifier] new (");
        return access;
    }

    private AccessNode castingOExpresionParentizada(Token parenthesisToken) throws LexicalException, SyntacticException
    {
        AccessNode access = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_NOT, Token.KEYWORD_NULL, Token.KEYWORD_TRUE, Token.KEYWORD_FALSE,
                Token.LITERAL_INT, Token.LITERAL_CHAR, Token.LITERAL_STRING, Token.PUNCTUATION_OPENING_PARENTHESIS, Token.KEYWORD_THIS, Token.KEYWORD_NEW,Token.IDENTIFIER_METHOD_VARIABLE))
        {
            ExpressionInParenthesisLinkNode expInParenthesis = new ExpressionInParenthesisLinkNode(expresion());
            access = new AccessNode(expInParenthesis);
            expInParenthesis.setToken(new Token(Token.PUNCTUATION_SEMICOLON, ";", currentToken.getLine()));
            match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
        {
            Token idToken = currentToken;
            match(Token.IDENTIFIER_CLASS);
            access = genericidadOEncadenado(idToken);
        }
        else
            launchSyntacticException("+ - ! null true false [int literal] [char literal] [String literal] ( this new [Method or variable identifier] [Class identifier]");
        return access;
    }

    private AccessNode genericidadOEncadenado(Token classIdToken) throws LexicalException, SyntacticException
    {
        AccessNode access = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_LESSER, Token.PUNCTUATION_CLOSING_PARENTHESIS))
        {
            CastingNode casting = new CastingNode(classIdToken);
            genericidad();      // Se ignora la genericidad
            match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
            CastedAccessNode castedAccess = new CastedAccessNode();
            castedAccess.setCasting(casting);
            finCasting(castedAccess);
            access = castedAccess;
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD))
        {
            ExpressionInParenthesisLinkNode expInParenthesis = new ExpressionInParenthesisLinkNode();
            expInParenthesis.setToken(new Token(Token.PUNCTUATION_SEMICOLON, ";", currentToken.getLine()));
            ClassLinkNode classLink = null;
            match(Token.PUNCTUATION_PERIOD);
            Token varOrMethodToken = currentToken;
            match(Token.IDENTIFIER_METHOD_VARIABLE);

            // ArgsActualesOVacio
            if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
            {
                MethodLinkNode method = new MethodLinkNode(varOrMethodToken);
                for (ExpressionNode argument : argsActuales())
                    method.addArgument(argument);
                classLink = new StaticMethodLinkNode(classIdToken, method);
            }
            else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD, Token.OPERATOR_ASSIGN, Token.OPERATOR_INCREMENT, Token.OPERATOR_DECREMENT, Token.PUNCTUATION_SEMICOLON,
                    Token.OPERATOR_OR, Token.OPERATOR_AND, Token.OPERATOR_EQUALS, Token.OPERATOR_NOT_EQUALS, Token.OPERATOR_LESSER, Token.OPERATOR_LESSER_OR_EQUAL,
                    Token.OPERATOR_GREATER, Token.OPERATOR_GREATER_OR_EQUAL, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_MULTIPLICATION, Token.OPERATOR_DIVISION,
                    Token.OPERATOR_MODULO, Token.PUNCTUATION_CLOSING_PARENTHESIS, Token.PUNCTUATION_COMMA))
            {
                // SIGUIENTES:  . =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,
                VariableLinkNode variable = new VariableLinkNode(varOrMethodToken);
                classLink = new StaticVariableLinkNode(classIdToken, variable);
            }
            else
                launchSyntacticException(". =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,");

            AccessNode innerAccess = new AccessNode(classLink);
            for (ChainableLinkNode link : encadenado())
                innerAccess.chainAccess(link);

            UnaryExpressionNode unaryExpression = new UnaryExpressionNode(innerAccess);
            expInParenthesis.setExpression(restoDeExpresion(unaryExpression));
            match(Token.PUNCTUATION_CLOSING_PARENTHESIS);

            access = new AccessNode(expInParenthesis);

            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
        }
        return access;
    }

    private void finCasting(CastedAccessNode access) throws LexicalException, SyntacticException
    {
        if (TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_THIS, Token.KEYWORD_NEW, Token.IDENTIFIER_METHOD_VARIABLE))
        {
            access.setFirst(primario());
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
        {
            Token classToken = currentToken;
            match(Token.IDENTIFIER_CLASS);

            match(Token.PUNCTUATION_PERIOD);

            Token varOrMethodToken = currentToken;
            match(Token.IDENTIFIER_METHOD_VARIABLE);
            ClassLinkNode classLink = null;

            // ArgsActualesOVacio
            if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
            {
                MethodLinkNode method = new MethodLinkNode(varOrMethodToken);
                for (ExpressionNode argument : argsActuales())
                    method.addArgument(argument);
                classLink = new StaticMethodLinkNode(classToken, method);
            }
            else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD, Token.OPERATOR_ASSIGN, Token.OPERATOR_INCREMENT, Token.OPERATOR_DECREMENT, Token.PUNCTUATION_SEMICOLON,
                    Token.OPERATOR_OR, Token.OPERATOR_AND, Token.OPERATOR_EQUALS, Token.OPERATOR_NOT_EQUALS, Token.OPERATOR_LESSER, Token.OPERATOR_LESSER_OR_EQUAL,
                    Token.OPERATOR_GREATER, Token.OPERATOR_GREATER_OR_EQUAL, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_MULTIPLICATION, Token.OPERATOR_DIVISION,
                    Token.OPERATOR_MODULO, Token.PUNCTUATION_CLOSING_PARENTHESIS, Token.PUNCTUATION_COMMA))
            {
                // SIGUIENTES:  . =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,
                VariableLinkNode variable = new VariableLinkNode(varOrMethodToken);
                classLink = new StaticVariableLinkNode(classToken, variable);
            }
            else
                launchSyntacticException(". =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,");

            access.setFirst(classLink);
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
        {
            Token parenthesisToken = currentToken;
            match(Token.PUNCTUATION_OPENING_PARENTHESIS);
            ExpressionInParenthesisLinkNode expInParenthesis = new ExpressionInParenthesisLinkNode(expresion());
            expInParenthesis.setToken(new Token(Token.PUNCTUATION_SEMICOLON, ";", currentToken.getLine()));
            access.setFirst(expInParenthesis);
            match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
        }
        else
            launchSyntacticException("this new [Method or variable identifier] )");
    }

    private LinkNode primario() throws LexicalException, SyntacticException
    {
        LinkNode primary = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_THIS))
            primary = accesoThis();
        else if(TokenInspector.isTokenAmong(currentToken, Token.KEYWORD_NEW))
            primary = accesoConstructor();
        else if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_METHOD_VARIABLE))
        {
            Token idToken = currentToken;
            match(Token.IDENTIFIER_METHOD_VARIABLE);
            primary = argsActualesOVacio(idToken);
        }
        else
            launchSyntacticException("this new [Method or variable identifier]");
        return primary;
    }

    private ChainableLinkNode argsActualesOVacio(Token idToken) throws LexicalException, SyntacticException
    {
        ChainableLinkNode link = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
        {
            MethodLinkNode method = new MethodLinkNode(idToken);
            for (ExpressionNode argument : argsActuales())
                method.addArgument(argument);
            link = method;
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD, Token.OPERATOR_ASSIGN, Token.OPERATOR_INCREMENT, Token.OPERATOR_DECREMENT, Token.PUNCTUATION_SEMICOLON,
                Token.OPERATOR_OR, Token.OPERATOR_AND, Token.OPERATOR_EQUALS, Token.OPERATOR_NOT_EQUALS, Token.OPERATOR_LESSER, Token.OPERATOR_LESSER_OR_EQUAL,
                Token.OPERATOR_GREATER, Token.OPERATOR_GREATER_OR_EQUAL, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_MULTIPLICATION, Token.OPERATOR_DIVISION,
                Token.OPERATOR_MODULO, Token.PUNCTUATION_CLOSING_PARENTHESIS, Token.PUNCTUATION_COMMA))
        {
            // SIGUIENTES:  . =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,
            link = new VariableLinkNode(idToken);
        }
        else
            launchSyntacticException(". =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,");
        return link;
    }

    private ThisLinkNode accesoThis() throws LexicalException, SyntacticException
    {
        ThisLinkNode thisAccess = new ThisLinkNode(currentToken);
        match(Token.KEYWORD_THIS);
        return thisAccess;
    }

    private ConstructorLinkNode accesoConstructor() throws LexicalException, SyntacticException
    {
        match(Token.KEYWORD_NEW);
        ConstructorLinkNode constructorNode = new ConstructorLinkNode(currentToken);
        match(Token.IDENTIFIER_CLASS);
        genericidadODiamante();
        for (ExpressionNode expression : argsActuales())
            constructorNode.addArgument(expression);
        return constructorNode;
    }

    private void genericidadODiamante() throws LexicalException, SyntacticException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_LESSER))
        {
            match(Token.OPERATOR_LESSER);
            entreDiamanteOVacio();
            match(Token.OPERATOR_GREATER);
        }
        else if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_OPENING_PARENTHESIS))
        {
//            SIGUIENTES: (
        }
        else
            launchSyntacticException("(");
    }

    private void entreDiamanteOVacio() throws LexicalException, SyntacticException
    {
        if(TokenInspector.isTokenAmong(currentToken, Token.IDENTIFIER_CLASS))
            entreDiamante();
        else if (TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_GREATER))
        {
//            SIGUIENTES: >
        }
        else
            launchSyntacticException(">");
    }

    private List<ExpressionNode> argsActuales() throws LexicalException, SyntacticException
    {
        match(Token.PUNCTUATION_OPENING_PARENTHESIS);
        List<ExpressionNode> expressionList = listaExpsOVacio();
        match(Token.PUNCTUATION_CLOSING_PARENTHESIS);
        return expressionList;
    }

    private List<ExpressionNode> listaExpsOVacio() throws LexicalException, SyntacticException
    {
        List<ExpressionNode> expressionList = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_NOT, Token.KEYWORD_NULL, Token.KEYWORD_TRUE, Token.KEYWORD_FALSE,
                Token.LITERAL_INT, Token.LITERAL_CHAR, Token.LITERAL_STRING, Token.IDENTIFIER_METHOD_VARIABLE, Token.KEYWORD_NEW, Token.KEYWORD_THIS, Token.PUNCTUATION_OPENING_PARENTHESIS, Token.IDENTIFIER_CLASS)) {
            expressionList = listaExps();
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_CLOSING_PARENTHESIS))
        {
            // SIGUIENTES: )
            expressionList = new LinkedList<>();
        }
        else
            launchSyntacticException(")");
        return expressionList;
    }

    private List<ExpressionNode> listaExps() throws LexicalException, SyntacticException
    {
        List<ExpressionNode> expressionList = new LinkedList<>();
        expressionList.add(expresion());
        for (ExpressionNode expression : expresionesAdicionalesOVacio())
            expressionList.add(expression);
        return expressionList;
    }

    private List<ExpressionNode> expresionesAdicionalesOVacio() throws LexicalException, SyntacticException
    {
        List<ExpressionNode> expressionList = null;
        if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_COMMA))
        {
            match(Token.PUNCTUATION_COMMA);
            expressionList = listaExps();
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_CLOSING_PARENTHESIS))
        {
            // SIGUIENTES: )
            expressionList = new LinkedList<>();
        }
        else
            launchSyntacticException(")");
        return expressionList;
    }

    private List<ChainableLinkNode> encadenado() throws LexicalException, SyntacticException
    {
        List<ChainableLinkNode> links = new LinkedList<>();
        if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD))
        {
            links.add(varOMetodoEncadenado());
            for (ChainableLinkNode link : encadenado())
                links.add(link);
        }
        else if (TokenInspector.isTokenAmong(currentToken, Token.OPERATOR_ASSIGN, Token.OPERATOR_INCREMENT, Token.OPERATOR_DECREMENT, Token.PUNCTUATION_SEMICOLON, Token.OPERATOR_OR, Token.OPERATOR_AND,
                Token.OPERATOR_EQUALS, Token.OPERATOR_NOT_EQUALS, Token.OPERATOR_LESSER, Token.OPERATOR_LESSER_OR_EQUAL, Token.OPERATOR_GREATER, Token.OPERATOR_GREATER_OR_EQUAL,
                Token.OPERATOR_PLUS, Token.OPERATOR_MINUS, Token.OPERATOR_MULTIPLICATION, Token.OPERATOR_DIVISION, Token.OPERATOR_MODULO, Token.PUNCTUATION_CLOSING_PARENTHESIS, Token.PUNCTUATION_COMMA))
        {
            // SIGUIENTES: =  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,
        }
        else
            launchSyntacticException("=  ++ --  ;  ||  &&  ==  !=  <  >  <=  >=  +  -  *  /  %  )  ,");
        return links;
    }

    private AccessNode encadenadoForzoso(Token classToken) throws LexicalException, SyntacticException
    {
        AccessNode access = new AccessNode();
        if(TokenInspector.isTokenAmong(currentToken, Token.PUNCTUATION_PERIOD))
        {
            ClassLinkNode classLink;
            ChainableLinkNode methodOrVariablelink = varOMetodoEncadenado();
            if (methodOrVariablelink instanceof MethodLinkNode)
                classLink = new StaticMethodLinkNode(classToken, (MethodLinkNode) methodOrVariablelink);
            else
                classLink = new StaticVariableLinkNode(classToken, (VariableLinkNode) methodOrVariablelink);

            access.setFirst(classLink);
            for (ChainableLinkNode link : encadenado())
                access.chainAccess(link);
        }
        else
            launchSyntacticException(".");
        return access;
    }

    private ChainableLinkNode varOMetodoEncadenado() throws LexicalException, SyntacticException
    {
        match(Token.PUNCTUATION_PERIOD);
        Token idToken = currentToken;
        match(Token.IDENTIFIER_METHOD_VARIABLE);
        return argsActualesOVacio(idToken);
    }

    private void advanceToken() throws LexicalException
    {
        currentToken = lexicalAnalyzer.nextToken();
    }

    private void match(int tokenID) throws LexicalException, SyntacticException
    {
        if (currentToken.getTokenID() == tokenID)
            advanceToken();
        else
            launchSyntacticException(TokenIndex.getInstance().getTokenString(tokenID));
    }


    private void launchSyntacticException(String expected) throws SyntacticException
    {
        String lexeme = currentToken.getLexeme();
        int line = currentToken.getLine();
        throw new SyntacticException(expected, lexeme, line);
    }
}

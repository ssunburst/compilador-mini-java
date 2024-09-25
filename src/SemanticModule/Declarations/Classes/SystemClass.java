package SemanticModule.Declarations.Classes;

import Exceptions.SemanticExceptions.IllegalMethodException;
import Exceptions.SemanticExceptions.RepeatedParameterNameException;
import SemanticModule.ASTNodes.Statement.Block.BlockNode;
import SemanticModule.Declarations.Parameter;
import SemanticModule.SymbolTable;
import SemanticModule.Types;
import SemanticModule.Declarations.Units.Method;

public class SystemClass extends Class
{
    private static SystemClass instance;

    private SystemClass()
    {
        super();
        this.name = "System";
        this.consolidated = true;

        try {
            Method read = new Method("read", Types.INT, this, true);
            read.setSourceClass(this);
            read.setBlock(new ReadBlock());

            Method printB = new Method("printB", this, true);
            printB.setSourceClass(this);
            printB.addParameter(new Parameter(printB, Types.BOOLEAN, "b"));
            printB.setBlock(new PrintBBlock());

            Method printC = new Method("printC", this, true);
            printC.setSourceClass(this);
            printC.addParameter(new Parameter(printC, Types.CHAR, "c"));
            printC.setBlock(new PrintCBlock());

            Method printI = new Method("printI", this, true);
            printI.setSourceClass(this);
            printI.addParameter(new Parameter(printI, Types.INT, "i"));
            printI.setBlock(new PrintIBlock());

            Method printS = new Method("printS", this, true);
            printS.setSourceClass(this);
            printS.addParameter(new Parameter(printS, Types.STRING, "s"));
            printS.setBlock(new PrintSBlock());

            Method println = new Method("println", this, true);
            println.setSourceClass(this);
            println.setBlock(new PrintlnBlock());

            Method printBln = new Method("printBln", this, true);
            printBln.setSourceClass(this);
            printBln.addParameter(new Parameter(printBln, Types.BOOLEAN, "b"));
            printBln.setBlock(new PrintBlnBlock());

            Method printCln = new Method("printCln", this, true);
            printCln.setSourceClass(this);
            printCln.addParameter(new Parameter(printCln, Types.CHAR, "c"));
            printCln.setBlock(new PrintClnBlock());

            Method printIln = new Method("printIln", this, true);
            printIln.setSourceClass(this);
            printIln.addParameter(new Parameter(printIln, Types.INT, "i"));
            printIln.setBlock(new PrintIlnBlock());

            Method printSln = new Method("printSln", this, true);
            printSln.setSourceClass(this);
            printSln.addParameter(new Parameter(printSln, Types.STRING, "s"));
            printSln.setBlock(new PrintSlnBlock());

            this.addMethod(read);
            this.addMethod(printB);
            this.addMethod(printI);
            this.addMethod(printC);
            this.addMethod(printS);
            this.addMethod(println);
            this.addMethod(printBln);
            this.addMethod(printCln);
            this.addMethod(printIln);
            this.addMethod(printSln);

        } catch (RepeatedParameterNameException | IllegalMethodException e) {}
    }

    public static SystemClass getInstance()
    {
        if (instance == null)
            instance = new SystemClass();
        return instance;
    }

    private class ReadBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("READ");
        }
    }

    private class PrintBBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "BPRINT");
        }
    }

    private class PrintCBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "CPRINT");
        }
    }

    private class PrintIBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "IPRINT");
        }
    }

    private class PrintSBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "SPRINT");
        }
    }

    private class PrintlnBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("PRNLN");
        }
    }

    private class PrintBlnBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "BPRINT", "PRNLN");
        }
    }

    private class PrintClnBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "CPRINT", "PRNLN");
        }
    }

    private class PrintIlnBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "IPRINT", "PRNLN");
        }
    }

    private class PrintSlnBlock extends BlockNode
    {
        @Override
        public void specificCode()
        {
            SymbolTable.appendCodeLines("LOAD 3", "SPRINT", "PRNLN");
        }
    }
}

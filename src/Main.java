import Exceptions.LexicalException;
import Exceptions.SemanticExceptions.*;
import Exceptions.SyntacticException;
import LexicalModule.LexicalAnalyzer;
import LexicalModule.SourceFileManager;
import SemanticModule.Declarations.Attribute;
import SemanticModule.Declarations.Classes.Class;
import SemanticModule.SymbolTable;
import SemanticModule.Declarations.Units.Constructor;
import SemanticModule.Declarations.Units.Method;
import SyntacticModule.SyntacticAnalyzer;
import Utilities.PositionSignaler;
import jdk.nashorn.internal.ir.Symbol;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            String inputFile = args[0];
            String outputFile = args.length > 1? args[1] : "salida.txt";

            SourceFileManager sfm = new SourceFileManager(inputFile);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sfm);
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexicalAnalyzer);

            SymbolTable.addPredefinedClasses();

            syntacticAnalyzer.start();

            SymbolTable.checkDefinition();
            SymbolTable.consolidate();
            SymbolTable.checkMainMethod();
            SymbolTable.checkStatements();

            String code = SymbolTable.generateCode();

            SymbolTable.flush();

            System.out.println("Compilación exitosa.");
            System.out.println("\n[SinErrores]");

            FileWriter writer = new FileWriter(outputFile);
            writer.write(code);
            writer.close();

            sfm.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Exiting...");;
        }
        catch(IOException e)
        {
            System.out.println("An error occurred while reading from the input file. Exiting...");
        }
        catch(LexicalException e)
        {
            System.out.println(e.getMessage());
            System.out.println();
            String highlightedError = PositionSignaler.signalPosition(e.getExtract(), e.getIndex());
            System.out.println(highlightedError);
            System.out.println();
            System.out.println(e);
        }
        catch(SyntacticException e)
        {
            System.out.println(e.getMessage());
            System.out.println();
            System.out.println(e);
            SymbolTable.flush();
        }
        catch(SemanticException e)
        {
            System.out.println(e.getMessage());
            System.out.println();
            System.out.println(e);
            SymbolTable.flush();
        }

        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("No file location entered. Exiting...");
        }
    }
}

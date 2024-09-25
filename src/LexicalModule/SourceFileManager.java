package LexicalModule;

import Utilities.CharacterInspector;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SourceFileManager
{
    private FileReader fileReader;
    private boolean reachedEoF;
    private int currentLineNumber;
    private int positionToRead;
    private String previousLine;
    private StringBuilder lineProgress;

    public SourceFileManager(String filename) throws FileNotFoundException
    {
        fileReader = new FileReader(filename);
        reachedEoF = false;
        currentLineNumber = 1;
        positionToRead = 0;
        lineProgress = new StringBuilder();
    }

    public int getCurrentLineNumber()
    {
        if(positionToRead == 0 && currentLineNumber > 1)
            return currentLineNumber - 1;
        else
            return currentLineNumber;
    }

    public int nextSymbol()
    {
        int symbol;
        if(reachedEoF)
            symbol = -1;
        else
        {
            try
            {
                symbol = fileReader.read();
                if(CharacterInspector.isCarriageReturn(symbol))
                    return nextSymbol();
                if (CharacterInspector.isNewLine(symbol))
                {
                    currentLineNumber++;
                    positionToRead = 0;
                    previousLine = lineProgress.toString();
                    lineProgress = new StringBuilder();
                }
                else if(CharacterInspector.isEndOfFile(symbol))
                {
                    reachedEoF = true;
                    positionToRead++;
                }
                else
                {
                    lineProgress.append((char)symbol);
                    positionToRead++;
                }
            }
            catch (IOException e)
            {
                symbol = -1;
            }
        }
        return symbol;
    }

    public int getLatestPositionRead()
    {
        if(positionToRead == 0)
        {
            if (currentLineNumber == 1)
                return -1;
            else
                return previousLine.length();
        }
        else
            return positionToRead - 1;
    }

    public String getPreviousLine()
    {
        if(previousLine == null)
            return "";
        else
            return previousLine;
    }

    public String getLineProgress()
    {
        return lineProgress.toString();
    }

    public String getLineRemainder()
    {
        StringBuilder remainder = new StringBuilder();
        try
        {
            int symbol = fileReader.read();
            while(!CharacterInspector.isNewLine(symbol) && !CharacterInspector.isCarriageReturn(symbol) && !CharacterInspector.isEndOfFile(symbol))
            {
                remainder.append((char)symbol);
                symbol = fileReader.read();
            }
        }
        catch(IOException e)
        {
            remainder.append("...");
        }
        return remainder.toString();
    }


    public void close() throws IOException
    {
        fileReader.close();
    }
}

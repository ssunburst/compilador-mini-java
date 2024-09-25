package LexicalModule;

import Exceptions.LexicalException;
import Utilities.CharacterInspector;

public class LexicalAnalyzer
{
    private StringBuilder lexeme;
    private StringBuilder multilineSequence;
    private int lexemeLine;
    private int currentSymbol;
    private SourceFileManager fileManager;
    
    public LexicalAnalyzer(SourceFileManager fileManager)
    {
        this.fileManager = fileManager;
        currentSymbol = fileManager.nextSymbol();
    }

    public Token nextToken() throws LexicalException
    {
        lexeme = new StringBuilder();
        multilineSequence = new StringBuilder();
        return e0();
    }

    public Token e0() throws LexicalException
    {
        lexemeLine = fileManager.getCurrentLineNumber();
        
        if(CharacterInspector.isBlank(currentSymbol))
        {
            consumeSymbol();
            return e0();
        }

        else if(CharacterInspector.isSlash(currentSymbol))
        {
            appendCharacterToLexeme();
            appendCharacterToMultilineSequence();
            consumeSymbol();
            return e1();
        }

        else if(CharacterInspector.isGreaterSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o1();
        }
        else if(CharacterInspector.isLesserSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o2();
        }
        else if(CharacterInspector.isExclamationSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o3();
        }
        else if(CharacterInspector.isPlusSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o4();
        }
        else if(CharacterInspector.isMinusSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o5();
        }
        else if(CharacterInspector.isAsterisk(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o6();
        }
        else if(CharacterInspector.isAmpersand(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o7();
        }
        else if(CharacterInspector.isVerticalBar(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o8();
        }
        else if(CharacterInspector.isPercentSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o9();
        }
        else if(CharacterInspector.isEqualsSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o10();
        }

        else if(CharacterInspector.isDoubleQuotationMark(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e5();
        }
        else if(CharacterInspector.isLowercaseLetter(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e8();
        }
        else if(CharacterInspector.isUppercaseLetter(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e9();
        }
        else if(CharacterInspector.isSingleQuotationMark(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e10();
        }
        else if(CharacterInspector.isDigit(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return ed(1);
        }
        else if(CharacterInspector.isOpeningParenthesis(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p1();
        }
        else if(CharacterInspector.isClosingParenthesis(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p2();
        }
        else if(CharacterInspector.isOpeningBracket(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p3();
        }
        else if(CharacterInspector.isClosingBracket(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p4();
        }
        else if(CharacterInspector.isSemicolon(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p5();
        }
        else if(CharacterInspector.isComma(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p6();
        }
        else if(CharacterInspector.isPeriod(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p7();
        }
        else if (CharacterInspector.isColon(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return p8();
        }
        else if(CharacterInspector.isEndOfFile(currentSymbol))
            return eEoF();
        else
        {
            String message = "'" + (char)currentSymbol + "' is not a valid character.";
            String conflictingLine = fileManager.getLineProgress() + fileManager.getLineRemainder();
            throw new LexicalException(Character.toString((char)currentSymbol), lexemeLine, fileManager.getLatestPositionRead(), message, conflictingLine);
        }
    }

    public Token o1()
    {
        if(CharacterInspector.isEqualsSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o11();
        }
        else
            return new Token(Token.OPERATOR_GREATER, lexeme.toString(), lexemeLine);
    }

    public Token o11()
    {
        return new Token(Token.OPERATOR_GREATER_OR_EQUAL, lexeme.toString(), lexemeLine);
    }

    public Token o2()
    {
        if(CharacterInspector.isEqualsSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o12();
        }
        else
            return new Token(Token.OPERATOR_LESSER, lexeme.toString(), lexemeLine);
    }

    public Token o12()
    {
        return new Token(Token.OPERATOR_LESSER_OR_EQUAL, lexeme.toString(), lexemeLine);
    }

    public Token o3()
    {
        if(CharacterInspector.isEqualsSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o13();
        }
        else
            return new Token(Token.OPERATOR_NOT, lexeme.toString(), lexemeLine);
    }

    public Token o13()
    {
        return new Token(Token.OPERATOR_NOT_EQUALS, lexeme.toString(), lexemeLine);
    }

    public Token o4()
    {
        if(CharacterInspector.isPlusSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o14();
        }
        else
            return new Token(Token.OPERATOR_PLUS, lexeme.toString(), lexemeLine);
    }

    public Token o14()
    {
        return new Token(Token.OPERATOR_INCREMENT, lexeme.toString(), lexemeLine);
    }

    public Token o5()
    {
        if(CharacterInspector.isMinusSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o15();
        }
        else
            return new Token(Token.OPERATOR_MINUS, lexeme.toString(), lexemeLine);
    }

    public Token o15()
    {
        return new Token(Token.OPERATOR_DECREMENT, lexeme.toString(), lexemeLine);
    }

    public Token o6()
    {
        return new Token(Token.OPERATOR_MULTIPLICATION, lexeme.toString(), lexemeLine);
    }

    public Token o7() throws LexicalException
    {
        if(CharacterInspector.isAmpersand(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o16();
        }
        else if (CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "Expected '&', but encountered End of File.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, multilineSequence.toString());
        }
        else if(CharacterInspector.isNewLine(currentSymbol))
        {
            String message = "Expected '&', but encountered new line.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getPreviousLine());
        }
        else
        {
            String message = "Expected '&', but encountered '" + (char)currentSymbol + "'.";
            String conflictingLine = fileManager.getLineProgress() + fileManager.getLineRemainder();
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, conflictingLine);
        }
    }

    public Token o16()
    {
        return new Token(Token.OPERATOR_AND, lexeme.toString(), lexemeLine);
    }

    public Token o8() throws LexicalException
    {
        if(CharacterInspector.isVerticalBar(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o17();
        }
        else if (CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "Expected '|', but encountered End of File.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getLineProgress());
        }
        else if(CharacterInspector.isNewLine(currentSymbol))
        {
            String message = "Expected '|', but encountered new line.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getPreviousLine());
        }
        else
        {
            String message = "Expected '|', but encountered '" + (char)currentSymbol + "'.";
            String conflictingLine = fileManager.getLineProgress() + fileManager.getLineRemainder();
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, conflictingLine);
        }
    }

    public Token o17()
    {
        return new Token(Token.OPERATOR_OR, lexeme.toString(), lexemeLine);
    }

    public Token o9()
    {
        return new Token(Token.OPERATOR_MODULO, lexeme.toString(), lexemeLine);
    }

    public Token o10()
    {
        if(CharacterInspector.isEqualsSign(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return o18();
        }
        else
            return new Token(Token.OPERATOR_ASSIGN, lexeme.toString(), lexemeLine);
    }

    public Token o18()
    {
        return new Token(Token.OPERATOR_EQUALS, lexeme.toString(), lexemeLine);
    }

    public Token p1()
    {
        return new Token(Token.PUNCTUATION_OPENING_PARENTHESIS, lexeme.toString(),lexemeLine);
    }

    public Token p2()
    {
        return new Token(Token.PUNCTUATION_CLOSING_PARENTHESIS, lexeme.toString(), lexemeLine);
    }

    public Token p3()
    {
        return new Token(Token.PUNCTUATION_OPENING_BRACKET, lexeme.toString(), lexemeLine);
    }

    public Token p4()
    {
        return new Token(Token.PUNCTUATION_CLOSING_BRACKET, lexeme.toString(), lexemeLine);
    }

    public Token p5()
    {
        return new Token(Token.PUNCTUATION_SEMICOLON, lexeme.toString(), lexemeLine);
    }

    public Token p6()
    {
        return new Token(Token.PUNCTUATION_COMMA, lexeme.toString(), lexemeLine);
    }

    public Token p7()
    {
        return new Token(Token.PUNCTUATION_PERIOD, lexeme.toString(), lexemeLine);
    }

    public Token p8()
    {
        return new Token(Token.PUNCTUATION_COLON, lexeme.toString(), lexemeLine);
    }

    public Token e1() throws LexicalException
    {
        if(CharacterInspector.isSlash(currentSymbol))
        {
            flushLexeme();
            flushMultilineSequence();
            consumeSymbol();
            return e2();
        }
        if(CharacterInspector.isAsterisk(currentSymbol))
        {
            flushLexeme();
            appendCharacterToMultilineSequence();
            consumeSymbol();
            return e3();
        }
        else
            return new Token(Token.OPERATOR_DIVISION, lexeme.toString(), lexemeLine);
    }

    public Token e2() throws LexicalException
    {
        if(CharacterInspector.isNewLine(currentSymbol))
        {
            consumeSymbol();
            return e0();
        }
        else if(CharacterInspector.isEndOfFile(currentSymbol))
            return eEoF();
        else
        {
            consumeSymbol();
            return e2();
        }
    }

    public Token e3() throws LexicalException
    {
        if(CharacterInspector.isAsterisk(currentSymbol))
        {
            appendCharacterToMultilineSequence();
            consumeSymbol();
            return e4();
        }
        else if(CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "Multiline comment interrupted by unexpected end of file in line " + fileManager.getCurrentLineNumber() + ".";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, multilineSequence.toString());
        }
        else
        {
            appendCharacterToMultilineSequence();
            consumeSymbol();
            return e3();
        }
    }

    public Token e4() throws LexicalException
    {
        if(CharacterInspector.isSlash(currentSymbol))
        {
            flushMultilineSequence();
            consumeSymbol();
            return e0();
        }
        else if(CharacterInspector.isAsterisk(currentSymbol))
        {
            appendCharacterToMultilineSequence();
            consumeSymbol();
            return e4();
        }
        else if(CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "Multiline comment interrupted by unexpected end of file in line " + fileManager.getCurrentLineNumber() + ".";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, multilineSequence.toString());
        }
        else
        {
            appendCharacterToMultilineSequence();
            consumeSymbol();
            return e3();
        }
    }

    public Token e5() throws LexicalException
    {
        if(CharacterInspector.isDoubleQuotationMark(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e7();
        }
        else if (CharacterInspector.isBackslash(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e6();
        }
        else if (CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "String literal interrupted by unexpected End of File.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getLineProgress());
        }
        else if(CharacterInspector.isNewLine(currentSymbol))
        {
            String message = "String literal interrupted by unexpected new line.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getPreviousLine());
        }
        else
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e5();
        }
    }

    public Token e6() throws LexicalException
    {
        if (CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "String literal interrupted by unexpected End of File.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getLineProgress());
        }
        else if(CharacterInspector.isNewLine(currentSymbol))
        {
            String message = "String literal interrupted by unexpected new line.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getPreviousLine());
        }
        else
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e5();
        }
    }

    public Token e7()
    {
        return new Token(Token.LITERAL_STRING, lexeme.toString(), lexemeLine);
    }

    public Token e8()
    {
        if(CharacterInspector.isLetter(currentSymbol) || CharacterInspector.isDigit(currentSymbol) || CharacterInspector.isUnderscore(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e8();
        }
        else
        {
            KeywordTable table = KeywordTable.getInstance();
            Integer token = table.getToken(lexeme.toString());
            if(token != null)
                return new Token(token, lexeme.toString(), lexemeLine);
            else
                return new Token(Token.IDENTIFIER_METHOD_VARIABLE, lexeme.toString(), lexemeLine);
        }
    }

    public Token e9()
    {
        if(CharacterInspector.isLetter(currentSymbol) || CharacterInspector.isDigit(currentSymbol) || CharacterInspector.isUnderscore(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e9();
        }
        else
        {
            KeywordTable table = KeywordTable.getInstance();
            Integer token = table.getToken(lexeme.toString());
            if(token != null)
                return new Token(token, lexeme.toString(), lexemeLine);
            else
                return new Token(Token.IDENTIFIER_CLASS, lexeme.toString(), lexemeLine);
        }
    }

    public Token e10() throws LexicalException
    {
        if(CharacterInspector.isBackslash(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e11();
        }
        else if (CharacterInspector.isSingleQuotationMark(currentSymbol))
        {
            String message = "Expected character different from '''.";
            String conflictingLine = fileManager.getLineProgress() + fileManager.getLineRemainder();
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, conflictingLine);
        }
        else if (CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "Character literal interrupted by unexpected End of File.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getLineProgress());
        }
        else if(CharacterInspector.isNewLine(currentSymbol))
        {
            String message = "Character literal interrupted by unexpected new line.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getPreviousLine());
        }
        else
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e12();
        }
    }

    public Token e11() throws LexicalException
    {
        if (CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "Character literal interrupted by unexpected End of File.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getPreviousLine());
        }
        else if(CharacterInspector.isNewLine(currentSymbol))
        {
            String message = "Character literal interrupted by unexpected new line.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getLineProgress());
        }
        else
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e12();
        }
    }

    public Token e12() throws LexicalException
    {
        if(CharacterInspector.isSingleQuotationMark(currentSymbol))
        {
            appendCharacterToLexeme();
            consumeSymbol();
            return e13();
        }
        else if (CharacterInspector.isEndOfFile(currentSymbol))
        {
            String message = "Character literal interrupted by unexpected End of File.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getLineProgress());
        }
        else if(CharacterInspector.isNewLine(currentSymbol))
        {
            String message = "Character literal interrupted by unexpected new line.";
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, fileManager.getPreviousLine());
        }
        else
        {
            String message = "Expected ''', but encountered '" + (char)currentSymbol + "'.";
            String conflictingLine = fileManager.getLineProgress() + fileManager.getLineRemainder();
            throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, conflictingLine);
        }
    }

    public Token e13()
    {
        return new Token(Token.LITERAL_CHAR, lexeme.toString(), lexemeLine);
    }

    public Token ed(int digits) throws LexicalException
    {
        if(CharacterInspector.isDigit(currentSymbol))
        {
            if(digits < 9) {
                appendCharacterToLexeme();
                consumeSymbol();
                return ed(digits + 1);
            }
            else
            {
                String message = "Numeric literal exceeding the allowed length of 9.";
                String conflictingLine = fileManager.getLineProgress() + fileManager.getLineRemainder();
                throw new LexicalException(lexeme.toString(), lexemeLine, fileManager.getLatestPositionRead(), message, conflictingLine);
            }
        }
        else
            return new Token(Token.LITERAL_INT, lexeme.toString(), lexemeLine);
    }

    public Token eEoF()
    {
        return new Token (Token.EOF, "", lexemeLine);
    }

    private void consumeSymbol()
    {
        currentSymbol = fileManager.nextSymbol();
    }

    private void appendCharacterToLexeme()
    {
        lexeme.append((char)currentSymbol);
    }

    private void flushMultilineSequence()
    {
        multilineSequence = new StringBuilder();
    }

    private void appendCharacterToMultilineSequence()
    {
        multilineSequence.append((char)currentSymbol);
    }

    private void flushLexeme()
    {
        lexeme = new StringBuilder();
    }

    public boolean isEndOfFile(Token token)
    {
        return token.getTokenID() == Token.EOF;
    }
}

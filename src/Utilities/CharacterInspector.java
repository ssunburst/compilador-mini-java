package Utilities;

public class CharacterInspector
{
    public static boolean isEndOfFile(int i)
    {
        return i == -1;
    }

    public static boolean isBlank(int i)
    {
        return isTabulation(i) || isWhitespace(i) || isCarriageReturn(i) || isNewLine(i);
    }

    public static boolean isWhitespace(int i)
    {
        return i == 32;
    }

    public static boolean isCarriageReturn(int i)
    {
        return i == 13;
    }

    public static boolean isNewLine(int i)
    {
          return i == 10;
    }

    public static boolean isSlash(int i)
    {
        return i == 47;
    }

    public static boolean isUnderscore(int i)
    {
        return i == 95;
    }

    public static boolean isAsterisk(int i)
    {
        return i == 42;
    }

    public static boolean isDoubleQuotationMark(int i)
    {
        return i == 34;
    }

    public static boolean isSingleQuotationMark(int i)
    {
        return i == 39;
    }

    public static boolean isBackslash(int i)
    {
        return i == 92;
    }

    public static boolean isLowercaseLetter(int i)
    {
        return (i >= 97) && (i <= 122);
    }

    public static boolean isUppercaseLetter(int i)
    {
        return (i >= 65) && (i <= 90);
    }

    public static boolean isLetter(int i)
    {
        return isLowercaseLetter(i) || isUppercaseLetter(i);
    }

    public static boolean isDigit(int i)
    {
        return (i >= 48) && (i <= 57);
    }

    public static boolean isOpeningParenthesis(int i)
    {
        return i == 40;
    }

    public static boolean isClosingParenthesis(int i)
    {
        return i == 41;
    }

    public static boolean isOpeningBracket(int i)
    {
        return i == 123;
    }

    public static boolean isClosingBracket(int i)
    {
        return i == 125;
    }

    public static boolean isSemicolon(int i)
    {
        return i == 59;
    }

    public static boolean isComma(int i)
    {
        return i == 44;
    }

    public static boolean isPeriod(int i)
    {
        return i == 46;
    }

    public static boolean isGreaterSign(int i)
    {
        return i == 62;
    }

    public static boolean isLesserSign(int i)
    {
        return i == 60;
    }

    public static boolean isExclamationSign(int i)
    {
        return i == 33;
    }

    public static boolean isEqualsSign(int i)
    {
        return i == 61;
    }

    public static boolean isPlusSign(int i)
    {
        return i == 43;
    }

    public static boolean isMinusSign(int i)
    {
        return i == 45;
    }

    public static boolean isAmpersand(int i)
    {
        return i == 38;
    }

    public static boolean isVerticalBar(int i)
    {
        return i == 124;
    }

    public static boolean isPercentSign(int i)
    {
        return i == 37;
    }

    public static boolean isColon(int i)
    {
        return i == 58;
    }

    public static boolean isTabulation(int i)
    {
        return i == 9;
    }
}

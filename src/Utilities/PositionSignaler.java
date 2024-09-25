package Utilities;

public class PositionSignaler
{
    public static String signalPosition(String extract, int position)
    {
        StringBuilder signaledLine = new StringBuilder();
        signaledLine.append(extract);
        signaledLine.append("\n");

        StringBuilder addendum = new StringBuilder();
        int i, addendumLength;

        if((extract.contains("\n")) || (position >= extract.length()))
            addendumLength = position + 1;
        else
            addendumLength = extract.length();

        for(i = 0; i < addendumLength; i++)
            addendum.append(' ');
        addendum.setCharAt(position, '^');

        signaledLine.append(addendum);
        return signaledLine.toString();
    }

}

package SemanticModule;

public interface Variable
{
    int getOffset();

    void setOffset(int offset);

    void generateLeftSideCode(boolean chained);

    void generateRightSideCode(boolean chained);

}

package gameplayHook.CodeModulePackage.components;

public class Condition {
    public Expression left;
    public KnowledgeToken operator;
    public Expression right;

    public Condition(Expression left, KnowledgeToken operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
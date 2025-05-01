package gameplayHook.CodeModulePackage.components;

public class CompoundCondition extends Condition {
    public Condition left;
    public KnowledgeToken logicalOp; // AND | OR
    public Condition right;

    public CompoundCondition(Condition left, KnowledgeToken logicalOp, Condition right) {
        super(null, null, null);
        this.left = left;
        this.logicalOp = logicalOp;
        this.right = right;
    }
}
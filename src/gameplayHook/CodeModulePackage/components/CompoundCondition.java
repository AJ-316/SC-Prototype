package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public class CompoundCondition extends BooleanExpression {
    public BooleanExpression left, right;
    public KnowledgeToken logicalOp;

    public CompoundCondition(BooleanExpression left, BooleanExpression right, KnowledgeToken logicalOp) {
        this.left = left;
        this.right = right;
        this.logicalOp = logicalOp;
    }

    @Override
    public Object evaluate(MachineContext ctx) {
        return switch (logicalOp.type) {
            case AND -> left.evaluateBoolean(ctx) && right.evaluateBoolean(ctx);
            case OR -> left.evaluateBoolean(ctx) || right.evaluateBoolean(ctx);
            default -> throw new IllegalStateException("Invalid logical operator");
        };
    }
}
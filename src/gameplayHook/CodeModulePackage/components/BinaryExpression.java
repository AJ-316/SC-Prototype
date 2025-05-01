package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public class BinaryExpression extends Expression {
    public Expression left;
    public Expression right;
    public KnowledgeToken arithmeticOp;

    public BinaryExpression(Expression left, Expression right, KnowledgeToken arithmeticOp) {
        this.left = left;
        this.right = right;
        this.arithmeticOp = arithmeticOp;
    }

    @Override
    public Object evaluate(MachineContext ctx) {
        Object l = left.evaluate(ctx);
        Object r = right.evaluate(ctx);

        return switch (arithmeticOp.type) {
            case DIVIDE -> toFloat(l) / toFloat(r);
            case MULTIPLY -> toFloat(l) * toFloat(r);
            case ADD -> toFloat(l) + toFloat(r);
            case SUBTRACT -> toFloat(l) - toFloat(r);

            default -> throw new IllegalStateException("Unsupported binary op");
        };
    }
}

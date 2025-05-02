package gameplayHook.CodeModulePackage.components.expressions.conditonal;

import gameplayHook.CodeModulePackage.components.expressions.UnaryExpression;
import gameplayHook.MachinePackage.components.MachineContext;

public class UnaryCondition extends BooleanExpression {
    public UnaryExpression expr;

    public UnaryCondition(UnaryExpression expr) {
        this.expr = expr;
    }

    public Object evaluate(MachineContext ctx) {
        Object value = expr.evaluate(ctx);

        if (!(value instanceof Boolean))
            throw new IllegalStateException("Expected boolean");

        return value;
    }
}
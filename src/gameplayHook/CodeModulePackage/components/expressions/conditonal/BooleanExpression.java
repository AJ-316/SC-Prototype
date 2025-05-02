package gameplayHook.CodeModulePackage.components.expressions.conditonal;

import gameplayHook.CodeModulePackage.components.expressions.Expression;
import gameplayHook.MachinePackage.components.MachineContext;

public abstract class BooleanExpression extends Expression {

    public boolean evaluateBoolean(MachineContext ctx) {
        Object result = evaluate(ctx);

        if (!(result instanceof Boolean))
            throw new IllegalStateException("Evaluation is not a Boolean");

        return (boolean) result;
    }

}

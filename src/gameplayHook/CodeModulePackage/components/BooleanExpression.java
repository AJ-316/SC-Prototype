package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public abstract class BooleanExpression extends Expression {

    public boolean evaluateBoolean(MachineContext ctx) {
        Object result = evaluate(ctx);

        if(!(result instanceof Boolean))
            throw new IllegalStateException("Evaluation is not a Boolean");

        return (boolean) result;
    }

}

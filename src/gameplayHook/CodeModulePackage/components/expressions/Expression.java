package gameplayHook.CodeModulePackage.components.expressions;

import gameplayHook.MachinePackage.components.MachineContext;

public abstract class Expression {
    public abstract Object evaluate(MachineContext ctx);

    protected float toFloat(Object val) {
        if (val instanceof Number) return ((Number) val).floatValue();
        throw new IllegalArgumentException("Value is not a number: " + val);
    }
}

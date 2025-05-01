package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public abstract class Expression {
    public abstract Object evaluate(MachineContext ctx);

    protected float toFloat(Object val) {
        if (val instanceof Number) return ((Number) val).floatValue();
        throw new IllegalArgumentException("Value is not a number: " + val);
    }
}

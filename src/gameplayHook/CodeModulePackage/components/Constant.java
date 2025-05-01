package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public class Constant extends Expression {
    private final Object value;

    public Constant(Object value) {
        this.value = value;
    }

    @Override
    public Object evaluate(MachineContext ctx) {
        return value;
    }
}
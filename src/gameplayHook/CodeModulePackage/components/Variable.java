package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public class Variable extends Expression {
    public String name;
    private Object value;

    public Variable(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object evaluate(MachineContext ctx) {
        Variable variable = ctx.getVar(name);
        return variable != null ? variable.value : null;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public class Variable extends Expression {
    public String name;
    public Object value;

    public Variable(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Object resolveValue(MachineContext ctx) {
        if(!ctx.getVar(name).equals(this)) return null;
        return value;
    }
}
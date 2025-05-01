package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

public class Variable extends Expression {
    public String name;

    public Variable(String name) {
        this.name = name;
    }

    public Object resolve(MachineContext ctx) {
        return ctx.get(name);
    }
}
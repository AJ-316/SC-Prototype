package gameplayHook.MachinePackage.machines;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.statements.Action;
import gameplayHook.MachinePackage.Machine;

public class TemplateMachine extends Machine {

    public TemplateMachine(String name) {
        super("_temp_" + name);
    }

    public void setVars(Variable... vars) {
        getCtx().setVars(vars);
    }

    public void setActions(Action... actions) {
        getCtx().setActions(actions);
    }
}

package gameplayHook.CodeModulePackage.machineComponents;

import gameplayHook.CodeModulePackage.components.Action;
import gameplayHook.CodeModulePackage.components.Constant;
import gameplayHook.CodeModulePackage.components.Expression;
import gameplayHook.CodeModulePackage.components.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineContext {
    private final List<Variable> variables = new ArrayList<>();
    private final List<Action> actions = new ArrayList<>();

    public void setAction(Action action) {
        actions.add(action);
    }

    public Action getAction(String name) {
        for(Action action : actions) {
            if(action.methodName.equals(name)) return action;
        }
        return null;
    }

    public void setVar(Variable variable) {
        variables.add(variable);
    }

    public Object getVar(String name) {
        for(Variable variable : variables) {
            if(variable.name.equals(name)) return variable;
        }
        return null;
    }
}

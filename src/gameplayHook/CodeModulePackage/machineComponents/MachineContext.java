package gameplayHook.CodeModulePackage.machineComponents;

import gameplayHook.CodeModulePackage.components.Action;
import gameplayHook.CodeModulePackage.components.Variable;

import java.util.ArrayList;
import java.util.List;

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

    public void updateVar(String name, Object value) {
        if(value == null) return;

        for(Variable variable : variables) {
            if(variable.name.equals(name)) {
                if(variable.value.getClass().equals(value.getClass())) {
                    variable.value = value;
                }
                break;
            }
        }
    }

    public void setVar(Variable variable) {
        variables.add(variable);
    }

    public Variable getVar(String name) {
        for(Variable variable : variables) {
            if(variable.name.equals(name)) return variable;
        }
        return null;
    }
}

package gameplayHook.MachinePackage.components;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.statements.Action;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

public class MachineContext {

    private final String machineName;
    private Variable[] variables;
    private Action[] actions;

    public MachineContext(String machineName) {
        this.machineName = machineName;
    }

    public Variable[] getVariables() {
        return variables;
    }

    public Action[] getActions() {
        return actions;
    }

    public void updateVar(String name, Object value) {
        if (value == null) return;

        for (Variable variable : variables) {
            if (variable.getName().equals(name)) {
                Object oldVal = variable.getValue();
                if (oldVal.getClass().equals(value.getClass())) {
                    variable.setValue(value);
                    return;
                }

                if (oldVal instanceof Number && value instanceof Number newValue) {
                    variable.setValue(
                            switch (oldVal) {
                                case Integer _ -> newValue.intValue();
                                case Double _ -> newValue.doubleValue();
                                case Long _ -> newValue.longValue();
                                default -> newValue.floatValue();
                            }
                    );
                    return;
                }

                System.err.println("Type mismatch: Cannot assign " + value.getClass().getSimpleName() +
                        " to variable of type " + oldVal.getClass().getSimpleName());
                return;
            }
        }
    }

    public void setVars(Variable... variables) {
        this.variables = variables;
        SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_UPDATE_CONTEXT, this);
    }

    public void setActions(Action... actions) {
        this.actions = actions;
        SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_UPDATE_CONTEXT, this);
    }

    public Variable getVar(String name) {
        for (Variable variable : variables) {
            if (variable.getName().equals(name)) return variable;
        }
        return null;
    }

    public Action getAction(String name) {
        for (Action action : actions) {
            if (action.getName().equals(name)) return action;
        }
        return null;
    }

    public String getMachineName() {
        return machineName;
    }
}

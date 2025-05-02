package gameplayHook.MachinePackage.components;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.statements.Action;

public class MachineContext {
    private Variable[] variables;
    private Action[] actions;

    public void updateVar(String name, Object value) {
        if (value == null) return;

        for (Variable variable : variables) {
            if (variable.name.equals(name)) {
                Object oldVal = variable.getValue();
                // If same type, assign directly
                if (oldVal.getClass().equals(value.getClass())) {
                    variable.setValue(value);
                    return;
                }

                // Allow numeric conversions
                if (oldVal instanceof Number && value instanceof Number newValue) {
                    variable.setValue(
                            switch (oldVal) {
                                case Integer _ -> newValue.intValue();
                                case Double _ -> newValue.doubleValue();
                                case Long _ -> newValue.longValue();
                                default -> newValue.floatValue(); // Fall back to Float
                            }
                    );
                    return;
                }

                // Else, do not assign (incompatible types)
                System.err.println("Type mismatch: Cannot assign " + value.getClass().getSimpleName() +
                        " to variable of type " + oldVal.getClass().getSimpleName());
                return;
            }
        }
    }

    public void setVars(Variable... variables) {
        this.variables = variables;
    }

    public void setActions(Action... actions) {
        this.actions = actions;
    }

    public Variable getVar(String name) {
        for (Variable variable : variables) {
            if (variable.name.equals(name)) return variable;
        }
        return null;
    }

    public Action getAction(String name) {
        for (Action action : actions) {
            if (action.getName().equals(name)) return action;
        }
        return null;
    }
}

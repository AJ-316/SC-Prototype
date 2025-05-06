package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.MachinePackage.components.MachineContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Action {
    private final String methodName;
    private final ActionMethod method;
    private final List<String> arguments;

    public Action(String methodName, List<String> arguments, ActionMethod method) {
        this.methodName = methodName;
        this.arguments = arguments;
        this.method = method;
    }

    public void run(MachineContext ctx) {
        if (arguments == null) {
            method.run(null);
            return;
        }

        List<Variable> variables = new ArrayList<>();
        for (String variable : arguments)
            variables.add(ctx.getVar(variable));

        method.run(variables);
    }

    public String getName() {
        return methodName;
    }

    public Object[] getArgs() {
        return arguments.toArray();
    }

    public interface ActionMethod {
        // Might just replace List of variables with the actual Machine Context
        // This can negate the creation of list of variables but will give access to the whole Machine
        void run(List<Variable> arguments);
    }
}
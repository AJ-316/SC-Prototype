package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

import java.util.ArrayList;
import java.util.List;

public class Action {
    public String methodName;
    public ActionMethod method;
    public List<String> arguments;

    public Action(String methodName, List<String> arguments, ActionMethod method) {
        this.methodName = methodName;
        this.arguments = arguments;
        this.method = method;
    }

    public void doAction(MachineContext ctx) {
        if(arguments == null) {
            method.run(null);
            return;
        }

        List<Variable> variables = new ArrayList<>();
        for (String variable : arguments)
            variables.add(ctx.getVar(variable));

        method.run(variables);
    }

    public interface ActionMethod {
        // Might just replace List of variables with the actual Machine Context
        // This can negate the creation of list of variables but gives access to the whole Machine
        void run(List<Variable> arguments);
    }
}
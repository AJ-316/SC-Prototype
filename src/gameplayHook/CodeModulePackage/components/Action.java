package gameplayHook.CodeModulePackage.components;

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

    public void doAction(List<Variable> arguments) {
        method.run(arguments);
    }


    public interface ActionMethod {
        void run(List<Variable> arguments);
    }
}
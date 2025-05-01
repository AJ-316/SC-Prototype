package gameplayHook.CodeModulePackage.components;

import java.util.List;

public class Action {
    public String methodName;
    public List<Expression> arguments;

    public Action(String methodName) {
        this.methodName = methodName;
    }
}
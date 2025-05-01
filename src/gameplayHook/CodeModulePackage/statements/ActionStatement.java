package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.Action;

public class ActionStatement extends Statement {
    public Action action;

    public ActionStatement(Action action) {
        this.action = action;
    }
}
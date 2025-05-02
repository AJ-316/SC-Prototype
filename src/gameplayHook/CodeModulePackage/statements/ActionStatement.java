package gameplayHook.CodeModulePackage.statements;

import gameplayHook.MachinePackage.components.MachineContext;

import static gameplayHook.CodeModulePackage.CodeModule.C_GREEN;
import static gameplayHook.CodeModulePackage.CodeModule.C_RESET;

public class ActionStatement implements ExpressionStatement {

    private final Action action;

    public ActionStatement(Action action) {
        this.action = action;
    }

    public static ActionStatement create(Action action) {
        return new ActionStatement(action);
    }

    @Override
    public void run(MachineContext ctx) {
        System.out.println(C_GREEN + "\t=> Executing action: " + C_RESET + action.getName());

        Action ctxAction = ctx.getAction(action.getName());
        ctxAction.run(ctx);
    }

    public Action getAction() {
        return action;
    }
}
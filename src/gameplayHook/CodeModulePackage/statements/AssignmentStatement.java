package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.MachinePackage.components.MachineContext;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import static gameplayHook.CodeModulePackage.CodeModule.*;

public class AssignmentStatement implements ExpressionStatement {

    public Assignment assignment;

    public AssignmentStatement(Assignment assignment) {
        this.assignment = assignment;
    }

    public static AssignmentStatement create(Assignment assignment) {
        return new AssignmentStatement(assignment);
    }

    @Override
    public void run(MachineContext ctx) {
        Object newValue = assignment.value.evaluate(ctx);
        if (newValue == null) throw new IllegalStateException("Null Value at Assignment: " + assignment.target.getName());

        Variable target = ctx.getVar(assignment.target.getName());
        Object preVal = target.getValue();

        ctx.updateVar(assignment.target.getName(), newValue);
        SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_UPDATE_CONTEXT, ctx);

        System.out.println(C_BLUE + "\t=> Assigning " + C_RESET + target.getName() + C_BLUE + "[" + C_RESET + preVal + C_BLUE + "]" +
                " = " + C_CYAN + "[" + C_RESET + target.getValue() + C_CYAN + "]" + C_RESET);
    }
}
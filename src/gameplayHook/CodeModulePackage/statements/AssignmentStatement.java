package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.MachinePackage.components.MachineContext;

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
        if (newValue == null) throw new IllegalStateException("Null Value at Assignment: " + assignment.target.name);

        Variable target = ctx.getVar(assignment.target.name);
        Object preVal = target.getValue();
        ctx.updateVar(assignment.target.name, newValue);

        System.out.println(C_BLUE + "\t=> Assigning " + C_RESET + target.name + C_BLUE + "[" + C_RESET + preVal + C_BLUE + "]" +
                " = " + C_CYAN + "[" + C_RESET + target.getValue() + C_CYAN + "]" + C_RESET);
    }
}
package gameplayHook.CodeModulePackage;

import gameplayHook.CodeModulePackage.components.*;
import gameplayHook.CodeModulePackage.machineComponents.MachineContext;
import gameplayHook.CodeModulePackage.statements.*;

public class Runner {
    public static final String C_RESET = "\u001B[0m";
    public static final String C_BLACK = "\u001B[30m";
    public static final String C_RED = "\u001B[31m";
    public static final String C_GREEN = "\u001B[32m";
    public static final String C_YELLOW = "\u001B[33m";
    public static final String C_BLUE = "\u001B[34m";
    public static final String C_PURPLE = "\u001B[35m";
    public static final String C_CYAN = "\u001B[36m";
    public static final String C_WHITE = "\u001B[37m";

    public void run(Statement statement, MachineContext ctx) {
        if (statement instanceof IfStatement) {
            runIf((IfStatement) statement, ctx);
        } else if (statement instanceof WhileStatement) {
            runWhile((WhileStatement) statement, ctx);
        } else if (statement instanceof ActionStatement) {
            runAction((ActionStatement) statement, ctx);
        } else if (statement instanceof AssignmentStatement) {
            runAssignment((AssignmentStatement) statement, ctx);
        }
    }

    private void runIf(IfStatement ifStmt, MachineContext ctx) {
        if (ifStmt.condition.evaluateBoolean(ctx)) {
            for (Statement stmt : ifStmt.thenBlock) {
                run(stmt, ctx);
            }
        } else if (ifStmt.elseBlock != null) {
            for (Statement stmt : ifStmt.elseBlock) {
                run(stmt, ctx);
            }
        }
    }

    private float toFloat(Object val) {
        if (val instanceof Number) return ((Number) val).floatValue();
        throw new IllegalArgumentException("Value is not a number: " + val);
    }

    private void runAction(ActionStatement actionStmt, MachineContext ctx) {
        System.out.println(C_GREEN + "\t=> Executing action: " + C_RESET + actionStmt.action.methodName);

        Action action = ctx.getAction(actionStmt.action.methodName);
        action.doAction(ctx);
    }

    private void runAssignment(AssignmentStatement assignStmt, MachineContext ctx) {
        Object newValue = assignStmt.assignment.value.evaluate(ctx);
        if(newValue == null) throw new IllegalStateException("Null Value at Assignment: " + assignStmt.assignment.target.name);

        Variable target = ctx.getVar(assignStmt.assignment.target.name);
        Object preVal = target.getValue();
        ctx.updateVar(assignStmt.assignment.target.name, newValue);

        System.out.println(C_BLUE + "\t=> Assigning " + C_RESET + target.name + C_BLUE + "[" + C_RESET + preVal + C_BLUE + "]" +
                " = " + C_CYAN + "[" + C_RESET + target.getValue() + C_CYAN + "]" + C_RESET);
    }

    private void runWhile(WhileStatement whileStmt, MachineContext ctx) {
        while (whileStmt.condition.evaluateBoolean(ctx)) {
            for (Statement stmt : whileStmt.body) {
                run(stmt, ctx);
            }
        }
    }
}

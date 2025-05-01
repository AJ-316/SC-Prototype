package gameplayHook.CodeModulePackage;

import gameplayHook.CodeModulePackage.components.*;
import gameplayHook.CodeModulePackage.machineComponents.MachineContext;
import gameplayHook.CodeModulePackage.statements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (evaluateCondition(ifStmt.condition, ctx)) {
            for (Statement stmt : ifStmt.thenBlock) {
                run(stmt, ctx);
            }
        } else if (ifStmt.elseBlock != null) {
            for (Statement stmt : ifStmt.elseBlock) {
                run(stmt, ctx);
            }
        }
    }

    private boolean evaluateCondition(Condition cond, MachineContext ctx) {
        if (cond instanceof CompoundCondition compound) {
            boolean leftResult = evaluateCondition(compound.left, ctx);
            boolean rightResult = evaluateCondition(compound.right, ctx);

            return switch (compound.logicalOp.type) {
                case AND -> leftResult && rightResult;
                case OR -> leftResult || rightResult;
                default -> throw new IllegalStateException("Invalid logical operator");
            };
        } else {
            Object leftVal = resolveValue(cond.left, ctx);
            Object rightVal = resolveValue(cond.right, ctx);
            System.out.println(C_PURPLE + "\t=> Evaluating condition: " + C_RESET + leftVal + " " + C_CYAN + cond.operator.type.name() + C_RESET + " " + rightVal);
            return switch (cond.operator.type) {
                case EQUALS -> {
                    if (leftVal instanceof Number && rightVal instanceof Number) {
                        yield toFloat(leftVal) == toFloat(rightVal);
                    }
                    yield Objects.equals(leftVal, rightVal);
                }
                case GREATER_THAN -> toFloat(leftVal) > toFloat(rightVal);
                case LESS_THAN -> toFloat(leftVal) < toFloat(rightVal);
                default -> throw new IllegalStateException("Invalid comparison operator");
            };
        }
    }

    private Object resolveValue(Expression expr, MachineContext ctx) {
        if (expr instanceof Constant) return ((Constant) expr).value;
        if (expr instanceof Variable) return ((Variable) expr).resolveValue(ctx);
        throw new IllegalArgumentException("Unsupported expression type");
    }

    private float toFloat(Object val) {
        if (val instanceof Number) return ((Number) val).floatValue();
        throw new IllegalArgumentException("Value is not a number: " + val);
    }

    private void runAction(ActionStatement actionStmt, MachineContext ctx) {
        System.out.println(C_GREEN + "\t=> Executing action: " + C_RESET + actionStmt.action.methodName);

        Action action = ctx.getAction(actionStmt.action.methodName);
        if(action.arguments == null) {
            action.doAction(null);
            return;
        }

        List<Variable> variables = new ArrayList<>();
        for (String variable : action.arguments)
            variables.add(ctx.getVar(variable));

        action.doAction(variables);
    }

    private void runAssignment(AssignmentStatement assignStmt, MachineContext ctx) {
        Object newValue = getExpressionValue(assignStmt.assignment.value);
        if(newValue == null) throw new IllegalStateException("Null Value at Assignment");

        Variable target = ctx.getVar(assignStmt.assignment.target.name);
        Object preVal = target.value;
        ctx.updateVar(assignStmt.assignment.target.name, getExpressionValue(assignStmt.assignment.value));

        System.out.println(C_BLUE + "\t=> Assigning " + C_RESET + target.name + C_BLUE + "[" + C_RESET + preVal + C_BLUE + "]" +
                " = " + C_CYAN + "[" + C_RESET + target.value + C_CYAN + "]" + C_RESET);
    }

    public Object getExpressionValue(Expression expression) {
        if(expression instanceof Variable variable) return variable.value;
        if(expression instanceof Constant constant) return constant.value;
        return null;
    }

    private void runWhile(WhileStatement whileStmt, MachineContext ctx) {
        while (evaluateCondition(whileStmt.condition, ctx)) {
            for (Statement stmt : whileStmt.body) {
                run(stmt, ctx);
            }
        }
    }
}

package gameplayHook.CodeModulePackage;

import gameplayHook.CodeModulePackage.components.*;
import gameplayHook.CodeModulePackage.machineComponents.MachineContext;
import gameplayHook.CodeModulePackage.statements.*;

import java.util.Objects;

public class Runner {
    public void run(Statement statement, MachineContext ctx) {
        if (statement instanceof IfStatement) {
            runIf((IfStatement) statement, ctx);
        } else if (statement instanceof WhileStatement) {
            runWhile((WhileStatement) statement, ctx);
        } else if (statement instanceof ActionStatement) {
            runAction((ActionStatement) statement);
        } else if (statement instanceof AssignmentStatement) {
            runAssignment((AssignmentStatement) statement);
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
            System.out.println("\t=> Evaluating condition: " + leftVal + " " + cond.operator.type.name() + " " + rightVal);
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
        if (expr instanceof Variable) return ((Variable) expr).resolve(ctx);
        throw new IllegalArgumentException("Unsupported expression type");
    }

    private float toFloat(Object val) {
        if (val instanceof Number) return ((Number) val).floatValue();
        throw new IllegalArgumentException("Value is not a number: " + val);
    }

    private void runAction(ActionStatement actionStmt) {
        System.out.println("Executing action: " + actionStmt.action.methodName);
    }

    private void runAssignment(AssignmentStatement assignStmt) {
        System.out.println("Assigning " + assignStmt.target.name + " = [value]");
    }

    private void runWhile(WhileStatement whileStmt, MachineContext ctx) {
        while (evaluateCondition(whileStmt.condition, ctx)) {
            for (Statement stmt : whileStmt.body) {
                run(stmt, ctx);
            }
            break; // REMOVE this break if you implement proper condition changes
        }
    }
}

package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

import java.util.Objects;

import static gameplayHook.CodeModulePackage.Runner.*;

public class BinaryCondition extends BooleanExpression {

    public Expression left, right;
    public KnowledgeToken operator;

    public BinaryCondition(Expression left, Expression right, KnowledgeToken operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Object evaluate(MachineContext ctx) {
        Object leftVal = left.evaluate(ctx);
        Object rightVal = right.evaluate(ctx);

        System.out.println(C_PURPLE + "\t=> Evaluating condition: " + C_RESET + leftVal + " " + C_CYAN + operator.type.name() + C_RESET + " " + rightVal);
        return switch (operator.type) {
            case EQUALS -> {
                if (leftVal instanceof Number && rightVal instanceof Number) {
                    yield toFloat(leftVal) == toFloat(rightVal);
                }
                yield Objects.equals(leftVal, rightVal);
            }
            case NOT_EQUALS -> {
                if (leftVal instanceof Number && rightVal instanceof Number) {
                    yield toFloat(leftVal) != toFloat(rightVal);
                }
                yield !Objects.equals(leftVal, rightVal);
            }
            case GREATER_THAN -> toFloat(leftVal) > toFloat(rightVal);
            case GREATER_OR_EQUALS -> toFloat(leftVal) >= toFloat(rightVal);
            case LESS_THAN -> toFloat(leftVal) < toFloat(rightVal);
            case LESS_OR_EQUALS -> toFloat(leftVal) <= toFloat(rightVal);
            default -> throw new IllegalStateException("Invalid comparison operator");
        };
    }
}
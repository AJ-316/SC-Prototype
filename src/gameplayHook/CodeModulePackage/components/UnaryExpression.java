package gameplayHook.CodeModulePackage.components;

import gameplayHook.CodeModulePackage.machineComponents.MachineContext;

import static gameplayHook.CodeModulePackage.Runner.*;
import static gameplayHook.CodeModulePackage.Runner.C_RESET;

public class UnaryExpression extends Expression {
    public Expression operand;
    public KnowledgeToken unaryOp;

    public UnaryExpression(Expression operand, KnowledgeToken unaryOp) {
        this.operand = operand;
        this.unaryOp = unaryOp;
    }

    @Override
    public Object evaluate(MachineContext ctx) {
        Object value = operand.evaluate(ctx);

        return switch (unaryOp.type) {
            case NOT -> {
                if (!(value instanceof Boolean)) {
                    throw new IllegalArgumentException("NOT operator requires a Boolean value");
                }
                System.out.println(C_PURPLE + "\t=> Evaluating condition: " + C_CYAN + "!" + C_RESET +
                        (operand instanceof Variable ? ((Variable)operand).name : "") + C_CYAN + "[" + C_RESET + value + C_CYAN + "]" + C_RESET);
                yield !(Boolean) value;
            }
            case NEGATE -> {
                if (!(value instanceof Number)) {
                    throw new IllegalArgumentException("NEGATE operator requires a Number value");
                }
                yield -((Number) value).floatValue();
            }
            default -> throw new IllegalStateException("Unsupported unary operator: " + unaryOp.type);
        };
    }
}

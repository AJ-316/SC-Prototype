package gameplayHook.CodeModulePackage.components.expressions;

import gameplayHook.CodeModulePackage.components.tokens.KnowledgeToken;
import gameplayHook.MachinePackage.components.MachineContext;

import static gameplayHook.CodeModulePackage.CodeModule.*;

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
            case NONE -> {
                if (!(value instanceof Boolean)) {
                    throw new IllegalArgumentException("Unary operator(NONE) requires a Boolean value");
                }
                System.out.println(C_PURPLE + "\t=> Evaluating condition: " + C_RESET +
                        (operand instanceof Variable ? ((Variable) operand).name : "") + C_CYAN + "[" + C_RESET + value + C_CYAN + "]" + C_RESET);
                yield (Boolean) value;
            }
            case NOT -> {
                if (!(value instanceof Boolean)) {
                    throw new IllegalArgumentException("Unary operator(NOT) requires a Boolean value");
                }
                System.out.println(C_PURPLE + "\t=> Evaluating condition: " + C_CYAN + "!" + C_RESET +
                        (operand instanceof Variable ? ((Variable) operand).name : "") + C_CYAN + "[" + C_RESET + value + C_CYAN + "]" + C_RESET);
                yield !(Boolean) value;
            }
            case NEGATE -> {
                if (!(value instanceof Number)) {
                    throw new IllegalArgumentException("Unary operator(NEGATE) requires a Number value");
                }
                yield -((Number) value).floatValue();
            }
            default -> throw new IllegalStateException("Unsupported unary operator: " + unaryOp.type);
        };
    }
}

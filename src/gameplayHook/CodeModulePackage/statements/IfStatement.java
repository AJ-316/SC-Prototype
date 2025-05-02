package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.expressions.conditonal.BooleanExpression;
import gameplayHook.MachinePackage.components.MachineContext;

import java.util.List;

public class IfStatement implements ConditionalStatement {
    public BooleanExpression condition;
    public List<Statement> thenBlock;
    public List<Statement> elseBlock;

    public IfStatement(BooleanExpression condition, List<Statement> thenBlock, List<Statement> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void run(MachineContext ctx) {
        if (condition.evaluateBoolean(ctx)) {
            for (Statement stmt : thenBlock) {
                stmt.run(ctx);
            }
        } else if (elseBlock != null) {
            for (Statement stmt : elseBlock) {
                stmt.run(ctx);
            }
        }
    }
}
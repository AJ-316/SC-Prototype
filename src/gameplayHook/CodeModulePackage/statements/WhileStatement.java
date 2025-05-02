package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.expressions.conditonal.BooleanExpression;
import gameplayHook.MachinePackage.components.MachineContext;

import java.util.List;

public class WhileStatement implements ConditionalStatement {

    public BooleanExpression condition;
    public List<Statement> body;

    public WhileStatement(BooleanExpression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void run(MachineContext ctx) {
        while (condition.evaluateBoolean(ctx)) {
            for (Statement stmt : body) {
                stmt.run(ctx);
            }
        }
    }
}

package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.BooleanExpression;

import java.util.List;

public class WhileStatement extends Statement {

    public BooleanExpression condition;
    public List<Statement> body;

    public WhileStatement(BooleanExpression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }
}

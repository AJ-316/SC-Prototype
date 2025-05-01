package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.BooleanExpression;

import java.util.List;

public class IfStatement extends Statement {
    public BooleanExpression condition;
    public List<Statement> thenBlock;
    public List<Statement> elseBlock;

    public IfStatement(BooleanExpression condition, List<Statement> thenBlock, List<Statement> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }
}
package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.Condition;

import java.util.List;

public class IfStatement extends Statement {
    public Condition condition;
    public List<Statement> thenBlock;
    public List<Statement> elseBlock;

    public IfStatement(Condition condition, List<Statement> thenBlock, List<Statement> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }
}
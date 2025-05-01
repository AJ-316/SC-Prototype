package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.Condition;

import java.util.List;

public class WhileStatement extends Statement {
    public Condition condition;
    public List<Statement> body;
}

package gameplayHook.CodeModulePackage;

import gameplayHook.CodeModulePackage.components.expressions.conditonal.BooleanExpression;
import gameplayHook.CodeModulePackage.statements.ConditionalStatement;
import gameplayHook.CodeModulePackage.statements.IfStatement;
import gameplayHook.CodeModulePackage.statements.Statement;
import gameplayHook.CodeModulePackage.statements.WhileStatement;
import gameplayHook.MachinePackage.components.MachineContext;

import java.util.ArrayList;
import java.util.List;

public class CodeModule {
    public static final String C_RESET = "\u001B[0m";
    public static final String C_GREEN = "\u001B[32m";
    public static final String C_YELLOW = "\u001B[33m";
    public static final String C_BLUE = "\u001B[34m";
    public static final String C_PURPLE = "\u001B[35m";
    public static final String C_CYAN = "\u001B[36m";

    private final List<ConditionalStatement> statements = new ArrayList<>();

    public void checkStatements(MachineContext ctx) {
        for (ConditionalStatement statement : statements) {
            statement.run(ctx);
        }
    }

    public void createIfStatement(BooleanExpression booleanExpression, List<Statement> thenBlock) {
        IfStatement ifStatement = new IfStatement(booleanExpression, thenBlock, null);
        statements.add(ifStatement);
    }

    public void createIfElseStatement(BooleanExpression booleanExpression, List<Statement> thenBlock, List<Statement> elseBlock) {
        IfStatement ifStatement = new IfStatement(booleanExpression, thenBlock, elseBlock);
        statements.add(ifStatement);
    }

    public void createIfElifStatement(List<BooleanExpression> booleanExpressions, List<List<Statement>> thenBlocks, List<Statement> elseBlock) {
        if (booleanExpressions.size() != thenBlocks.size()) {
            throw new IllegalArgumentException("Each condition must have a corresponding then-block");
        }
        List<Statement> currentElseBlock = elseBlock;

        for (int i = booleanExpressions.size() - 1; i >= 0; i--) {
            BooleanExpression condition = booleanExpressions.get(i);
            List<Statement> thenBlock = thenBlocks.get(i);
            currentElseBlock = List.of(new IfStatement(condition, thenBlock, currentElseBlock));
        }

        statements.add((ConditionalStatement) currentElseBlock.getFirst());
    }

    public void createWhileStatement(BooleanExpression booleanExpression, List<Statement> actionStmt) {
        WhileStatement whileStatement = new WhileStatement(booleanExpression, actionStmt);
    }
}

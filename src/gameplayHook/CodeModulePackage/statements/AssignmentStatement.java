package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.Expression;
import gameplayHook.CodeModulePackage.components.Variable;

public class AssignmentStatement extends Statement {
    public Variable target;
    public Expression value;
}
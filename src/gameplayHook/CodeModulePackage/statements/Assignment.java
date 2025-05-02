package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.expressions.Expression;
import gameplayHook.CodeModulePackage.components.expressions.Variable;

public class Assignment {
    public Variable target;
    public Expression value;

    public Assignment(Variable target, Expression value) {
        this.target = target;
        this.value = value;
    }
}
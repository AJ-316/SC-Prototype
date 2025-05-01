package gameplayHook.CodeModulePackage.components;

public class Assignment {
    public Variable target;
    public Expression value;

    public Assignment(Variable target, Expression value) {
        this.target = target;
        this.value = value;
    }
}
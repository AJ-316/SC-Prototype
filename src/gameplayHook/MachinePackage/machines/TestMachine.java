package gameplayHook.MachinePackage.machines;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.statements.Action;
import gameplayHook.MachinePackage.Machine;

import java.util.Arrays;
import java.util.List;

public class TestMachine extends Machine {

    public TestMachine(String name) {
        super(name);

        getCtx().setVars(
                new Variable("var1", 1),
                new Variable("var2", "a"),
                new Variable("var3", 1.2),
                new Variable("var4", 3f),
                new Variable("var5", 3f),
                new Variable("var6", 3f),
                new Variable("var7", 3f),
                new Variable("var8", 3f),
                new Variable("var9", 3f),
                new Variable("var10", 3f)
        );

        getCtx().setActions(
                new Action("action1", List.of("var1", "var2"), (vars) -> System.out.println("Action 1: " + Arrays.toString(vars.toArray()))),
                new Action("action2", List.of("var2", "var4"), (vars) -> System.out.println("Action 2: " + Arrays.toString(vars.toArray()))),
                new Action("action3", List.of(),               (vars) -> System.out.println("Action 3: " + Arrays.toString(vars.toArray()))),
                new Action("action4", List.of("var3"),     (vars) -> System.out.println("Action 4: " + Arrays.toString(vars.toArray())))
        );
    }
}

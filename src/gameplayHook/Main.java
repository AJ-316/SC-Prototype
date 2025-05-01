package gameplayHook;

import gameplayHook.CodeModulePackage.Runner;
import gameplayHook.CodeModulePackage.components.*;
import gameplayHook.CodeModulePackage.machineComponents.MachineContext;
import gameplayHook.CodeModulePackage.statements.ActionStatement;
import gameplayHook.CodeModulePackage.statements.IfStatement;
import gameplayHook.CodeModulePackage.statements.WhileStatement;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        example2();
    }

    /**
     * While Statement with modification of variable
     * */
    private static void example2() {
        // WHILE drone.ammo < 20
        Variable droneAmmo = new Variable("drone.ammo", 15);
        Constant ammoThreshold = new Constant(20f);
        Condition droneAmmoCondition = new Condition(droneAmmo, new KnowledgeToken(OperatorType.LESS_THAN), ammoThreshold);

        // action
        Action action = new Action("drone.reload", List.of("drone.ammo"),
                (expressions) -> System.out.println(expressions.getFirst().value instanceof Number ammo ? "Reloading: " + (expressions.getFirst().value = ammo.floatValue() + 1) : expressions.getFirst().value));
        ActionStatement actionStmt = new ActionStatement(action);

        // WHILE
        WhileStatement whileStatement = new WhileStatement(droneAmmoCondition, List.of(actionStmt));

        // runtime context
        MachineContext context = new MachineContext();
        context.setAction(action);
        context.setVar(droneAmmo);

        Runner runner = new Runner();
        runner.run(whileStatement, context);
    }

    /**
     * Compound Condition with If Statement
     * */
    private static void example1() {
        // IF drone.ammo == 20
        Variable droneAmmo = new Variable("drone.ammo", 20);
        Constant ammoThreshold = new Constant(20f);
        Condition droneAmmoCondition = new Condition(droneAmmo, new KnowledgeToken(OperatorType.EQUALS), ammoThreshold);

        // IF drone.power > 50
        Variable dronePower = new Variable("drone.power", 52);
        Constant powerThreshold = new Constant(50f);
        Condition dronePowerCondition = new Condition(dronePower, new KnowledgeToken(OperatorType.GREATER_THAN), powerThreshold);

        CompoundCondition condition = new CompoundCondition(droneAmmoCondition, new KnowledgeToken(OperatorType.AND), dronePowerCondition);

        // action
        Action actionFlashlightOn = new Action("drone.flashlight.on", null, (_) -> System.out.println("Flashlight Turned ON"));
        Action actionFlashlightOff = new Action("drone.flashlight.off", null, (_) -> System.out.println("Flashlight Turned OFF"));
        // IF
        IfStatement ifStmt = new IfStatement(condition,
                List.of(new ActionStatement(actionFlashlightOff)),
                List.of(new ActionStatement(actionFlashlightOn)));

        // runtime context
        MachineContext context = new MachineContext();
        context.setAction(actionFlashlightOn);
        context.setAction(actionFlashlightOff);
        context.setVar(dronePower);
        context.setVar(droneAmmo);

        Runner runner = new Runner();
        runner.run(ifStmt, context);
    }
}

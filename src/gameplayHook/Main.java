package gameplayHook;

import gameplayHook.CodeModulePackage.Runner;
import gameplayHook.CodeModulePackage.components.*;
import gameplayHook.CodeModulePackage.machineComponents.MachineContext;
import gameplayHook.CodeModulePackage.statements.ActionStatement;
import gameplayHook.CodeModulePackage.statements.IfStatement;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        example1();
    }

    private static void example1() {
        // Create condition: IF drone.ammo == 20
        Variable droneAmmo = new Variable("drone.ammo");
        Constant ammoThreshold = new Constant(20f);
        Condition droneAmmoCondition = new Condition(droneAmmo, new KnowledgeToken(OperatorType.EQUALS), ammoThreshold);

        // Create condition: IF drone.power > 50
        Variable dronePower = new Variable("drone.power");
        Constant powerThreshold = new Constant(50f);
        Condition dronePowerCondition = new Condition(dronePower, new KnowledgeToken(OperatorType.GREATER_THAN), powerThreshold);

        CompoundCondition condition = new CompoundCondition(droneAmmoCondition, new KnowledgeToken(OperatorType.AND), dronePowerCondition);

        // Action: drone.flashlight.off
        Action action = new Action("drone.flashlight.off");
        ActionStatement actionStmt = new ActionStatement(action);

        // IF statement
        IfStatement ifStmt = new IfStatement(condition, List.of(actionStmt), List.of(new ActionStatement(new Action("drone.flashlight.on"))));

        // Set up the runtime context (mock)
        MachineContext context = new MachineContext();
        context.set("drone.power", 51f); // Set the drone power here
        context.set("drone.ammo", 20); // Set the drone power here

        // Run the statement
        Runner runner = new Runner();
        runner.run(ifStmt, context);
    }
}

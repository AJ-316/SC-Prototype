package gameplayHook;

import gameplayHook.CodeModulePackage.Runner;
import gameplayHook.CodeModulePackage.components.*;
import gameplayHook.CodeModulePackage.machineComponents.MachineContext;
import gameplayHook.CodeModulePackage.statements.ActionStatement;
import gameplayHook.CodeModulePackage.statements.AssignmentStatement;
import gameplayHook.CodeModulePackage.statements.IfStatement;
import gameplayHook.CodeModulePackage.statements.WhileStatement;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        example5();
    }

    /**
     * Unary Operator with If Statement
     * */
    private static void example5() {
        Variable stationShieldActive = new Variable("station.shieldActive", false);
        Action stationShieldOn = new Action("station.shieldOn", List.of("station.shieldActive"), (exprs) -> {
            exprs.getFirst().setValue(true);
            System.out.println("Turned Shield On");
        });
        Action stationShieldOff = new Action("station.shieldOff", List.of("station.shieldActive"), (exprs) -> {
            exprs.getFirst().setValue(false);
            System.out.println("Turned Shield Off");
        });

        UnaryCondition condition = new UnaryCondition(new UnaryExpression(stationShieldActive, new KnowledgeToken(OperatorType.NOT)));

        IfStatement ifStatement = new IfStatement(condition, List.of(new ActionStatement(stationShieldOn)), List.of(new ActionStatement(stationShieldOff)));

        MachineContext ctx = new MachineContext();
        ctx.setAction(stationShieldOn);
        ctx.setAction(stationShieldOff);
        ctx.setVar(stationShieldActive);

        Runner runner = new Runner();
        runner.run(ifStatement, ctx);
    }

    /**
     * Binary Expression with If Statement
     * */
    private static void example4() {
        enum TurretMode { SINGLE, BURST }

        /*  If turret has targets more or equal to 5 in view
            Then set the mode to Burst and calculate Burst Capacity = targetsInView / 2

            Single Mode:
                - Loads Single bullet; takes less time to load; takes time to switch targets
                - FireRate(--) Accuracy(++)
            Burst Mode:
                - Loads Batch of bullets; takes more time to load = target switch is instant between bursts
                - [Linear with BurstCapacity] FireRate(*BurstCapacity) Accuracy(*BurstCapacity)

            eg- BurstCapacity: 1 = FireRate(default) Accuracy(default)
                BurstCapacity: 2 = FireRate(+) Accuracy(-)
                BurstCapacity: 5 = FireRate(++) Accuracy(--)
        */

        // IF turret.targetsInView >= 5
        Variable turretTargetsInView = new Variable("turret.targetsInView", 6);
        Constant targetThreshold = new Constant(5);
        BinaryCondition condition = new BinaryCondition(turretTargetsInView, targetThreshold, new KnowledgeToken(OperatorType.GREATER_OR_EQUALS));

        Variable turretAmmo = new Variable("turret.ammo", 20);
        Variable turretMode = new Variable("turret.mode", TurretMode.SINGLE);
        Variable turretBurstCapacity = new Variable("turret.burstCapacity", 2);

        // turretMode = BURST
        Assignment assignmentModeBurst = new Assignment(turretMode, new Constant(TurretMode.BURST));

        // turretMode = SINGLE
        Assignment assignmentModeSingle = new Assignment(turretMode, new Constant(TurretMode.SINGLE));

        // turretMode = SINGLE
        Assignment assignmentBurstCapacity = new Assignment(turretBurstCapacity, new BinaryExpression(turretTargetsInView, new Constant(2), new KnowledgeToken(OperatorType.DIVIDE)));

        // IF
        IfStatement ifStmt = new IfStatement(condition,
                List.of(
                        new AssignmentStatement(assignmentBurstCapacity),
                        new AssignmentStatement(assignmentModeBurst)),
                List.of(
                        new AssignmentStatement(assignmentModeSingle)
                ));

        // runtime context
        MachineContext context = new MachineContext();
        context.setVar(turretAmmo);
        context.setVar(turretMode);
        context.setVar(turretBurstCapacity);
        context.setVar(turretTargetsInView);

        Runner runner = new Runner();
        runner.run(ifStmt, context);
    }

    /**
     * Variable Assignment with If Statement
     * */
    private static void example3() {
        enum DroneAmmoType { LIGHT, HEAVY }

        // IF drone.ammo <= 5
        Variable droneAmmo = new Variable("drone.ammo", 20);
        Constant ammoThreshold = new Constant(20f);
        BinaryCondition droneAmmoCondition = new BinaryCondition(droneAmmo, ammoThreshold, new KnowledgeToken(OperatorType.LESS_OR_EQUALS));

        Variable droneAmmoType = new Variable("drone.ammoType", DroneAmmoType.LIGHT);

        Assignment assignmentAmmoType = new Assignment(droneAmmoType, new Constant(DroneAmmoType.HEAVY));

        // IF
        IfStatement ifStmt = new IfStatement(droneAmmoCondition, List.of(new AssignmentStatement(assignmentAmmoType)), null);

        // runtime context
        MachineContext context = new MachineContext();
        context.setVar(droneAmmo);
        context.setVar(droneAmmoType);

        Runner runner = new Runner();
        runner.run(ifStmt, context);
    }

    /**
     * While Statement with modification of variable
     * */
    private static void example2() {
        // WHILE drone.ammo < 20
        Variable droneAmmo = new Variable("drone.ammo", 15);
        Constant ammoThreshold = new Constant(20f);
        BinaryCondition droneAmmoCondition = new BinaryCondition(droneAmmo, ammoThreshold, new KnowledgeToken(OperatorType.LESS_THAN));

        // action
        Action action = new Action("drone.reload", List.of("drone.ammo"),
                (expressions) -> {
                    if (expressions.getFirst().getValue() instanceof Number ammo) {
                        expressions.getFirst().setValue(ammo.floatValue() + 1);
                        System.out.println("Reloading: " + expressions.getFirst().getValue());
                    } else System.out.println(expressions.getFirst().getValue());
                });
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
     * Compound Condition with If Statement <br/>
     * 1) >= <br/>
     * 2) == <br/>
     * */
    private static void example1() {
        // IF drone.ammo == 20
        Variable droneAmmo = new Variable("drone.ammo", 20);
        Constant ammoThreshold = new Constant(20f);
        BinaryCondition droneAmmoCondition = new BinaryCondition(droneAmmo, ammoThreshold, new KnowledgeToken(OperatorType.EQUALS));

        // IF drone.power > 50
        Variable dronePower = new Variable("drone.power", 52);
        Constant powerThreshold = new Constant(50f);
        BinaryCondition dronePowerCondition = new BinaryCondition(dronePower, powerThreshold, new KnowledgeToken(OperatorType.GREATER_OR_EQUALS));

        CompoundCondition condition = new CompoundCondition(droneAmmoCondition, dronePowerCondition, new KnowledgeToken(OperatorType.AND));

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

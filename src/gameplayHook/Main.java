package gameplayHook;

import gameplayHook.CodeModulePackage.CodeModule;
import gameplayHook.CodeModulePackage.components.expressions.BinaryExpression;
import gameplayHook.CodeModulePackage.components.expressions.Constant;
import gameplayHook.CodeModulePackage.components.expressions.UnaryExpression;
import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.components.expressions.conditonal.BinaryCondition;
import gameplayHook.CodeModulePackage.components.expressions.conditonal.CompoundCondition;
import gameplayHook.CodeModulePackage.components.expressions.conditonal.UnaryCondition;
import gameplayHook.CodeModulePackage.components.tokens.KnowledgeToken;
import gameplayHook.CodeModulePackage.components.tokens.OperatorType;
import gameplayHook.CodeModulePackage.statements.Action;
import gameplayHook.CodeModulePackage.statements.ActionStatement;
import gameplayHook.CodeModulePackage.statements.Assignment;
import gameplayHook.CodeModulePackage.statements.AssignmentStatement;
import gameplayHook.MachinePackage.components.MachineContext;
import gameplayHook.MachinePackage.machines.Door;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        example6();
    }

    /**
     * Machine Model - Example: <br/>
     * <li>Door</li>
     * <li><b>IF</b> door is not open <br/>
     * <b>THEN</b> open the door</li>
     */
    private static void example6() {
        Door door = new Door();

        CodeModule codeModule = new CodeModule();
        codeModule.createIfStatement(new UnaryCondition(new UnaryExpression(door.getCtx().getVar(Door.KV_IS_OPEN), new KnowledgeToken(OperatorType.NOT))),
                List.of(ActionStatement.create(door.getCtx().getAction(Door.KA_OPEN))));

        door.attachCodeModule(codeModule);
        door.executeCodeModules();
    }

    /**
     * Unary Operator - Example: <br/>
     * <li>Station</li>
     * <li><b>IF</b> station's shield is not active <br/>
     * <b>THEN</b> activate shield <br/>
     * <b>ELSE</b> deactivate shield</li>
     */
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

        CodeModule codeModule = new CodeModule();
        // IF
        codeModule.createIfElseStatement(condition,
                List.of(ActionStatement.create(stationShieldOn)),
                List.of(ActionStatement.create(stationShieldOff)));

        MachineContext context = new MachineContext();
        context.setActions(stationShieldOn, stationShieldOff);
        context.setVars(stationShieldActive);

        codeModule.checkStatements(context);
        codeModule.checkStatements(context);
    }

    /**
     * Binary Expression - Example: <br/>
     * <li>Turret</li>
     * <li><b>IF</b> turret has >= 5 targets in view <br/>
     * <b>THEN</b> mode = burst and burstCapacity = (targets in view)/2 <br/>
     * <b>ELSE</b> mode = single </li>
     */
    private static void example4() {
        enum TurretMode {SINGLE, BURST}

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

        // burstCapacity = targetsInView / 2
        Assignment assignmentBurstCapacity = new Assignment(turretBurstCapacity, new BinaryExpression(turretTargetsInView, new Constant(2), new KnowledgeToken(OperatorType.DIVIDE)));

        CodeModule codeModule = new CodeModule();
        // IF
        codeModule.createIfElseStatement(condition,
                List.of(AssignmentStatement.create(assignmentBurstCapacity), AssignmentStatement.create(assignmentModeBurst)),
                List.of(AssignmentStatement.create(assignmentModeSingle)));

        // runtime context
        MachineContext context = new MachineContext();
        context.setVars(turretAmmo, turretMode, turretBurstCapacity, turretTargetsInView);
        codeModule.checkStatements(context);
    }

    /**
     * Variable Assignment - Example: <br/>
     * <li>Drone</li>
     * <li><b>IF</b> drone has ammo <= 20 <br/>
     * <b>THEN</b> ammoType = HEAVY </li>
     */
    private static void example3() {
        enum DroneAmmoType {LIGHT, HEAVY}

        // IF drone.ammo <= 5
        Variable droneAmmo = new Variable("drone.ammo", 20);
        Constant ammoThreshold = new Constant(20f);
        BinaryCondition droneAmmoCondition = new BinaryCondition(droneAmmo, ammoThreshold, new KnowledgeToken(OperatorType.LESS_OR_EQUALS));

        Variable droneAmmoType = new Variable("drone.ammoType", DroneAmmoType.LIGHT);

        Assignment assignmentAmmoType = new Assignment(droneAmmoType, new Constant(DroneAmmoType.HEAVY));

        CodeModule codeModule = new CodeModule();
        // IF
        codeModule.createIfStatement(droneAmmoCondition,
                List.of(AssignmentStatement.create(assignmentAmmoType)));

        // runtime context
        MachineContext context = new MachineContext();
        context.setVars(droneAmmo, droneAmmoType);
        codeModule.checkStatements(context);
    }

    /**
     * While Statement - Example: <br/>
     * <li>Drone</li>
     * <li><b>WHILE</b> drone has ammo < 20 <br/>
     * <b>DO</b> reload ammo (one at a time) </li>
     */
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

        CodeModule codeModule = new CodeModule();
        // WHILE
        codeModule.createWhileStatement(droneAmmoCondition, List.of(ActionStatement.create(action)));

        // runtime context
        MachineContext context = new MachineContext();
        context.setActions(action);
        context.setVars(droneAmmo);

        codeModule.checkStatements(context);
    }

    /**
     * Compound Condition - Example: <br/>
     * <li>Drone</li>
     * <li><b>IF</b> (drone has 20 ammo) <b>AND</b> (drone's power >= 50) <br/>
     * <b>THEN</b> turn off drone's flashlight <br/>
     * <b>ELSE</b> turn on drone's flashlight </li>
     */
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

        CodeModule codeModule = new CodeModule();
        // IF
        codeModule.createIfElseStatement(condition,
                List.of(ActionStatement.create(actionFlashlightOff)),
                List.of(ActionStatement.create(actionFlashlightOn)));

        // runtime context
        MachineContext context = new MachineContext();
        context.setActions(actionFlashlightOn, actionFlashlightOff);
        context.setVars(dronePower, droneAmmo);
        codeModule.checkStatements(context);
    }
}

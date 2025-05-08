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
import gameplayHook.MachinePackage.TriggerType;
import gameplayHook.MachinePackage.components.MachineContext;
import gameplayHook.MachinePackage.machines.Door;
import gameplayHook.MachinePackage.machines.TestMachine;
import gameplayHook.MachinePackage.machines.Turret;
import gameplayHook.SimUIPackage.RuntimeCodePackage.RuntimeCodeRunner;
import gameplayHook.SimUIPackage.Windows.SimulatorWindow;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        SimulatorWindow.init();
        example6();
        example7();
        TestMachine testMachine = new TestMachine("TestMachine");
    }

    /**
     * Runtime Code create/compile/execute - Example: <br/>
     * @see #example1()
     * */
    private static void example8() {
        Variable droneAmmo = new Variable("drone.ammo", 20);
        Constant ammoThreshold = new Constant(20f);
        BinaryCondition droneAmmoCondition = new BinaryCondition(droneAmmo, ammoThreshold, new KnowledgeToken(OperatorType.EQUALS));

        // IF drone.power > 50
        Variable dronePower = new Variable("drone.power", 52);
        Constant powerThreshold = new Constant(50f);
        BinaryCondition dronePowerCondition = new BinaryCondition(dronePower, powerThreshold, new KnowledgeToken(OperatorType.GREATER_OR_EQUALS));

        CompoundCondition condition = new CompoundCondition(droneAmmoCondition, dronePowerCondition, new KnowledgeToken(OperatorType.AND));

        // action
        RuntimeCodeRunner codeRunner = new RuntimeCodeRunner(Action.ActionMethod.class);
        codeRunner.createMethod("drone_flashlight_on", "System.out.println(\"Flashlight Turned ON\");");
        codeRunner.createMethod("drone_flashlight_off", "System.out.println(\"Flashlight Turned OFF\");");
        Map<String, Object> compiledActions;
        try {
            compiledActions = codeRunner.compileMethods();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(compiledActions == null) throw new RuntimeException("CompiledActions is Null");

        Action actionFlashlightOn = new Action("drone.flashlight.on", null, (Action.ActionMethod) compiledActions.get("drone_flashlight_on"));
        Action actionFlashlightOff = new Action("drone.flashlight.off", null, (Action.ActionMethod) compiledActions.get("drone_flashlight_off"));

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        // IF
        codeModule.createIfElseStatement(condition,
                List.of(ActionStatement.create(actionFlashlightOff)),
                List.of(ActionStatement.create(actionFlashlightOn)));

        // runtime context
        MachineContext context = new MachineContext("drone");
        context.setActions(actionFlashlightOn, actionFlashlightOff);
        context.setVars(dronePower, droneAmmo);
        codeModule.runIfTriggered(context, TriggerType.MANUAL);
    }

    /**
     * Machine Model - Example: <br/>
     * @see #example4()
     * */
    private static void example7() {
        Turret turret = new Turret();

        Variable targetsInView = turret.getCtx().getVar(Turret.KV_TARGETS_IN_VIEW);
        Variable turretMode = turret.getCtx().getVar(Turret.KV_MODE);

        // targets >= 5
        BinaryCondition shouldBurst = new BinaryCondition(targetsInView, new Constant(5), new KnowledgeToken(OperatorType.GREATER_OR_EQUALS));

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        // IF ELSE
        codeModule.createIfElseStatement(shouldBurst,
                List.of(
                        // Assign burstCapacity = targets / 2
                        AssignmentStatement.create(new Assignment(turret.getCtx().getVar(Turret.KV_BURST_CAPACITY),
                                new BinaryExpression(targetsInView, new Constant(2), new KnowledgeToken(OperatorType.DIVIDE)))),
                        // Set Mode to BURST
                        AssignmentStatement.create(new Assignment(turretMode, new Constant(Turret.TurretMode.BURST)))),
                List.of(
                        // Set Mode to SINGLE
                        AssignmentStatement.create(new Assignment(turretMode, new Constant(Turret.TurretMode.SINGLE))))
        );

        turret.attachCodeModule(codeModule);
        turret.executeCodeModules(TriggerType.MANUAL);
    }

    /**
     * Machine Model - Example: <br/>
     * <li>Door</li>
     * <code><b>IF</b> door is not open <br/>
     * <b>THEN</b> open the door</code>
     */
    public static void example6() {
        Door door = new Door();

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        codeModule.createIfStatement(new UnaryCondition(new UnaryExpression(door.getCtx().getVar(Door.KV_IS_OPEN), new KnowledgeToken(OperatorType.NOT))),
                List.of(ActionStatement.create(door.getCtx().getAction(Door.KA_OPEN))));

        door.attachCodeModule(codeModule);
        door.executeCodeModules(TriggerType.MANUAL);
    }

    /**
     * Unary Operator - Example: <br/>
     * <li>Station</li>
     * <code><b>IF</b> station's shield is not active <br/>
     * <b>THEN</b> activate shield <br/>
     * <b>ELSE</b> deactivate shield</code>
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

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        // IF
        codeModule.createIfElseStatement(condition,
                List.of(ActionStatement.create(stationShieldOn)),
                List.of(ActionStatement.create(stationShieldOff)));

        MachineContext context = new MachineContext("station");
        context.setActions(stationShieldOn, stationShieldOff);
        context.setVars(stationShieldActive);

        codeModule.runIfTriggered(context, TriggerType.MANUAL);
        codeModule.runIfTriggered(context, TriggerType.MANUAL);
    }

    /**
     * Binary Expression - Example: <br/>
     * <li>Turret</li>
     * <code><b>IF</b> turret has >= 5 targets in view <br/>
     * <b>THEN</b> mode = burst and burstCapacity = (targets in view)/2 <br/>
     * <b>ELSE</b> mode = single </code>
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

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        // IF
        codeModule.createIfElseStatement(condition,
                List.of(AssignmentStatement.create(assignmentBurstCapacity), AssignmentStatement.create(assignmentModeBurst)),
                List.of(AssignmentStatement.create(assignmentModeSingle)));

        // runtime context
        MachineContext context = new MachineContext("turret");
        context.setVars(turretAmmo, turretMode, turretBurstCapacity, turretTargetsInView);
        codeModule.runIfTriggered(context, TriggerType.MANUAL);
    }

    /**
     * Variable Assignment - Example: <br/>
     * <li>Drone</li>
     * <code><b>IF</b> drone has ammo <= 20 <br/>
     * <b>THEN</b> ammoType = HEAVY </code>
     */
    private static void example3() {
        enum DroneAmmoType {LIGHT, HEAVY}

        // IF drone.ammo <= 5
        Variable droneAmmo = new Variable("drone.ammo", 20);
        Constant ammoThreshold = new Constant(20f);
        BinaryCondition droneAmmoCondition = new BinaryCondition(droneAmmo, ammoThreshold, new KnowledgeToken(OperatorType.LESS_OR_EQUALS));

        Variable droneAmmoType = new Variable("drone.ammoType", DroneAmmoType.LIGHT);

        Assignment assignmentAmmoType = new Assignment(droneAmmoType, new Constant(DroneAmmoType.HEAVY));

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        // IF
        codeModule.createIfStatement(droneAmmoCondition,
                List.of(AssignmentStatement.create(assignmentAmmoType)));

        // runtime context
        MachineContext context = new MachineContext("drone");
        context.setVars(droneAmmo, droneAmmoType);
        codeModule.runIfTriggered(context, TriggerType.MANUAL);
    }

    /**
     * While Statement - Example: <br/>
     * <li>Drone</li>
     * <code><b>WHILE</b> drone has ammo < 20 <br/>
     * <b>DO</b> reload ammo (one at a time) </code>
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

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        // WHILE
        codeModule.createWhileStatement(droneAmmoCondition, List.of(ActionStatement.create(action)));

        // runtime context
        MachineContext context = new MachineContext("drone");
        context.setActions(action);
        context.setVars(droneAmmo);

        codeModule.runIfTriggered(context, TriggerType.MANUAL);
    }

    /**
     * Compound Condition - Example: <br/>
     * <li>Drone</li>
     * <code><b>IF</b> (drone has 20 ammo) <b>AND</b> (drone's power >= 50) <br/>
     * <b>THEN</b> turn off drone's flashlight <br/>
     * <b>ELSE</b> turn on drone's flashlight </code>
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

        CodeModule codeModule = new CodeModule(TriggerType.MANUAL);
        // IF
        codeModule.createIfElseStatement(condition,
                List.of(ActionStatement.create(actionFlashlightOff)),
                List.of(ActionStatement.create(actionFlashlightOn)));

        // runtime context
        MachineContext context = new MachineContext("drone");
        context.setActions(actionFlashlightOn, actionFlashlightOff);
        context.setVars(dronePower, droneAmmo);
        codeModule.runIfTriggered(context, TriggerType.MANUAL);
    }
}

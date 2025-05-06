package gameplayHook.MachinePackage.machines;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.MachinePackage.Machine;

public class Turret extends Machine {
    public static final String KV_TARGETS_IN_VIEW = "turret.targetsInView";
    public static final String KV_MODE = "turret.mode";
    public static final String KV_BURST_CAPACITY = "turret.burstCapacity";
    
    public enum TurretMode { SINGLE, BURST }

    public Turret() {
        super("turret");

        getCtx().setVars(
            new Variable(KV_TARGETS_IN_VIEW, 10),
            new Variable(KV_MODE, TurretMode.SINGLE),
            new Variable(KV_BURST_CAPACITY, 1)
        );
    }
}

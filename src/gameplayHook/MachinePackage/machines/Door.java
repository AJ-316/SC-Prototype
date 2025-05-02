package gameplayHook.MachinePackage.machines;

import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.statements.Action;
import gameplayHook.MachinePackage.Machine;

import java.util.List;

public class Door extends Machine {

    public static final String KV_IS_OPEN = "door.isOpen";
    public static final String KA_OPEN = "door.open";
    public static final String KA_CLOSE = "door.close";

    public Door() {
        super();

        Variable isOpen = new Variable(KV_IS_OPEN, false);

        Action open = new Action(KA_OPEN, List.of(KV_IS_OPEN), vars -> {
            Variable varIsOpen = vars.getFirst();

            if ((boolean) varIsOpen.getValue()) {
                System.out.println("Door is Already Open");
                return;
            }

            varIsOpen.setValue(true);
            System.out.println("Door is Opened");
        });

        Action close = new Action(KA_CLOSE, List.of(KV_IS_OPEN), vars -> {
            Variable varIsOpen = vars.getFirst();

            if ((boolean) varIsOpen.getValue()) {
                System.out.println("Door is Already Closed");
                return;
            }

            varIsOpen.setValue(false);
            System.out.println("Door is Closed");
        });

        getCtx().setVars(isOpen);
        getCtx().setActions(open, close);
    }
}

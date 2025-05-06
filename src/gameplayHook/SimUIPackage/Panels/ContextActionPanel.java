package gameplayHook.SimUIPackage.Panels;

import gameplayHook.MachinePackage.components.MachineContext;

import java.util.Arrays;

public class ContextActionPanel extends ContextPanel {

    public ContextActionPanel() {
        super("Machine Actions", "No Actions");
    }

    @Override
    protected String[] getContext(MachineContext machineContext) {
        if(machineContext.getActions() == null) return null;

        String[] context = new String[machineContext.getActions().length];
        for (int i = 0; i < context.length; i++) {
            context[i] = machineContext.getActions()[i].getName() + ": " + Arrays.toString(machineContext.getActions()[i].getArgs());
        }

        return context;
    }
}

package gameplayHook.SimUIPackage.Panels;

import gameplayHook.MachinePackage.components.MachineContext;

public class ContextVariablePanel extends ContextPanel {

    public ContextVariablePanel() {
        super("Machine Variables", "No Variables");
    }

    @Override
    protected String[] getContext(MachineContext machineContext) {
        if(machineContext.getVariables() == null) return null;

        String[] context = new String[machineContext.getVariables().length];
        for (int i = 0; i < context.length; i++) {
            context[i] = machineContext.getVariables()[i].getName() + ": = " + machineContext.getVariables()[i].getValue();
        }

        return context;
    }
}

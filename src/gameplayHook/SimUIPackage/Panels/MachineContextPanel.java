package gameplayHook.SimUIPackage.Panels;

import javax.swing.*;

public class MachineContextPanel extends JSplitPane {

    private ContextActionPanel actionPanel;
    private ContextVariablePanel variablePanel;

    public MachineContextPanel() {
        actionPanel = new ContextActionPanel();
        variablePanel = new ContextVariablePanel();

        setRightComponent(actionPanel);
        setLeftComponent(variablePanel);
        setDividerLocation(300);
    }
}

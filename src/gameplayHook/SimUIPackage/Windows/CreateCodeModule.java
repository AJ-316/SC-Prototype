package gameplayHook.SimUIPackage.Windows;

import gameplayHook.MachinePackage.TriggerType;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import javax.swing.*;

public class CreateCodeModule extends DialogWindow {

    private static final JComboBox<TriggerType> triggerTypeComboBox = new JComboBox<>(TriggerType.values());

    public CreateCodeModule() {
        super("Create Code Module");
    }

    @Override
    protected void init() {
        addRow(createField("CodeModuleType", triggerTypeComboBox));

        JPanel panel = new JPanel();
        panel.add(getSubmit());
        panel.add(getCancel());
        addRow(panel);
    }

    @Override
    protected boolean onSubmit() {
        SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_ATTACH_CM, triggerTypeComboBox.getSelectedItem());
        return true;
    }
}

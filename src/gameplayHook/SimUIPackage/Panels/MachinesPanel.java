package gameplayHook.SimUIPackage.Panels;

import com.formdev.flatlaf.ui.FlatLineBorder;
import gameplayHook.MachinePackage.components.MachineContext;
import gameplayHook.SimUIPackage.Components.MachineSelectButton;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import javax.swing.*;
import java.awt.*;

public class MachinesPanel extends CustomPanel {

    private static int MACHINE_COUNTER;
    private static final JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 100, 10));;

    public MachinesPanel() {
        super("Machines", new Color(100, 200, 200));

        buttonsPanel.setOpaque(false);

        JScrollPane scrollPane = createScrollPane(buttonsPanel, null);

        getContainer().add(scrollPane);
        getContainer().setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        SimEventsHandler.subscribeEvent(SimEventsHandler.EVENT_ON_ADD_MACHINE, (objects) ->
                addButton(objects.length > 0 ? (MachineContext) objects[0] : null));
    }

    public static void addButton(MachineContext context) {
        if(context == null) return;

        JPanel buttonRowPanel = new JPanel(new BorderLayout());
        buttonRowPanel.setBorder(new FlatLineBorder(new Insets(4, 4, 4, 4), new Color(1, 1, 1, 0.1f), 1, 16));

        JLabel machineNumber = new JLabel(String.valueOf(++MACHINE_COUNTER));
        machineNumber.setHorizontalAlignment(JLabel.CENTER);
        machineNumber.setPreferredSize(new Dimension(32, machineNumber.getPreferredSize().height));

        buttonRowPanel.add(new MachineSelectButton(context), BorderLayout.CENTER);
        buttonRowPanel.add(machineNumber, BorderLayout.WEST);

        buttonsPanel.add(buttonRowPanel);
        buttonsPanel.revalidate();
    }

}

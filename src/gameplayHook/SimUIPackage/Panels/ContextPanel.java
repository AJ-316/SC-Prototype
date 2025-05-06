package gameplayHook.SimUIPackage.Panels;

import gameplayHook.MachinePackage.components.MachineContext;
import gameplayHook.SimUIPackage.GradientBorder;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public abstract class ContextPanel extends CustomPanel {

    private static String selectedContext = "";
    protected final JLabel noContextLabel = new JLabel();
    protected JPanel actionContainer;

    private static final GradientBorder contextBorder = new GradientBorder(
            new Insets(8, 8, 8, 8),
            new Color(55, 196, 114, 125),
            new Color(55, 196, 182, 125),
            new Color(21, 131, 209, 125), 2, 16
    );

    public ContextPanel(String name, String emptyContextLabel) {
        super(name, new Color(50, 50, 0));
        noContextLabel.setText(emptyContextLabel);
        noContextLabel.setFont(BOLD_CONSOLAS);
        noContextLabel.setForeground(new Color(125, 125, 125));
        noContextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noContextLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        actionContainer = new JPanel();
        actionContainer.setOpaque(false);
        actionContainer.setLayout(new BoxLayout(actionContainer, BoxLayout.Y_AXIS));
        actionContainer.add(noContextLabel);

        JScrollPane scrollPane = new JScrollPane(actionContainer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setOpaque(false);

        getContainer().add(scrollPane, BorderLayout.CENTER);

        SimEventsHandler.subscribeEvent(SimEventsHandler.EVENT_ON_SELECT_MACHINE, (objects) -> {
            if(objects.length < 1) return;
            updateContext((MachineContext) objects[0]);
        });

        SimEventsHandler.subscribeEvent(SimEventsHandler.EVENT_ON_UPDATE_CONTEXT, (objects) -> {
            if(objects.length < 1 || !isSelectedContext((MachineContext) objects[0])) return;
            updateContext((MachineContext) objects[0]);
        });
    }

    protected abstract String[] getContext(MachineContext machineContext);

    protected void updateContext(MachineContext machineContext) {
        actionContainer.removeAll();
        String[] context = getContext(machineContext);
        System.out.println(Arrays.toString(context));

        if (context == null || context.length == 0) {
            actionContainer.add(noContextLabel); // ✅ show fallback inside action container
        } else {
            for (String contextData : context) {
                String[] kv = contextData.split(":");
                JPanel contextContainer = new JPanel(new FlowLayout());
                contextContainer.setOpaque(false);
                contextContainer.setAlignmentX(LEFT_ALIGNMENT);

                contextContainer.add(getLabel(kv[0], true));
                if(kv.length > 1)
                    contextContainer.add(getLabel(kv[1], false));

                contextContainer.setMaximumSize(contextContainer.getPreferredSize());
                actionContainer.add(contextContainer);
                actionContainer.add(Box.createVerticalStrut(5));
            }
        }

        repaint();
        revalidate();
    }

    protected JLabel getLabel(String name, boolean isBordered) {
        JLabel label = new JLabel(name);
        if(isBordered)
            label.setBorder(contextBorder);

        label.setFont(PLAIN_CONSOLAS);
        return label;
    }

    private void updateSelectedContext(MachineContext context) {
        selectedContext = context.getMachineName();
    }

    private boolean isSelectedContext(MachineContext context) {
        return selectedContext.equals(context.getMachineName());
    }
}

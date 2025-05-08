package gameplayHook.SimUIPackage.Windows;

import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DialogWindow extends JDialog {

    public static void init(JFrame owner) {
        DialogWindow.owner = owner;

        SimEventsHandler.subscribeEvent(SimEventsHandler.EVENT_ON_DW_CREATE_MACHINE, _ -> new CreateMachineDialog());
    }

    private static JFrame owner;
    private final JButton submit;
    private final JButton cancel;
    protected final GridBagConstraints constraints;
    protected final Map<String, JTextComponent> textFields;

    public DialogWindow(String title) {
        super(owner, true);
        textFields = new HashMap<>();

        setTitle(title);
        constraints = new GridBagConstraints();
        setLayout(new GridBagLayout());
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        this.submit = new JButton("Submit");
        this.cancel = new JButton("Cancel");

        setOnCancel(_ -> { dispose(); textFields.clear(); });

        init();

        removeCloseButton(this);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    protected void addRow(Component... components) {
        if(constraints.gridy < 0)
            constraints.gridy = 0;

        constraints.gridy++;
        constraints.gridx = -1;

        for (Component component : components) {
            constraints.gridx++;
            add(component, constraints);
        }
    }

    protected abstract void init();

    protected void setOnSubmit(ActionListener actionListener) {
        submit.addActionListener(actionListener);
    }

    protected void setOnCancel(ActionListener actionListener) {
        cancel.addActionListener(actionListener);
    }

    protected JButton getSubmit() {
        return submit;
    }

    protected JButton getCancel() {
        return cancel;
    }

    protected JTextComponent addTextComponent(String name, JTextComponent component) {
        textFields.put(name, component);
        return component;
    }

    protected String getText(String name) {
        JTextComponent component = textFields.get(name);
        if(component == null) return "NO_TEXT_COMPONENT_FOUND";
        return component.getText();
    }

    private static java.util.List<Component> getAllComponents(Container c) {
        Component[] comps = c.getComponents();
        List<Component> list = new ArrayList<>();
        for (Component comp : comps) {
            list.add(comp);
            if (comp instanceof Container cont) {
                list.addAll(getAllComponents(cont));
            }
        }
        return list;
    }

    private static void removeCloseButton(DialogWindow dw) {
        SwingUtilities.invokeLater(() -> {
            for (Component comp : getAllComponents(dw)) {
                if (comp instanceof JButton button) {
                    String accessibleName = button.getAccessibleContext().getAccessibleName();
                    if (accessibleName != null && accessibleName.toLowerCase().contains("close")) {
                        button.setVisible(false); // Or button.setEnabled(false)
                        System.out.println("Removed a button: " + accessibleName);
                    }
                }

                if (comp instanceof JLabel label) {
                    if (label.getText() != null && label.getText().equals(dw.getTitle())) {
                        // Optionally, tweak font or padding
                        label.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0)); // top, left, bottom, right
                        label.revalidate();
                        label.repaint();
                        System.out.println("Adjusted title label padding");
                    }
                }
            }
        });
    }
}

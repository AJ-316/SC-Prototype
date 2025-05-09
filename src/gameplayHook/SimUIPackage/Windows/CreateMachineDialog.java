package gameplayHook.SimUIPackage.Windows;

import com.formdev.flatlaf.ui.FlatLineBorder;
import gameplayHook.CodeModulePackage.components.expressions.Variable;
import gameplayHook.CodeModulePackage.statements.Action;
import gameplayHook.MachinePackage.Machine;
import gameplayHook.MachinePackage.machines.TemplateMachine;
import gameplayHook.SimUIPackage.Panels.CustomPanel;
import gameplayHook.SimUIPackage.RuntimeCodePackage.RuntimeCodeRunner;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CreateMachineDialog extends DialogWindow {

    private int ACTION_COUNTER;
    private int VARIABLE_COUNTER;
    private JPanel actionsPanel;
    private JPanel variablesPanel;

    private RuntimeCodeRunner codeRunner;

    public CreateMachineDialog() {
        super("Create Machine");
    }

    private JScrollPane initActionsPanel() {
        actionsPanel = new JPanel();
        JScrollPane pane = CustomPanel.createScrollPane(actionsPanel, null);
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        pane.setMaximumSize(new Dimension(320, 250));
        pane.setPreferredSize(new Dimension(320, 250));
        return pane;
    }

    private JScrollPane initVariablesPanel() {
        variablesPanel = new JPanel();
        JScrollPane pane = CustomPanel.createScrollPane(variablesPanel, null);
        variablesPanel.setLayout(new BoxLayout(variablesPanel, BoxLayout.Y_AXIS));
        pane.setMaximumSize(new Dimension(320, 240));
        pane.setPreferredSize(new Dimension(320, 240));
        pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        return pane;
    }

    @Override
    protected void init() {
        constraints.insets = new Insets(5, 5, 5, 5);

        JTextField machineNameField = (JTextField) addTextComponent("machineName", CustomPanel.getTextField());
        addRow(createField("Machine Name", machineNameField));

        JSplitPane splitPane = new JSplitPane();
        splitPane.setRightComponent(initActionsPanel());
        splitPane.setLeftComponent(initVariablesPanel());
        splitPane.setDividerSize(10);
        splitPane.setDividerLocation(210);
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> { if ((int) e.getNewValue() > 210) splitPane.setDividerLocation(210); });

        addRow(splitPane);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton clearAllBtn = new JButton("Clear All");
        clearAllBtn.addActionListener(_ -> clearAll());
        JButton addActionBtn = new JButton("Add Action");
        addActionBtn.addActionListener(_ -> createAction());
        JButton addVariableBtn = new JButton("Add Variable");
        addVariableBtn.addActionListener(_ -> createVariable());
        buttonsPanel.add(clearAllBtn);
        buttonsPanel.add(addActionBtn);
        buttonsPanel.add(addVariableBtn);
        buttonsPanel.add(getSubmit());
        buttonsPanel.add(getCancel());

        addRow(buttonsPanel);

        createAction();
        createVariable();

        codeRunner = new RuntimeCodeRunner(Action.ActionMethod.class);
    }

    protected boolean onSubmit() {
        String name = getText("machineName");
        if(name.isBlank()) return false;

        TemplateMachine machine = new TemplateMachine(name);

        List<Variable> variables = new ArrayList<>();
        boolean atLeastOneMethod = false;
        for (String textFieldName : textFields.keySet()) {
            if(textFieldName.startsWith("actionName")) {
                if(!getText(textFieldName).isBlank()) {
                    String code = getText("actionCode" + textFieldName.replaceAll("[^0-9]", ""));
                    if (!code.isBlank()) {
                        codeRunner.createMethod(getText(textFieldName).trim(), code);
                        atLeastOneMethod = true;
                    }
                }
            }

            if(textFieldName.startsWith("variableName")) {
                if(!getText(textFieldName).isBlank()) {
                    String value = getText("variableValue" + textFieldName.replaceAll("[^0-9]", ""));
                    if (!value.isBlank())
                        variables.add(new Variable(getText(textFieldName).trim(), parseValue(value)));
                }
            }
        }

        Map<String, Object> actionMethods = null;
        if(atLeastOneMethod) {
            try {
                actionMethods = codeRunner.compileMethods();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        machine.setVars(variables.toArray(new Variable[0]));
        System.out.println(Arrays.toString(machine.getCtx().getVariables()));

        if(actionMethods == null) return false;

        if (!actionMethods.isEmpty()) {
            Action[] actions = new Action[actionMethods.size()];

            int i = 0;
            for (String methodName : actionMethods.keySet()) {
                String[] methodArgs = methodName.split("\\$");
                List<String> args = methodArgs[1].trim().equals("null") ? null : List.of(methodArgs[1].split("_"));
                actions[i++] = new Action(methodArgs[0], args, (Action.ActionMethod) actionMethods.get(methodName));
            }

            machine.setActions(actions);

            for (Action action : machine.getCtx().getActions()) {
                System.out.println(action.getName());
                action.run(machine.getCtx());
            }
        }

        SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_ADD_MACHINE, machine.getCtx());
        return true;
    }

    private Object parseValue(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String trimmed = input.trim().toLowerCase();

        if (trimmed.equals("true") || trimmed.equals("false")) {
            return Boolean.parseBoolean(trimmed);
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e1) {
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException e2) {
                return input;
            }
        }
    }

    private boolean removeComponent(int index, JPanel componentPanel, String componentName, String componentField) {
        Component panelComp = componentPanel.getComponent(index - 1);
        Component strutComp = componentPanel.getComponent(index);

        if (panelComp instanceof CustomPanel panel) {
            String key = panel.getTitle().getText().replace(componentName, "").trim();
            JTextComponent textComponent = textFields.get(componentName.toLowerCase() + "Name" + key);

            if (textComponent != null && textComponent.getText().isBlank()) {
                componentPanel.remove(strutComp);
                componentPanel.remove(panelComp);

                textFields.remove(componentName.toLowerCase() + "Name" + key);
                textFields.remove(componentField + key);

                return true;
            }
        }

        return false;
    }

    private void clearAll() {
        for (int i = actionsPanel.getComponents().length - 1; i >= 1; i--) {
            if(removeComponent(i, actionsPanel, "Action", "actionCode")) i -= 1;
        }

        for (int i = variablesPanel.getComponents().length - 1; i >= 1; i--) {
            if(removeComponent(i, variablesPanel, "Variable", "variableValue")) i -= 1;
        }

        ACTION_COUNTER = 0;
        for (Component c : actionsPanel.getComponents()) {
            if (c instanceof CustomPanel) ACTION_COUNTER++;
        }

        VARIABLE_COUNTER = 0;
        for (Component c : variablesPanel.getComponents()) {
            if (c instanceof CustomPanel) VARIABLE_COUNTER++;
        }

        revalidate();
        repaint();
    }

    private JButton getRemovePanelButton(ActionListener removeComponent) {
        JButton button = new JButton("X");
        System.out.println(button.getPreferredSize().height);
        button.setPreferredSize(new Dimension(35, 35));
        button.setMaximumSize(  new Dimension(35, 35));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 40));
        panel.add(button);

        button.addActionListener(removeComponent);

        return button;
    }

    private void removePanelWithStrut(JPanel container, CustomPanel panelToRemove, String componentName, String componentField) {
        int index = container.getComponentZOrder(panelToRemove);
        container.remove(panelToRemove);

        if (index < container.getComponentCount()) {
            Component next = container.getComponent(index);
            if (next instanceof Box.Filler) {
                container.remove(next);
            }
        }

        String number = panelToRemove.getTitle().getText().replaceAll("[^0-9]", "");
        textFields.remove(componentName + "Name" + number);
        textFields.remove(componentField + number);

        ACTION_COUNTER = 0;
        VARIABLE_COUNTER = 0;

        for (String textComponentName : textFields.keySet()) {
            if (textComponentName.matches("actionName[0-9]+")) ACTION_COUNTER++;
            if (textComponentName.matches("variableName[0-9]+")) VARIABLE_COUNTER++;
        }

        revalidate();
        repaint();
    }

    private void createAction() {
        CustomPanel actionPanel = new CustomPanel("Action " + ++ACTION_COUNTER, Color.red);
        actionPanel.getTitleBar().add(getRemovePanelButton(_ ->
                removePanelWithStrut(actionsPanel, actionPanel, "action", "actionCode")), BorderLayout.EAST);
        actionPanel.getContainer().setLayout(new BoxLayout(actionPanel.getContainer(), BoxLayout.Y_AXIS));
        actionPanel.setBackground(new Color(0, 0, 0, 0.2f));

        JTextPane textPane = (JTextPane) addTextComponent("actionCode" + ACTION_COUNTER, new JTextPane());
        textPane.setFont(CustomPanel.PLAIN_CONSOLAS_REG);
        textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,10));
        JScrollPane scrollPane = CustomPanel.createScrollPane(textPane, new FlatLineBorder(new Insets(2, 2, 2, 2), new Color(1, 1, 1, 0.3f), 1, 12));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 50));

        actionPanel.getContainer().add(createField("Name", addTextComponent("actionName" + ACTION_COUNTER, CustomPanel.getTextField())));
        actionPanel.getContainer().add(createField("Code", scrollPane));

        actionsPanel.add(actionPanel);
        actionsPanel.add(Box.createVerticalStrut(5));

        revalidate();
    }

    private void createVariable() {
        CustomPanel variablePanel = new CustomPanel("Variable " + ++VARIABLE_COUNTER, Color.red);
        variablePanel.getTitleBar().add(getRemovePanelButton(_ ->
                removePanelWithStrut(variablesPanel, variablePanel, "variable", "variableValue")), BorderLayout.EAST);
        variablePanel.getContainer().setLayout(new BoxLayout(variablePanel.getContainer(), BoxLayout.Y_AXIS));
        variablePanel.setBackground(new Color(0, 0, 0, 0.2f));

        variablePanel.getContainer().add(createField("Name:",
                addTextComponent("variableName" + VARIABLE_COUNTER, CustomPanel.getTextField())));
        variablePanel.getContainer().add(createField("Value:",
                addTextComponent("variableValue" + VARIABLE_COUNTER, CustomPanel.getTextField())));
        variablePanel.getContainer().add(Box.createVerticalStrut(10));
        variablePanel.getContainer().setPreferredSize(variablePanel.getPreferredSize());
        variablePanel.getContainer().setMinimumSize(variablePanel.getPreferredSize());

        variablesPanel.add(variablePanel);
        variablesPanel.add(Box.createVerticalStrut(5));

        revalidate();
    }
}

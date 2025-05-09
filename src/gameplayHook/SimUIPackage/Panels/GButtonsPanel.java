package gameplayHook.SimUIPackage.Panels;

import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;

import javax.swing.*;
import java.awt.*;

public class GButtonsPanel extends CustomPanel {

    public GButtonsPanel() {
        super("Game Buttons", new Color(100, 100, 200));

        getContainer().setLayout(new BoxLayout(getContainer(), BoxLayout.Y_AXIS));
        createButton("Create Machine").addActionListener(_ -> SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_DW_CREATE_MACHINE));
        createButton("Attach Code Module").addActionListener(_ -> SimEventsHandler.triggerEvent(SimEventsHandler.EVENT_ON_CREATE_CM));
    }

    private JButton createButton(String name) {
        JButton button = new JButton(name);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMaximumSize().height));
        getContainer().add(button);
        getContainer().add(Box.createVerticalStrut(5));
        return button;
    }
}

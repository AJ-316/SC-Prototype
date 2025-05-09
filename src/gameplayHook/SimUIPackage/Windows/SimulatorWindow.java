package gameplayHook.SimUIPackage.Windows;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import gameplayHook.SimUIPackage.Panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SimulatorWindow {

    public static void init() {
        FlatOneDarkIJTheme.setup();
        SimulatorWindow simulatorWindow = new SimulatorWindow();
    }

    private final JFrame frame;
    private final JPanel container;
    private final JMenuBar menuBar;

    public SimulatorWindow() {
        frame = new JFrame("SC_Prototype");
        frame.getRootPane().putClientProperty("FlatLaf.useWindowDecorations", true);
        frame.setIconImage(getIcon("iconLow").getImage());
        frame.setJMenuBar(menuBar = new JMenuBar());

        container = new JPanel();
        container.setLayout(new GridLayout(1, 2));
        container.setPreferredSize(new Dimension(1280, 720));
        frame.add(container);

        container.add(createA());
        container.add(createB());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        DialogWindow.init(frame);
    }

    private JPanel createB() {
        JPanel partB = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        partB.setLayout(new GridBagLayout());

        TokensPanel tokensPanel = new TokensPanel();
        ConfigCodeModulePanel configCodeModule = new ConfigCodeModulePanel();
        MachineContextPanel contextPanel = new MachineContextPanel();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.25;
        constraints.insets.top = constraints.insets.right = 10;
        constraints.insets.bottom = constraints.insets.left = 5;
        partB.add(tokensPanel, constraints);

        constraints.gridy++;
        constraints.weighty = 0.45;
        constraints.insets.right = 10;
        constraints.insets.bottom = constraints.insets.top = constraints.insets.left = 5;
        partB.add(configCodeModule, constraints);

        constraints.gridy++;
        constraints.weighty = 0.3;
        constraints.insets.bottom = constraints.insets.right = 10;
        constraints.insets.top = constraints.insets.left = 5;
        partB.add(contextPanel, constraints);

        return partB;
    }

    private JPanel createA() {
        JPanel partA = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();

        partA.setLayout(new GridBagLayout());

        EventPanel eventPanel = new EventPanel();
        MachinesPanel machinesPanel = new MachinesPanel();
        GButtonsPanel buttonsPanel = new GButtonsPanel();

        constraints.gridx = constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridheight = 2;
        constraints.insets.right = 5;
        constraints.insets.top = constraints.insets.bottom = constraints.insets.left = 10;
        constraints.fill = GridBagConstraints.BOTH;
        partA.add(eventPanel, constraints);

        constraints.gridx++;
        constraints.gridheight = 1;
        constraints.insets.top = 10;
        constraints.insets.bottom = constraints.insets.left = constraints.insets.right = 5;
        partA.add(machinesPanel, constraints);

        constraints.gridy++;
        constraints.weighty = 0.8;
        constraints.insets.bottom = 10;
        constraints.insets.top = constraints.insets.left = constraints.insets.right = 5;
        partA.add(buttonsPanel, constraints);

        return partA;
    }

    public static ImageIcon getIcon(String icon) {
        return new ImageIcon(Objects.requireNonNull(SimulatorWindow.class.getResource("/" + icon + ".png")));
    }

    private void addMenu(JMenu menu, JMenuItem... menuItems) {
        for (JMenuItem menuItem : menuItems) {
            menu.add(menuItem);
        }
        menuBar.add(menu);
    }
}

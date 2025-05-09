package gameplayHook.SimUIPackage.Panels;

import com.formdev.flatlaf.ui.FlatLineBorder;
import gameplayHook.CodeModulePackage.CodeModule;
import gameplayHook.MachinePackage.TriggerType;
import gameplayHook.SimUIPackage.SimEventPackage.SimEventsHandler;
import gameplayHook.SimUIPackage.Windows.SimulatorWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigCodeModulePanel extends CustomPanel {

    private List<CodeModuleContainer> codeModuleContainerList;
    private final JTabbedPane tabbedPane;

    public ConfigCodeModulePanel() {
        super("Edit Code Module", new Color(200, 100, 200));

        codeModuleContainerList = new ArrayList<>();
        tabbedPane = new JTabbedPane();
        getContainer().add(tabbedPane);
        tabbedPane.putClientProperty("JTabbedPane.tabAreaAlignment", "center");
        tabbedPane.putClientProperty("JTabbedPane.tabAlignment", "leading");
        tabbedPane.putClientProperty("JTabbedPane.scrollButtonsPolicy", "never");
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.setOpaque(false);
        tabbedPane.setBorder(new FlatLineBorder(new Insets(2, 2, 2, 2), new Color(1, 1, 1, 0.1f), 1, 12));

        for (TriggerType triggerType : TriggerType.values()) {
            createCodeModule(triggerType);
        }

        for (TriggerType triggerType : TriggerType.values()) {
            createCodeModule(triggerType);
        }

        SimEventsHandler.subscribeEvent(SimEventsHandler.EVENT_ON_ATTACH_CM, objects -> createCodeModule((TriggerType) objects[0]));
    }

    private void createCodeModule(TriggerType triggerType) {
        CodeModule codeModule = new CodeModule(triggerType);

        CodeModuleContainer codeModuleContainer = new CodeModuleContainer(codeModule);
        codeModuleContainerList.add(codeModuleContainer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(codeModuleContainer);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab(triggerType.name(), SimulatorWindow.getIcon(getIconName(triggerType)), panel);
    }

    private static String getIconName(TriggerType triggerType) {
        return switch (triggerType) {
            case TICK -> "chipGreen";
            case PROXIMITY -> "chipPurple";
            case SIGNAL -> "chipYellow";
            case INTERACTION -> "chipRed";
            case MANUAL -> "chipBlue";
            default -> "chipA";
        };
    }

    private static class CodeModuleContainer extends CustomPanel {

        private final CodeModule codeModule;
        private final JTextPane textPane;

        public CodeModuleContainer(CodeModule codeModule) {
            super(codeModule.getTriggerType().name(), Color.yellow);
            setBackground(null);

            this.codeModule = codeModule;
            textPane = new JTextPane();
            textPane.setFont(PLAIN_CONSOLAS_REG);
            JScrollPane scrollPane = createScrollPane(textPane, null);

            getContainer().setLayout(new BorderLayout());
            getContainer().add(scrollPane, BorderLayout.CENTER); // todo instead of textPane here will be interactive panel to drag and drop actions,tokens,variables
        }
    }


}

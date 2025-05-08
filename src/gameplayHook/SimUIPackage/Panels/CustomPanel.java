package gameplayHook.SimUIPackage.Panels;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CustomPanel extends JPanel {

    public static final Font PLAIN_CONSOLAS_REG = new Font("Consolas", Font.PLAIN, 14);
    protected static final Font BOLD_CONSOLAS_REG = new Font("Consolas", Font.BOLD, 14);
    protected static final Font PLAIN_CONSOLAS_BIG = new Font("Consolas", Font.BOLD, 16);
    private final JPanel container;
    private final JLabel titleLabel;
    private final JPanel titleBar;

    public CustomPanel(String title, Color bg) {
        setBackground(new Color(1, 1, 1, 0.01f));
        setBorder(new FlatLineBorder(new Insets(0, 0, 0, 0), new Color(1, 1, 1, 0.1f), 1, 32));
        /*setBorder(new GradientBorder(
                new Insets(2, 2, 2, 2),
                new Color(55, 196, 114, 75),
                new Color(55, 196, 182, 75),
                new Color(21, 131, 209, 75), 2, 32
        ));*/

        super.setLayout(new BorderLayout());
        container = new JPanel(new BorderLayout());
        container.setBackground(bg);
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleBar.add(titleLabel, BorderLayout.CENTER);
        super.add(titleBar, BorderLayout.NORTH);
        super.add(container, BorderLayout.CENTER);
    }

    protected String getNumberSpaced(String numStr, int width) {
        int len = numStr.length();
        if (len >= width) return numStr.substring(0, width);

        int pad = width - len, left = pad / 2, right = pad - left;
        return " ".repeat(left) + numStr + " ".repeat(right);
    }

    public JPanel getContainer() {
        return container;
    }

    public static JScrollPane createScrollPane(Component component, Border border) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(12);
        scrollPane.setBorder(border);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;
    }

    public static JTextField getTextField() {
        JTextField textField = new JTextField();
        textField.setFont(PLAIN_CONSOLAS_BIG);
        textField.setBorder(new FlatLineBorder(new Insets(8, 8, 8, 8), new Color(1, 1, 1, 0.2f), 1, 12));
        return textField;
    }

    public static JTextPane getTextPane() {
        JTextPane textPane = new JTextPane();
        textPane.setFont(PLAIN_CONSOLAS_BIG);
        textPane.setBorder(new FlatLineBorder(new Insets(8, 8, 8, 8), new Color(1, 1, 1, 0.2f), 1, 12));
        return textPane;
    }

    public JLabel getTitle() {
        return titleLabel;
    }

    public JPanel getTitleBar() {
        return titleBar;
    }
}

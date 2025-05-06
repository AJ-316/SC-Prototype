package gameplayHook.SimUIPackage.Panels;

import com.formdev.flatlaf.ui.FlatLineBorder;
import gameplayHook.SimUIPackage.GradientBorder;

import javax.swing.*;
import java.awt.*;

public class CustomPanel extends JPanel {

    protected static final Font PLAIN_CONSOLAS = new Font("Consolas", Font.PLAIN, 14);
    protected static final Font BOLD_CONSOLAS = new Font("Consolas", Font.BOLD, 14);
    private final JPanel container;

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

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        super.add(titleLabel, BorderLayout.NORTH);
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
}

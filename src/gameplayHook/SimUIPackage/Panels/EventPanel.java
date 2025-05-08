package gameplayHook.SimUIPackage.Panels;

import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class EventPanel extends CustomPanel {

    // Door
    //     => CodeModule[proximity] triggered
    //     => State ( isOpen:true )
    // Drone
    //     => CodeModule[manual] triggered [IF x && y THEN reload]
    //     => State ( ammo:8, ammoType:HEAVY )
    private static final JTextPane logger = new JTextPane();

    private static int STYLE_COUNTER;
    private static int LINE_COUNTER;

    private static final Style NONE = createStyle(null, null);
    private static final Style LINE_NUMBER = createStyle(null, new Color(1, 1, 1, 0.1f));
    private static final Style MACHINE_NAME = createStyle(new Color(255, 255, 0, 180), null);
    private static final Style VALUE = createStyle(new Color(0, 255, 255, 180), null);
    private static final Style TOKEN = createStyle(new Color(255, 140, 0, 180), null);

    public EventPanel() {
        super("Events Timeline", new Color(200, 200, 100));
        logger.setFont(PLAIN_CONSOLAS_REG);
        logger.setEditable(false);
        logger.setBackground(new Color(19, 21, 25));
        logger.setBorder(new FlatLineBorder(new Insets(0, 0, 5, 20), null));
        logger.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(logger);
        JScrollPane scrollPane = createScrollPane(wrapper, new FlatLineBorder(new Insets(2, 2, 2, 2), new Color(1, 1, 1, 0.35f)));
        scrollPane.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));

        getContainer().add(scrollPane, BorderLayout.CENTER);

        for (int i = 0; i < 10; i++) {
            appendLineNumber();
            appendStyledText("Drone:\n", MACHINE_NAME);
            appendLineNumber();
            appendStyledText("\t=> CodeModule[", NONE);
            appendStyledText("manual", VALUE);
            appendStyledText("]\n", NONE);
            for (String kv : ("ammo:8,ammoType:HEAVY".split(","))) {
                String[] keyValue = kv.split(":");
                appendLineNumber();
                appendStyledText("\t - ", NONE);
                appendStyledText(keyValue[0], TOKEN);
                appendStyledText(":", NONE);
                appendStyledText(keyValue[1], VALUE);
                appendStyledText("\n", NONE);
            }

            if (i < 9) {
                appendStyledText("\n", NONE);
            }
        }
    }

    public void log(String text) {
        String prev = logger.getText().stripTrailing();
        prev += prev.isEmpty() ? text : "\n" + text;
        logger.setText(prev);
        SwingUtilities.invokeLater(() -> logger.setCaretPosition(logger.getDocument().getLength()));
    }

    private void appendLineNumber() {
        String lineNumber = String.valueOf(++LINE_COUNTER);
        lineNumber = getNumberSpaced(lineNumber, 4);

        appendStyledText(" " + lineNumber + " ", LINE_NUMBER);
        appendStyledText("  ", NONE);
    }

    private static Style createStyle(Color fg, Color bg) {
        String name = fg == null ? (bg == null ? "null" : bg.toString()) : fg.toString();
        Style style = logger.getStyledDocument().addStyle(name + ++STYLE_COUNTER, null);

        if(fg != null)
            StyleConstants.setForeground(style, fg);
        if(bg != null)
            StyleConstants.setBackground(style, bg);
        return style;
    }

    public void appendColoredText(String text, Color color) {
        StyledDocument doc = logger.getStyledDocument();
        Style style = logger.addStyle("ColorStyle", null);
        if(color != null)
            StyleConstants.setForeground(style, color);

        try {
            doc.insertString(doc.getLength(), text + "\n", style);
            logger.setCaretPosition(doc.getLength()); // Scroll to bottom
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendStyledText(String text, Style style) {
        StyledDocument doc = logger.getStyledDocument();

        try {
            doc.insertString(doc.getLength(), text, style);
            logger.setCaretPosition(doc.getLength()); // Scroll to bottom
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
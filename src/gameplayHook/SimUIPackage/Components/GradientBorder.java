package gameplayHook.SimUIPackage.Components;

import com.formdev.flatlaf.ui.FlatLineBorder;

import java.awt.*;

public class GradientBorder extends FlatLineBorder {
    private final Color startColor;
    private final Color midColor;
    private final Color endColor;

    public GradientBorder(Insets insets, Color startColor, Color midColor, Color endColor, float lineThickness, int arcRadius) {
        super(insets, startColor, lineThickness, arcRadius);
        this.startColor = startColor;
        this.midColor = midColor;
        this.endColor = endColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        LinearGradientPaint gradient = new LinearGradientPaint(
                new Point(x, y), new Point(x + width, y),
                new float[] { 0f, 0.61f, 1f },
                new Color[] { startColor, midColor, endColor }
        );

        g2.setPaint(gradient);

        g2.setStroke(new BasicStroke(getLineThickness()));
        g2.drawRoundRect(
                (int) (x + getLineThickness() / 2),
                (int) (y + getLineThickness() / 2),
                (int) (width - getLineThickness()),
                (int) (height - getLineThickness()),
                getArc(), getArc()
        );

        g2.dispose();
    }
}
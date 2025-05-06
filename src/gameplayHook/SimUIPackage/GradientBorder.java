package gameplayHook.SimUIPackage;

import com.formdev.flatlaf.ui.FlatLineBorder;

import java.awt.*;

public class GradientBorder extends FlatLineBorder {
    private final Color startColor;
    private final Color midColor;
    private final Color endColor;

    // Constructor accepts the gradient colors and corner radius
    public GradientBorder(Insets insets, Color startColor, Color midColor, Color endColor, float lineThickness, int arcRadius) {
        super(insets, startColor, lineThickness, arcRadius); // Set rounded corners
        this.startColor = startColor;
        this.midColor = midColor;
        this.endColor = endColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smoother borders
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define gradient
        LinearGradientPaint gradient = new LinearGradientPaint(
                new Point(x, y), new Point(x + width, y),
                new float[] { 0f, 0.61f, 1f },
                new Color[] { startColor, midColor, endColor }
        );

        g2.setPaint(gradient);

        // Use correct method names for line thickness and arc radius
        g2.setStroke(new BasicStroke(getLineThickness()));
        g2.drawRoundRect(
                (int) (x + getLineThickness() / 2),
                (int) (y + getLineThickness() / 2),
                (int) (width - getLineThickness()),
                (int) (height - getLineThickness()),
                getArc(), getArc()  // Correctly use getArc() for corner radius
        );

        g2.dispose();
    }
}
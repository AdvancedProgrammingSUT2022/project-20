package ir.ap.client.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Tile extends Polygon {

    public Tile(double x, double y, double r) {
        double h = r / 2 * Math.sqrt(3);
        getPoints().addAll(
                x, y - r,
                x + h, y - r / 2,
                x + h, y + r / 2,
                x, y + r,
                x - h, y + r / 2,
                x - h, y - r / 2
        );
        setStrokeWidth(1);
        setStroke(Color.GRAY);
    }
}

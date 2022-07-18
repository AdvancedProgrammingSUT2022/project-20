package ir.ap.client.components;

import ir.ap.client.App;
import ir.ap.client.components.map.serializers.TileSerializer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {

    private static double RADIUS;
    private static double HEIGHT;

    TileSerializer tile;

    StackPane images = new StackPane();

    public Hexagon(double x, double y, double r) {
        double h = r / 2 * Math.sqrt(3);
        RADIUS = r;
        HEIGHT = h;
        getPoints().addAll(
                x - r / 2, y - h,
                x + r / 2, y - h,
                x + r, y,
                x + r / 2, y + h,
                x - r / 2, y + h,
                x - r, y
        );
        setStrokeWidth(1);
        setStroke(Color.GRAY);
        images.setLayoutX(x - r);
        images.setLayoutY(y - h);
        images.setMaxWidth(2 * r);
        images.setMaxHeight(2 * h);
    }

    public Hexagon(double x, double y, double r, TileSerializer tile) {
        this(x, y, r);
        this.setTile(tile);
    }

    public TileSerializer getTile() {
        return tile;
    }

    public void setTile(TileSerializer tile) {
        this.tile = tile;
        loadFill();
    }

    public StackPane getImages() {
        return images;
    }

    public void loadFill() {
        ImageView terrainImage = new ImageView(getTerrainTypeImage(getTile().getTerrainTypeId()));
        ImageView featureImage = new ImageView(getTerrainFeatureImage(getTile().getTerrainFeatureId()));
        terrainImage.setFitWidth(2 * RADIUS);
        terrainImage.setFitHeight(2 * HEIGHT);
        featureImage.setFitWidth(2 * RADIUS);
        featureImage.setFitHeight(2 * HEIGHT);
        setFill(Color.WHITE);
        if (terrainImage != null)
            images.getChildren().add(terrainImage);
        if (featureImage != null)
            images.getChildren().add(featureImage);
    }

    private Image getTerrainTypeImage(int terrainTypeId) {
        String addr = "png/civAsset/map/Tiles/";
        switch (terrainTypeId) {
            case 2:
                return new Image(App.class.getResource(addr + "Desert.png").toExternalForm());
            case 3:
                return new Image(App.class.getResource(addr + "Grassland.png").toExternalForm());
            case 4:
                return new Image(App.class.getResource(addr + "Hill.png").toExternalForm());
            case 5:
                return new Image(App.class.getResource(addr + "Mountain.png").toExternalForm());
            case 6:
                return new Image(App.class.getResource(addr + "Ocean.png").toExternalForm());
            case 7:
                return new Image(App.class.getResource(addr + "Plains.png").toExternalForm());
            case 8:
                return new Image(App.class.getResource(addr + "Snow.png").toExternalForm());
            case 9:
                return new Image(App.class.getResource(addr + "Tundra.png").toExternalForm());
            default:
                return null;
        }
    }

    private Image getTerrainFeatureImage(int terrainFeatureId) {
        String addr = "png/civAsset/map/Tiles/";
        switch (terrainFeatureId) {
            case 3:
                return new Image(App.class.getResource(addr + "Flood plains.png").toExternalForm());
            case 4:
                return new Image(App.class.getResource(addr + "Jungle.png").toExternalForm());
            case 7:
                return new Image(App.class.getResource(addr + "Ice.png").toExternalForm());
            case 8:
                return new Image(App.class.getResource(addr + "Forest.png").toExternalForm());
            case 11:
                return new Image(App.class.getResource(addr + "Marsh.png").toExternalForm());
            case 13:
                return new Image(App.class.getResource(addr + "Oasis.png").toExternalForm());
            default:
                return null;
        }
    }
}

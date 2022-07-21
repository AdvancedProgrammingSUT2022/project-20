package ir.ap.client.components.map;

import com.google.gson.JsonObject;
import ir.ap.client.View;
import ir.ap.client.components.Hexagon;
import ir.ap.client.components.map.serializers.MapSerializer;
import ir.ap.client.components.map.serializers.TileSerializer;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MapView extends View {

    private static final double TILE_RADIUS = 70;
    private static final double TILE_HEIGHT = TILE_RADIUS / 2 * Math.sqrt(3);
    private static Hexagon[][] hexagons = new Hexagon[1000][1000];

    @FXML
    private AnchorPane root;

    public void initialize() {
        showCurrentMap();
    }

    private void showCurrentMap() {
        JsonObject mapJson = send("mapShow", currentUsername, 0, 1000, 1000);
        if (!responseOk(mapJson)) {
            System.out.println(getField(mapJson, "msg", String.class));
            enterMain();
            return;
        }
        // System.out.println(mapJson);
        MapSerializer map = GSON.fromJson(mapJson, MapSerializer.class);
        int height = map.getHeight();
        int width = map.getWidth();
        TileSerializer[][] tiles = map.getMap();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Hexagon tile = getHexagonByTile(tiles[i][j], i, j); // TODO: mishe har dafe new nakard!
                addHexagon(tile);
                if(tile.getTile().getResource() != null)tile.showResource(root);
                if(tile.getTile().getImprovement() != null)tile.showImprovement(root);
                tile.showUnits(root);
            }
        }
    }

    private void addHexagon(Hexagon tile) {
        root.getChildren().add(tile);
        root.getChildren().add(tile.getImages());
    }

    private Hexagon getHexagonByTile(TileSerializer tile, int i, int j) {
        if( hexagons[ i ][ j ] != null ){
            return hexagons[ i ][ j ];
        }
        double mapX = (TILE_RADIUS + 3 * TILE_RADIUS / 2 * tile.getY());
        double mapY = (2 * TILE_HEIGHT * tile.getX() + (tile.getY() % 2 == 0 ? 2 * TILE_HEIGHT : TILE_HEIGHT));
        return new Hexagon(mapX, mapY, TILE_RADIUS, tile);
    }

    public static Hexagon[][] getHexagons() {
        return hexagons;
    }
}

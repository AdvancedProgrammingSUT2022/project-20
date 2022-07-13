package ir.ap.client.components.map;

import com.google.gson.JsonArray;
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

    @FXML
    private AnchorPane root;

    public void initialize() {
        showCurrentMap();
    }

    private void showCurrentMap() {
        JsonObject mapJson = GAME_CONTROLLER.mapShow(currentUsername, 0, 1000, 1000);
        MapSerializer map = GSON.fromJson(mapJson, MapSerializer.class);
        int height = map.getHeight();
        int width = map.getWidth();
        TileSerializer[][] tiles = map.getMap();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Hexagon tile = getHexagonByTile(tiles[i][j]);
                addHexagon(tile);
            }
        }
    }

    private void addHexagon(Hexagon tile) {
        root.getChildren().add(tile);
        root.getChildren().add(tile.getImages());
    }

    private Hexagon getHexagonByTile(TileSerializer tile) {
        double mapX = (TILE_RADIUS + 3 * TILE_RADIUS / 2 * tile.getY());
        double mapY = (2 * TILE_HEIGHT * tile.getX() + (tile.getY() % 2 == 0 ? 2 * TILE_HEIGHT : TILE_HEIGHT));
        return new Hexagon(mapX, mapY, TILE_RADIUS, tile);
    }
}

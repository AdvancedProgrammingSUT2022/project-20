package ir.ap.client.components;

import ir.ap.client.App;
import ir.ap.client.components.map.serializers.TileSerializer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {

    private static double RADIUS;
    private static double HEIGHT;

    public static double RESOURCE_SIZE = 40;
    public static double IMPROVEMENT_SIZE = 40;

    TileSerializer tile;
    Unit combatUnit;
    Unit nonCombatUnit;
    double x;
    double y;

    StackPane images = new StackPane();

    public Hexagon(double x, double y, double r) {
        this.x = x;
        this.y = y;
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
    
    public void showUnits(AnchorPane root){
        if(tile.getCombatUnit() != null){
            showCombatUnit(x + (RADIUS/4), y + (HEIGHT/2), tile);
            root.getChildren().add( this.combatUnit );
        }
        if(tile.getNonCombatUnit() != null){
            showNonCombatUnit(x - (RADIUS/4), y + (HEIGHT/2), tile);
            root.getChildren().add( this.nonCombatUnit );
        }
    }

    public void showCombatUnit(double x, double y, TileSerializer tile1){
        if( this.combatUnit == null ){
            this.combatUnit = new Unit(x, y, (double)Unit.UNIT_SIZE, tile1.getCombatUnit());
        }
    }

    public void showNonCombatUnit(double x, double y, TileSerializer tile1){
        if( this.nonCombatUnit == null ){
            this.nonCombatUnit = new Unit(x, y, (double)Unit.UNIT_SIZE, tile1.getNonCombatUnit());
        }
    }

    public void showImprovement(AnchorPane root){
        ImageView improvementImage = new ImageView();
        try {
            improvementImage = new ImageView(new Image(App.class.getResource("png/civAsset/icons/ImprovementIcons/" + lowerCaseString(tile.getImprovement().getName()) + ".png").toExternalForm()));            
        } catch (Exception e) {
            System.out.println(tile.getImprovement().getName() + "not found!");            
        }    
        improvementImage.setFitWidth(IMPROVEMENT_SIZE);
        improvementImage.setFitHeight(IMPROVEMENT_SIZE);
        improvementImage.setLayoutX(x + (RADIUS/2) - (IMPROVEMENT_SIZE/2));
        improvementImage.setLayoutY(y - (IMPROVEMENT_SIZE/2));
        if(improvementImage != null){
            root.getChildren().add(improvementImage);
        }    
    }    

    public void showResource(AnchorPane root){
        ImageView resourcImage = new ImageView();
        try{
            resourcImage = new ImageView(new Image(App.class.getResource("png/civAsset/icons/ResourceIcons/" + lowerCaseString(tile.getResource().getName()) + ".png").toExternalForm()));
        }catch(Exception ex){
            System.out.println(tile.getResource().getName() + "not found!");
        }    
        resourcImage.setFitWidth(RESOURCE_SIZE);
        resourcImage.setFitHeight(RESOURCE_SIZE);
        resourcImage.setLayoutX(x - (RADIUS/2) - (RESOURCE_SIZE/2));
        resourcImage.setLayoutY(y - (RESOURCE_SIZE/2));
        if(resourcImage != null)
            root.getChildren().add(resourcImage);
    }        

    public Unit getCombatUnit() {
        return combatUnit;
    }

    public Unit getNonCombatUnit() {
        return nonCombatUnit;
    }

    public void setTile(TileSerializer tile) {
        this.tile = tile;
        loadFill();
    }

    public StackPane getImages() {
        return images;
    }

    public String lowerCaseString(String s1){
        String s2 = s1.toLowerCase();
        String s3 = Character.toUpperCase(s2.charAt(0)) + s2.substring(1);
        return s3;
    }

    public void loadFill() {
        ImageView terrainImage = new ImageView(getTerrainTypeImage(getTile().getTerrainTypeId()));
        ImageView featureImage = new ImageView(getTerrainFeatureImage(getTile().getTerrainFeatureId()));
        ImageView fogImage = new ImageView(new Image(App.class.getResource("png/civAsset/map/Tiles/FogOfWar.png").toExternalForm()));
        terrainImage.setFitWidth(2 * RADIUS);
        terrainImage.setFitHeight(2 * HEIGHT);
        featureImage.setFitWidth(2 * RADIUS);
        featureImage.setFitHeight(2 * HEIGHT);
        fogImage.setFitWidth(2 * RADIUS);
        fogImage.setFitHeight(2 * HEIGHT);
        setFill(Color.WHITE);
        if (terrainImage != null)
            images.getChildren().add(terrainImage);
        if (featureImage != null)
            images.getChildren().add(featureImage);
        if (getTile().getKnowledge().equals("FOG_OF_WAR"))
            images.getChildren().add(fogImage);
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

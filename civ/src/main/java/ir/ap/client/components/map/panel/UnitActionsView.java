package ir.ap.client.components.map.panel;

import java.util.stream.Stream;

import ir.ap.client.GameView;
import ir.ap.client.components.map.serializers.TileSerializer;
import ir.ap.client.components.map.serializers.UnitSerializer;
import ir.ap.model.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class UnitActionsView {
    @FXML
    Button move;

    @FXML
    Button sleep;
    
    @FXML
    Button alert;

    @FXML
    Button wake;

    @FXML
    Button delete;

    @FXML
    Button cancel;

    @FXML
    Button foundCity;

    @FXML
    Button buildRoad;

    @FXML
    Button removeRoad;

    @FXML
    Button clearLand;

    @FXML
    MenuButton lands;

    @FXML
    Button buildImprovement;

    @FXML
    Button repairImprovement;

    @FXML
    MenuButton improvements;

    @FXML
    Button fortify;

    @FXML
    Button garrison;

    @FXML
    Button rangeAttack;

    @FXML
    Button meleeAttack;

    @FXML
    Button pillage;

    @FXML
    AnchorPane root;

    UnitSerializer unitSerializer;

    public void initialize(){
        root.getStyleClass().add("backgroundColorB");
        Stream.of(move, sleep, wake, alert, delete, cancel, foundCity, buildRoad, removeRoad, clearLand, lands,
            buildImprovement, repairImprovement, improvements, fortify, garrison, rangeAttack, meleeAttack,
            pillage).forEach( button -> button.getStyleClass().add("gameButton"));
    }

    public void setUnit(UnitSerializer unitSerializer){
        this.unitSerializer = unitSerializer;
        TileSerializer tile = GameView.getTileById(unitSerializer.getTileId());
        if( unitSerializer.getUnitAction() == null )cancel.setVisible(false);
        if( unitSerializer.getUnitType().equals("WORKER") ){
            Stream.of(foundCity, fortify, garrison, rangeAttack, meleeAttack, pillage).forEach(button -> button.setVisible(false));
            if( GameView.tileHasRoadOrRailRoad(unitSerializer.getTileId()) ){
                buildRoad.setVisible(false);
            }else{
                removeRoad.setVisible(false);
            }
            lands.getItems().clear();
            int featureId = GameView.getTerrainFeature(unitSerializer.getTileId());
            if( featureId == 4 ){
                lands.getItems().add(new MenuItem("JUNGLE"));
            }else if( featureId == 8 ){
                lands.getItems().add(new MenuItem("FOREST"));
            }else if( featureId == 11 ){
                lands.getItems().add(new MenuItem("MARSH"));
            }
            improvements.getItems().clear();
            if(tile.getImprovement() == null){
                repairImprovement.setVisible(false);
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 1) )improvements.getItems().add(new MenuItem("CAMP"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 2) )improvements.getItems().add(new MenuItem("FARM"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(),3) )improvements.getItems().add(new MenuItem("LUMBER_MILL"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 4) )improvements.getItems().add(new MenuItem("MINE"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 5) )improvements.getItems().add(new MenuItem("PASTURE"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 6) )improvements.getItems().add(new MenuItem("PLANTATION"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 7) )improvements.getItems().add(new MenuItem("QUARRY"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 8) )improvements.getItems().add(new MenuItem("TRADING_POST"));
                if( GameView.tileCanBuildImprovement(tile.getIndex(), 9) )improvements.getItems().add(new MenuItem("FACTORY"));    
            }
            else{
                buildImprovement.setVisible(false);
            }
        }else if( unitSerializer.getUnitType().equals("SETTLER") ){
            Stream.of(buildRoad, removeRoad, clearLand, lands, buildImprovement, repairImprovement, improvements,
            fortify, garrison, rangeAttack, meleeAttack, pillage).forEach(button -> button.setVisible(false));
        }else{
            Stream.of(buildRoad, removeRoad, clearLand, lands, buildImprovement, repairImprovement, improvements,
            foundCity).forEach(button -> button.setVisible(false));
            if(unitSerializer.getCombatType() == "MELEE" ){
                rangeAttack.setVisible(false);
            }else{
                meleeAttack.setVisible(false);
            }
        }
    }

    public void move(){

    }

    public void sleep(){

    }

    public void wake(){

    }

    public void alert(){

    }

    public void delete(){

    }

    public void cancel(){

    }

    public void foundCity(){

    }

    public void buildRoad(){

    }

    public void clearLand(){
        // check if lands is empty
    }

    public void buildImprovement(){

    }

    public void repairImprovement(){

    }

    public void fortify(){

    }

    public void garrison(){

    }

    public void pillage(){

    }

    public void rangeAttack(){

    }

    public void meleeAttack(){

    }

}

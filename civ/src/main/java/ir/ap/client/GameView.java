package ir.ap.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ir.ap.client.components.map.CurrentResearchView;
import ir.ap.client.components.map.MapView;
import ir.ap.client.components.map.serializers.TechnologySerializer;
import ir.ap.client.components.map.serializers.TileSerializer;
import ir.ap.client.components.map.serializers.UnitSerializer;
import ir.ap.controller.GameController;
import ir.ap.controller.UserController;
import ir.ap.model.Improvement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GameView extends View {

    @FXML
    private BorderPane root;

    @FXML
    private AnchorPane mapPart;

    @FXML
    private AnchorPane infoPanel;

    private ScrollPane scrollMap;

    private AnchorPane map;
    private MapView mapView;

    public void initialize() throws IOException {
        initializeMap();
        scrollMap = new ScrollPane(map);
        scrollMap.setMaxWidth(App.SCREEN_WIDTH);
        scrollMap.setMaxHeight(App.SCREEN_HEIGHT-infoPanel.getPrefHeight());
        // scrollMap.setLayoutX(value);
        // scrollMap.setLayoutY(value);
        scrollMap.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMap.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Platform.runLater(() -> {
            scrollMap.requestFocus();
        });
        mapPart.getChildren().add(scrollMap);
        mapPart.getChildren().add(makeNextTurnButton());
        makeCurrentResearchPanel();
        makeInfoButtons();
        makeNotificationsButton();
    }

    private void makeNotificationsButton(){
        Button notificationPanel = new Button("Notifications");
        notificationPanel.getStyleClass().add("notificationButton");
        notificationPanel.setLayoutX(898);
        notificationPanel.setLayoutY(14);
        notificationPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showNotificationPanel();
            }
        });
        mapPart.getChildren().add(notificationPanel);
    }

    private void makeInfoButtons(){
        Button researchPanel = new Button("Researches");
        Button unitsPanel = new Button("Units");
        Button citiesPanel = new Button("Cities");
        Button demographicsPanel = new Button("Demographics");
        Stream.of(researchPanel, unitsPanel, citiesPanel, demographicsPanel).forEach( button -> 
        button.getStyleClass().add("gameButton"));
        researchPanel.setPrefWidth(130);
        researchPanel.setLayoutX(14);
        researchPanel.setLayoutY(186);
        researchPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showTechnologyInfoPanel();
            }            
        });
        unitsPanel.setPrefWidth(130);
        unitsPanel.setLayoutX(14);
        unitsPanel.setLayoutY(221);
        unitsPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showUnitsInfoPanel();
            }            
        });
        citiesPanel.setPrefWidth(130);
        citiesPanel.setLayoutX(14);
        citiesPanel.setLayoutY(256);
        citiesPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showCitiesInfoPanel();
            }            
        });
        demographicsPanel.setPrefWidth(130);
        demographicsPanel.setLayoutX(14);
        demographicsPanel.setLayoutY(291);
        demographicsPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showDemographicsInfoPanel();
            }            
        });
        mapPart.getChildren().addAll(researchPanel, unitsPanel, citiesPanel, demographicsPanel);
    }

    private void makeCurrentResearchPanel() throws IOException{
        JsonObject currentResearch = send("civGetCurrentResearch", currentUsername);
        if(responseOk(currentResearch)){
            TechnologySerializer technology = GSON.fromJson(currentResearch.get("technology"), TechnologySerializer.class);
            FXMLLoader fxmlLoader = new FXMLLoader(GameView.class.getResource("fxml/components/map/panel/currentResearch-view.fxml"));
            AnchorPane currentResearchRoot = fxmlLoader.load();
            CurrentResearchView currentResearchView = fxmlLoader.getController();
            currentResearchView.setLabel(technology.getName() + "(" + technology.getTurnsLeftForFinish() + ")");
            currentResearchView.setImage(new Image(GameView.class.getResource("png/technology/" + technology.getName().toLowerCase() + ".png").toExternalForm()));
            currentResearchRoot.setLayoutX(14);
            currentResearchRoot.setLayoutY(14);
            mapPart.getChildren().add(currentResearchRoot);
        }
    }

    private Button makeNextTurnButton(){
        Button nextTurn = new Button("NEXT TURN");
        nextTurn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nextTurn();
            }            
        });
        nextTurn.getStyleClass().add("nextTurnButton");
        nextTurn.setLayoutX(811);
        nextTurn.setLayoutY(508);
        nextTurn.setPrefWidth(200);
        nextTurn.setPrefHeight(24);
        return nextTurn;    
    }

    private void initializeMap() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameView.class.getResource("fxml/components/map/map-view.fxml"));
        map = fxmlLoader.load();
        mapView = fxmlLoader.getController();
    }

    public String lowerCaseString(String s1){
        String s2 = s1.toLowerCase();
        String s3 = Character.toUpperCase(s2.charAt(0)) + s2.substring(1);
        return s3;
    }

    private void nextTurn(){

    }

    private void showTechnologyInfoPanel()
    {
        ArrayList<String> improvementsNames = new ArrayList<String>();
        ArrayList<String> resourcesNames = new ArrayList<String>();
        ArrayList<String> unitTypesNames = new ArrayList<String>();
        ArrayList<String> buildingTypesNames = new ArrayList<String>();
        ArrayList<String> unitActionsNames = new ArrayList<String>();
        ArrayList<String> technologiesNames = new ArrayList<String>();

        JsonObject jsonObject = send("infoResearch", currentUsername);
        if(responseOk(jsonObject) == false) return;

        for(int j = 0; j < 6; j++)
        {
            JsonElement jsonElement = jsonObject.getAsJsonArray("objectsUnlocksSeparated").get(j);
            JsonArray jsonArray = GSON.fromJson(jsonElement, JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++)
            {
                JsonObject jsonObject1 = GSON.fromJson(jsonArray.get(i), JsonObject.class);
                String name = GSON.fromJson(jsonObject1.get("name"), String.class);
                if(j == 0) improvementsNames.add(name);
                if(j == 1) resourcesNames.add(name);
                if(j == 2) unitTypesNames.add(name);
                if(j == 3) buildingTypesNames.add(name);
                if(j == 4) unitActionsNames.add(name);
                if(j == 5) technologiesNames.add(name);
            }
        }
    }

    private void showUnitsInfoPanel(){
        JsonObject jsonObject = send("infoUnits", currentUsername);
        if(responseOk(jsonObject) == false) return;

        ArrayList<String> unitNames = new ArrayList<String>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("units");
        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObject1 = GSON.fromJson(jsonArray.get(i), JsonObject.class);
            String name = GSON.fromJson(jsonObject1.get("name"), String.class);
            unitNames.add(name);
        }
    }

    private void showCitiesInfoPanel(){
        JsonObject jsonObject = send("infoCities", currentUsername);
        if(responseOk(jsonObject) == false) return;

        ArrayList<String> cityNames = new ArrayList<String>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("cities");
        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObject1 = GSON.fromJson(jsonArray.get(i), JsonObject.class);
            String name = GSON.fromJson(jsonObject1.get("name"), String.class);
            cityNames.add(name);
        }
    }

    private void showDemographicsInfoPanel(){
        JsonObject jsonObject = send("infoDemographics", currentUsername);
        if(responseOk(jsonObject) == false) return;

        ArrayList<String> demographicsNames = new ArrayList<String>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("demographics");
        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObject1 = GSON.fromJson(jsonArray.get(i), JsonObject.class);
            String name = GSON.fromJson(jsonObject1.get("name"), String.class);
            demographicsNames.add(name);
        }
    }

    private void showMilitaryInfo(){
        // can go into this panel from unitsInfoPanel
        JsonObject jsonObject = send("infoMilitary", currentUsername);
        if(responseOk(jsonObject) == false) return;

        ArrayList<String> militaryNames = new ArrayList<String>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("military");
        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObject1 = GSON.fromJson(jsonArray.get(i), JsonObject.class);
            String name = GSON.fromJson(jsonObject1.get("name"), String.class);
            militaryNames.add(name);
        }
    }

    private void showEconomicInfo(){
        // can go into this panel from citiesInfoPanel
        JsonObject jsonObject = send("infoEconomic", currentUsername);
        if(responseOk(jsonObject) == false) return;

        ArrayList<String> economicNames = new ArrayList<String>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("economic");
        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObject1 = GSON.fromJson(jsonArray.get(i), JsonObject.class);
            String name = GSON.fromJson(jsonObject1.get("name"), String.class);
            economicNames.add(name);
        }
    }

    private void showNotificationPanel(){
        JsonObject jsonObject = send("infoNotifications", currentUsername);
        if(responseOk(jsonObject) == false) return;

        ArrayList<String> notificationNames = new ArrayList<String>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("notifications");
        for(int i = 0; i < jsonArray.size(); i++)
        {
            JsonObject jsonObject1 = GSON.fromJson(jsonArray.get(i), JsonObject.class);
            String name = GSON.fromJson(jsonObject1.get("name"), String.class);
            notificationNames.add(name);
        }
    }

    public void showSettingPanel(){
        
    }

    public void showMenuPanel(){

    }

    public static void showUnitInfoPanel(UnitSerializer unitSerializer){
        
    }

    public static void showTileInfoPanel(TileSerializer tileSerializer){
        
    }

    public static String getEra(){
        JsonObject jsonObject = send("getEra");
        if(!responseOk(jsonObject))return null;
        return GSON.fromJson(jsonObject.get("era"), String.class);
    }

}

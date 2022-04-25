package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import ir.ap.model.TerrainType.TerrainFeature;

public class Map {
    private  ArrayList<Tile> tiles ;
    private  ArrayList<Tile> khoshkiHa ;
    private int[][] dist = new int[100][100] ;
    private final int INF = 1000000000 ;
    
    private void generateRandomMap(long seed){
        Random randomobj = new Random();
        randomobj.setSeed(seed);
        // Set Tiles, khoshkiHa, initializing dist[][]
        // Call updateDistances
        
        for(int i = 0 ; i < 30 ; i ++){
            for(int j = 0 ; j < 30 ; j ++){
                if( (i >= 10 && i < 20) || (j >= 5 && j < 25) )continue ;
                Tile tile = new Tile( (i*30)+j, TerrainType.OCEAN, null, new ArrayList<>(Arrays.asList(new Resource[]{})));
                tiles.add( tile );
            }
        }

        TerrainType[] allTerrainTypes = TerrainType.values();
        Resource[] allResources = Resource.values();

        for(int i = 10 ; i < 20 ; i ++){
            for(int j = 5 ; j < 25 ; j ++){
                TerrainType terrainType ;
                TerrainType.TerrainFeature terrainFeature ;
                ArrayList<Resource> resources = new ArrayList<>() ;
                
                terrainType = allTerrainTypes[ randomobj.nextInt(allTerrainTypes.length) ] ;
                terrainFeature = terrainType.getFeaturesPossible().get( randomobj.nextInt( terrainType.getFeaturesPossible().size() ) );
                for(int z = 0 ; z < allResources.length; z ++){
                    if( (terrainType.isResourcePossible( allResources[ z ] ) == true) || (terrainFeature.isResourcePossible( allResources[ z ] ) == true) ){
                        if( randomobj.nextInt( 2 ) == 0 ){
                            resources.add( allResources[ z ] );
                        }
                    }
                }
                Tile tile = new Tile((i*30)+j, terrainType, terrainFeature, resources) ;
                for(int z = 0 ; z < 6 ; z ++){
                    if( randomobj.nextInt( 2 ) == 1 ){
                        tile.setHasRiverOnSide(i, true);
                    }
                } 
                // Hasayegi Ha And initialize dist[][]
                tiles.add( tile );
                khoshkiHa.add( tile );
            }
        }
        updateDistances();
    }

    public Map(long seed) {            
        generateRandomMap(seed);
    }

    public Tile getTileByIndex( int index ){
        for(int i = 0 ; i < tiles.size() ; i ++){
            if(tiles.get( i ).getIndex() == index){
                return tiles.get( i );
            }
        }
        return null;
    }

    public void updateDistances(){
        for(int k = 0 ; k < khoshkiHa.size() ; k ++){
            for(int i = 0 ; i < khoshkiHa.size() ; i ++){
                for(int j = 0 ; j < khoshkiHa.size() ; j ++){
                    if( dist[ i ][ j ] > (dist[ i ][ k ] + dist[ k ][ j ]) && (dist[k][j] != INF && dist[i][k] != INF) ){
                        dist[ i ][ j ] = dist[ i ][ k ] + dist[ k ][ j ] ;
                    }
                }
            }
        }
    }

    public void moveUnit(int index1, int index2){

    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Tile> getKhoshkiHa() {
        return khoshkiHa;
    }

    public int[][] getDist() {
        return dist;
    }
    
}

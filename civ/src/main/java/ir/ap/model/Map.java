package ir.ap.model;

import java.util.ArrayList;
import java.util.Random;

public class Map {
    private  ArrayList<Tile> tiles ;
    private  ArrayList<Tile> khoshkiHa ;
    private int[][] dist = new int[100][100] ;
    private final int INF = 1000000000 ;
    
    private void generateRandomMap(){
        // Set Tiles, khoshkiHa, initializing dist[][]
        // Call updateDistances
        for(int i = 0 ; i < 30 ; i ++){
            for(int j = 0 ; j < 30 ; j ++){

            }
        }
    }

    public Map() {            
        generateRandomMap();
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

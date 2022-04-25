package ir.ap.model;

import java.util.ArrayList;
import java.util.Random;

public class Map {
    private final ArrayList<Tile> Tiles;
    private final ArrayList<Tile> khoshkiHa ;
    private int[][] dist = new int[100][100] ;
    private final Random randomobj;    
    private final int INF = 1000000000 ;
    
    private void generateRandomMap(){
        // Set Tiles, khoshkiHa, initializing dist[][]
        // Call updateDistances

    }

    public Map(int seed) {        
        randomobj = new Random(); 
        randomobj.setSeed(seed);    
        generateRandomMap();
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

    
}

package ir.ap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Map {
    private  ArrayList<Tile> tiles = new ArrayList<>();
    private  ArrayList<Tile> khoshkiHa = new ArrayList<>();
    private int[][] dist = new int[910][910] ;
    private int[][] distNonWeighted = new int[910][910] ;
    private final int INF = 1000000000 ;
    
    private void generateRandomMap(long seed){
        Random randomobj = new Random();
        randomobj.setSeed(seed);
        // Set Tiles, khoshkiHa, initializing dist[][]
        // First Tiles in columns are has this pattern => down - up - down - up - ...

        for(int i = 0 ; i < 30 ; i ++){
            for(int j = 0 ; j < 30 ; j ++){
                if( (i >= 10 && i < 20) || (j >= 5 && j < 25) )continue ;
                Tile tile = new Tile( (i*30)+j, TerrainType.OCEAN, null, new ArrayList<>(Arrays.asList(new Resource[]{})));
                tiles.add( tile );
            }
        }

        for(int i = 0 ; i < 30*30 ; i ++){
            for(int j = 0 ; j < 30*30 ; j ++){
                dist[ i ][ j ] = INF;
                distNonWeighted[ i ][ j ] = INF;
            }
        }

        TerrainType[] allTerrainTypes = TerrainType.values(); 
        Resource[] allResources = Resource.values(); 

        for(int i = 10 ; i < 20 ; i ++){
            for(int j = 5 ; j < 25 ; j ++){
                TerrainType terrainType ;
                TerrainType.TerrainFeature terrainFeature = null;
                ArrayList<Resource> resources = new ArrayList<>() ;
                
                terrainType = allTerrainTypes[ randomobj.nextInt(allTerrainTypes.length) ] ;
                // TODO no terrain feature?
                if (terrainType.getFeaturesPossible().size() > 0)
                    terrainFeature = terrainType.getFeaturesPossible().get( randomobj.nextInt( terrainType.getFeaturesPossible().size() ) );
                for(int z = 0 ; z < allResources.length; z ++){
                    if( (terrainType.isResourcePossible( allResources[ z ] ) == true) ||
                        (terrainFeature != null && terrainFeature.isResourcePossible( allResources[ z ] ) == true) ){
                        if( randomobj.nextInt( 2 ) == 0 ){
                            resources.add( allResources[ z ] );
                        }
                    }
                }
                Tile tile = new Tile((i*30)+j, terrainType, terrainFeature, resources) ;
                tiles.add( tile );
                khoshkiHa.add( tile );
            }
        }
        
        // add neighborhoods
        for(int i = 0 ; i < 30 ; i ++){
            for(int j = 0 ; j < 30 ; j ++){
                Tile tile = this.getTileByIndex( (i*30)+j );
                int index = tile.getIndex() ;
                if( i != 0 )addNeighborAndWeight(tile, this.getTileByIndex(index-1), Direction.UP);
                if( i != 29 )addNeighborAndWeight(tile, this.getTileByIndex(index+1), Direction.DOWN);
                if( j != 0 && !(i == 0 && j%2 == 1) ){
                    if( j%2 == 0 )addNeighborAndWeight(tile, this.getTileByIndex(index-30), Direction.UP_LEFT);
                    else addNeighborAndWeight(tile, this.getTileByIndex(index-31), Direction.UP_LEFT);
                }
                if( j != 0 && !(i == 0 && j%2 == 0) ){
                    if( j%2 == 0 )addNeighborAndWeight(tile, this.getTileByIndex(index-29), Direction.DOWN_LEFT);
                    else addNeighborAndWeight(tile, this.getTileByIndex(index-30), Direction.DOWN_LEFT);
                }
                if( j != 29 && !(i == 0 && j%2 == 1) ){
                    if( j%2 == 0 )addNeighborAndWeight(tile, this.getTileByIndex(index-30), Direction.UP_RIGHT);
                    else addNeighborAndWeight(tile, this.getTileByIndex(index-31), Direction.UP_RIGHT);
                }
                if( j != 29 && !(i == 0 && j%2 == 0) ){
                    if( j%2 == 0 )addNeighborAndWeight(tile, this.getTileByIndex(index-29), Direction.DOWN_RIGHT);
                    else addNeighborAndWeight(tile, this.getTileByIndex(index-30), Direction.DOWN_RIGHT);
                }
            }
        }
        // add rivers and its weight's changes
        for(int i = 0 ; i < 30 ; i ++){
            for(int j = 0 ; j < 30 ; j ++){
                Tile tile = this.getTileByIndex( (i*30)+j );
                for(int z = 1 ; z <= 3 ; z ++){
                    boolean is_river = false;
                    if( randomobj.nextInt( 2 ) == 1 ){
                        is_river = true;
                    }
                    tile.setHasRiverOnSide(z, is_river);
                    Tile neighbor = tile.getNeighborOnSide( z );
                    if( neighbor != null )neighbor.setHasRiverOnSide((z+3)%6, is_river);
                    if( is_river == true ){
                        tile.setWeightOnSide(z, 5);
                        dist[ tile.getIndex() ][ neighbor.getIndex() ] = Math.min(dist[ tile.getIndex() ][ neighbor.getIndex() ], 5);
                        if( neighbor != null )neighbor.setWeightOnSide((z+3)%6, 5);
                        dist[ neighbor.getIndex() ][ tile.getIndex() ] = Math.min(dist[ neighbor.getIndex() ][ tile.getIndex() ], 5);
                    }
                }    
            }
        }
        updateDistances();
        updateNonWeightedDistances();
    }

    public Map(long seed) {            
        generateRandomMap(seed);
    }

    private void addNeighborAndWeight(Tile tile, Tile neighbor, Direction direction){
        tile.setNeighborOnSide(direction, neighbor);
        int weight1 = neighbor.getMovementCost();
        tile.setWeightOnSide(direction, weight1);
        dist[ tile.getIndex() ][ neighbor.getIndex() ] = Math.min(dist[ tile.getIndex() ][ neighbor.getIndex() ], weight1);
        distNonWeighted[ tile.getIndex() ][ neighbor.getIndex() ] = Math.min(distNonWeighted[ tile.getIndex() ][ neighbor.getIndex() ], 1);
    }

    public Tile getTileByIndex( int index ){
        return tiles.get(index);
    }

    public void updateNonWeightedDistances(){
        for(int k = 0 ; k < khoshkiHa.size() ; k ++){
            for(int i = 0 ; i < khoshkiHa.size() ; i ++){
                for(int j = 0 ; j < khoshkiHa.size() ; j ++){
                    int i1 = khoshkiHa.get( i ).getIndex() ;
                    int j1 = khoshkiHa.get( j ).getIndex() ;
                    int k1 = khoshkiHa.get( k ).getIndex() ;
                    if( distNonWeighted[ i1 ][ j1 ] > (distNonWeighted[ i1 ][ k1 ] + distNonWeighted[ k1 ][ j1 ]) && (distNonWeighted[ k1 ][ j1 ] != INF && distNonWeighted[ i1 ][ k1 ] != INF) ){
                        distNonWeighted[ i1 ][ j1 ] = distNonWeighted[ i1 ][ k1 ] + distNonWeighted[ k1 ][ j1 ] ;
                    }
                }
            }
        }
    }

    public void updateDistances(){
        for(int k = 0 ; k < khoshkiHa.size() ; k ++){
            for(int i = 0 ; i < khoshkiHa.size() ; i ++){
                for(int j = 0 ; j < khoshkiHa.size() ; j ++){
                    int i1 = khoshkiHa.get( i ).getIndex() ;
                    int j1 = khoshkiHa.get( j ).getIndex() ;
                    int k1 = khoshkiHa.get( k ).getIndex() ;
                    if( dist[ i1 ][ j1 ] > (dist[ i1 ][ k1 ] + dist[ k1 ][ j1 ]) && (dist[ k1 ][ j1 ] != INF && dist[ i1 ][ k1 ] != INF) ){
                        dist[ i1 ][ j1 ] = dist[ i1 ][ k1 ] + dist[ k1 ][ j1 ] ;
                    }
                }
            }
        }
    }

    public int getDistanceInTiles(Tile tile1, Tile tile2){
        return distNonWeighted[ tile1.getIndex() ][ tile2.getIndex() ];
    }

    public int getWeightedDistance(Tile tile1, Tile tile2){
        return dist[ tile1.getIndex() ][ tile2.getIndex() ];
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

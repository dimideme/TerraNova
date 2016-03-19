package com.demergis.terranova;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ShortArray;

public class MapManager {

    private final ContinentMap map;

    private Vector3[][] points;  		// 2D array of 3D points to be used as corners of triangles
    private ArrayList<Triangle> triangles;   // final list of triangles, with x,y,z coordinates

    private float latMin = -1000f;      // upper (northern) bound of world space, in miles
    private float latMax = 1000f;       // lower (southern) bound of world space, in miles
    private float longMin = -1000f;     // left (western) bound of world space, in miles
    private float longMax = 1000f;      // right (eastern) bound of world space, in miles
    private float latSpan = latMax - latMin;    // total height of world space, in miles
    private float longSpan = longMax - longMin; // total width of world space, in miles
    private float pointsPerUnitScalingFactor = 0.4f;                        // represents the number of points to place per unit of world coordinates
                                                                            // e.g. a scaling factor of 10 means that 200 points will be used for a width of 2000 miles
    private int pointsX = (int) ( longSpan * pointsPerUnitScalingFactor );			// number of points placed along X axis (number of columns)
    private int pointsY = (int) ( latSpan * pointsPerUnitScalingFactor );			// number of points placed along Y axis (number of rows)

    private float zCeiling, zFloor, zSeaLevel, zMax, zMin, zSpan;

    public MapManager() {

        map = new ContinentMap();

        points = new Vector3[pointsX][pointsY];
        triangles = new ArrayList<Triangle>();

        zCeiling = 1000f;
        zFloor = 0f;
        zSeaLevel = 200f;
        zMax = zFloor;
        zMin = zCeiling;
        zSpan = zMax - zSeaLevel;

    }

    public ContinentMap createMap( String mapId ) {

        Gdx.app.log(TerraNova.LOG, "MapManager: getMap()");

        // Step 1 - scatter points
        scatterPoints(new Random());

        // Step 2 - add z-dimension
        if( mapId.equals("random") )
            setRandomZValues(new Random());
        else
            readZValues(mapId);

        // Step 3 - set negative z-values to 0
        clipNegativeZValues();

        // Step 4alt - use a naive method to build triangles - simpler version of Delaunay method
        createSimpleTriangles();

        // Step 5 - determine moisture, altitude, latitude of triangles
        determineEnvironment();

        // Step 6 - determine color and add triangles to map
        determineColorAndAddToMap(zMax);

        return map;

    }

    private void scatterPoints(Random random) {
        Gdx.app.log( TerraNova.LOG, "MapManager: generateRandomMap(): scattering points" );
        float xMin = 0;
        float xMax = 0;
        float yMin = 0;
        float yMax = 0;
        for( int i = 0; i < pointsX; i++ ) {
            for( int j = 0; j < pointsY; j++ ) {
                float xPos = ( ( longSpan * i ) / pointsX ) + longMin;
                float yPos = ( ( latSpan * j ) / pointsY ) + latMin;
                points[i][j] = new Vector3( xPos, yPos, zFloor );
                if ( xMin > xPos ) xMin = xPos;
                if ( xMax < xPos ) xMax = xPos;
                if ( yMin > yPos ) yMin = yPos;
                if ( yMax < yPos ) yMax = yPos;
            }
        }
        Gdx.app.log(TerraNova.LOG, "MapManager: generateRandomMap(): scatterPoints(): xMin: " + xMin + ", xMax: " + xMax + ", " + yMin + ", yMax: " + yMax);
    }

    // This method is used for random maps, z-values are computed with a sum-of-sines method
    private void setRandomZValues(Random random) {
        Gdx.app.log( TerraNova.LOG, "MapManager: generateRandomMap(): adding z-dimension" );

        for( int i = 1; i < pointsX-1; i++ ) {
            for( int j = 1; j < pointsY-1; j++ ) {
                points[i][j].z = zCeiling / 11.875f * (
                                5f * ( (float)-Math.cos( 2 * Math.PI * i / pointsX) ) +
                                0.5f * ( (float)-Math.cos( 4 * Math.PI * i / pointsX) ) +
                                0.25f * ( (float)-Math.cos( 8 * Math.PI * i / pointsX) ) +
                                0.125f * ( (float)-Math.cos( 16 * Math.PI * i / pointsX) ) +
                                0.0625f * ( (float)-Math.cos( 32 * Math.PI * i / pointsX) ) +
                                5f * ( (float)-Math.cos( 2 * Math.PI * j / pointsY) ) +
                                0.5f * ( (float)-Math.cos( 4 * Math.PI * j / pointsY) ) +
                                0.25f * ( (float)-Math.cos( 8 * Math.PI * j / pointsY) ) +
                                0.125f * ( (float)-Math.cos( 16 * Math.PI * j / pointsY) ) +
                                0.0625f * ( (float)-Math.cos( 32 * Math.PI * j / pointsY ) ) );
                float z = points[i][j].z;
                if( z > zMax ) zMax = z;
                if( z < zMin ) zMin = z;
            }
            zSpan = zMax - zMin;
        }

        System.out.println("zmin, zmax = " + zMin + ", " + zMax);
    }

    // This method is used for pre-loaded maps: z-values are read directly from the heightmap
    private void readZValues( String mapId ) {
        Gdx.app.log( TerraNova.LOG, "MapManager: loadMap(): readZValues()" );

        FileHandle file = Gdx.files.internal( "heightmaps/" + mapId + ".png" );
        Pixmap pixmap = new Pixmap( file );

        int imageWidth = pixmap.getWidth();
        int imageHeight = pixmap.getHeight();

        for( int i = 1; i < pointsX; i++ ) {
            for( int j = 1; j < pointsY; j++ ) {
                int pixelX = (int)( ( points[i][j].x - longMin ) * imageWidth / longSpan );
                int pixelY = imageHeight - (int)( ( points[i][j].y - latMin ) * imageHeight / latSpan );
                int pixelValue = pixmap.getPixel( pixelX, pixelY );

                Color c = new Color();
                Color.rgb888ToColor(c, pixelValue);
                float z = (float) ( ( pixelValue ) / Math.pow(2, 32) ) * zCeiling;
                if ( z < 0 ) z+= zCeiling;  // compensate for the fact that very high altitude pixels may be interpreted as negative numbers due to using signed 32-bit integer

                points[i][j].z = z;
                if( z > zMax ) zMax = z;
                if( z < zMin ) zMin = z;

            }
        }

        Gdx.app.log(TerraNova.LOG, "MapManager: loadMap(): readZValues(): zMax: " + zMax + ", zMin: " + zMin);
    }

    private void createSimpleTriangles() {
        Gdx.app.log( TerraNova.LOG, "MapManager: generateRandomMap(): compute triangle positions");
        for (int i = 0; i < pointsX - 1; i++) {
            for (int j = 0; j < pointsY - 1; j++) {

                Triangle triangle1 = new Triangle();
                Triangle triangle2 = new Triangle();
                triangle1.vertices[0] = new Vector3(points[i][j]);
                triangle1.vertices[1] = new Vector3(points[i + 1][j]);
                triangle1.vertices[2] = new Vector3(points[i + 1][j + 1]);
                triangle2.vertices[0] = new Vector3(points[i][j]);
                triangle2.vertices[1] = new Vector3(points[i + 1][j + 1]);
                triangle2.vertices[2] = new Vector3(points[i][j + 1]);
                triangles.add(triangle1);
                triangles.add(triangle2);
            }
        }
    }

    private void clipNegativeZValues() {
        for( int i = 1; i < pointsX; i++ ) {
            for( int j = 1; j < pointsY; j++ ) {
                if( points[i][j].z < 0 ) points[i][j].z = 0;
            }
        }
        zMin = 0;
        zSpan = zMax - zSeaLevel;
    }

    public void determineEnvironment() {

        // for each triangle, determine whether triangle constitutes part of a coastline
        for( Triangle t : triangles ) {

            int numPointsAboveSeaLevel = 0;

            for( int i = 0; i < 3; i++ ) {

                Vector3 vertex = t.vertices[i];
                if ( vertex.z > zSeaLevel ) {
                    numPointsAboveSeaLevel++;
                }

            }
            t.isUnderwater = ( numPointsAboveSeaLevel == 0 );
            t.isCoastalPoint = ( numPointsAboveSeaLevel == 1 );
            t.isCoastalSegment = ( numPointsAboveSeaLevel == 2 );

        }

        // TODO: determine moisture value of triangle

    }

    private void determineColorAndAddToMap(float zMax) {
        Gdx.app.log( TerraNova.LOG, "MapManager: determineColorAndAddToMap(): setting triangle color and adding to map" );

        FileHandle file = Gdx.files.internal( "images/terrain.png" );
        Pixmap pixmap = new Pixmap( file );

        int imageWidth = pixmap.getWidth();
        int imageHeight = pixmap.getHeight();

        for( Triangle t : triangles ) {

            Color[] color = new Color[3];   // array of colors for the three points of the triangle
            for( int i = 0; i < 3; i++ ) {

                if( t.vertices[i].z < zSeaLevel )
                    color[i] = new Color( 0f, 0f, 0.5f, 1.0f );  // Navy Blue
                else {
                    Random random = new Random();
                    int pixel;
                    int pixelX, pixelY;
                    pixelX = (int)(random.nextFloat() * imageWidth);
                    pixelY = (int)( ( 1 - ( t.vertices[i].z - zSeaLevel) / zSpan) * imageHeight );
                    pixel = pixmap.getPixel( pixelX, pixelY );
                    //Gdx.app.log( TerraNova.LOG, "MapManager: determineColorAndAddToMap(): for zvalue: "+ t.vertices[i].z + ", selecting pixel " + pixelX + ", "+ pixelY + ", with value " + pixel );
                    Color c = new Color();
                    Color.rgba8888ToColor(c, pixel);
                    color[i] = new Color( c.r, c.g, c.b, c.a );
                }

            }

            map.addTriangle(
                    t.vertices[0], color[0],
                    t.vertices[1], color[1],
                    t.vertices[2], color[2]  );
        }
    }

    public void getCoastline() {

    }


    public void dispose() {


    }
}
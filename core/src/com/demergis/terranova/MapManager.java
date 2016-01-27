package com.demergis.terranova;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ShortArray;
import com.badlogic.gdx.math.DelaunayTriangulator;


public class MapManager {

    private final ContinentMap map;

    private Vector3[][] points;  		// 2D array of 3D points to be used as corners of triangles
    private float[] delPoints;			// 1D array of 2D points, pairs of alternating x and y values
    private ShortArray delIndices;		// result of delaunay triangulation, list of indices into delPoint pairs
    private ArrayList<Triangle> triangles;   // final list of triangles, with x,y,z coordinates

    private float latMin = -1000f;      // upper (northern) bound of world space, in miles
    private float latMax = 1000f;       // lower (southern) bound of world space, in miles
    private float longMin = -1000f;     // left (western) bound of world space, in miles
    private float longMax = 1000f;      // right (eastern) bound of world space, in miles
    private float latSpan = latMax - latMin;    // total height of world space, in miles
    private float longSpan = longMax - longMin; // total width of world space, in miles
    private float pointsPerUnitScalingFactor = 0.1f;                        // represents the number of points to place per unit of world coordinates
                                                                            // e.g. a scaling factor of 10 means that 200 points will be used for a width of 2000 miles
    private int pointsX = (int) ( longSpan * pointsPerUnitScalingFactor );			// number of points placed along X axis (number of columns)
    private int pointsY = (int) ( latSpan * pointsPerUnitScalingFactor );			// number of points placed along Y axis (number of rows)

    private float zCeiling, zFloor, zSeaLevel, zMax, zMin, zSpan;

    public MapManager() {

        map = new ContinentMap();

        points = new Vector3[pointsX][pointsY];
        delPoints = new float[pointsX*pointsY*2];
        delIndices = new ShortArray();
        triangles = new ArrayList<Triangle>();

        zCeiling = 1000f;
        zFloor = 0f;
        zSeaLevel = 55f;
        zMax = zFloor;
        zMin = zCeiling;
        zSpan = zMax - zMin;

    }

    public ContinentMap createMap( String mapId ) {

        Gdx.app.log( TerraNova.LOG, "MapManager: getMap()" );

        // Step 1 - scatter points
        scatterPoints(new Random());

        // Step 2 - add z-dimension
        if( mapId.equals("random") )
            setRandomZValues(new Random());
        else
            readZValues(mapId);

        // Step 3 - set negative z-values to 0
        clipNegativeZValues();

        // Step 4 - compute Delaunay indices, and use them to build triangles
        //computeDelaunayIndices();
        //createDelaunayTriangles();

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
        for( int i = 0; i < pointsX; i++ ) {
            for( int j = 0; j < pointsY; j++ ) {
                float xPos = ( ( longSpan * ( i + random.nextFloat() - 0.5f ) ) / pointsX ) + longMin;
                float yPos = ( ( latSpan * ( j + random.nextFloat() - 0.5f ) ) / pointsY ) + latMin;
                points[i][j] = new Vector3( xPos, yPos, zFloor );
            }
        }
    }

    // This method is used for random maps, z-values are computed with a sum-of-sines method
    private void setRandomZValues(Random random) {
        Gdx.app.log( TerraNova.LOG, "MapManager: generateRandomMap(): adding z-dimension" );

        for( int i = 1; i < pointsX-1; i++ ) {
            for( int j = 1; j < pointsY-1; j++ ) {
                points[i][j].z =
                        0.2f * ( 60 + (float)Math.sin( i * 2 * Math.PI / pointsX ) ) +
                                0.2f * ( 60 + (float)Math.sin( i * 20 * Math.PI / pointsX ) ) +
                                0.2f * ( 60 + (float)Math.sin( j * 2 * Math.PI / pointsY ) ) +
                                0.2f * ( 60 + (float)Math.sin( j * 20 * Math.PI / pointsY ) ) +
                                0.2f * ( 60 + (float)( random.nextFloat() - 0.5f ) );
                float z = points[i][j].z;
                if( z > zMax ) zMax = z;
                if( z < zMin ) zMin = z;
                zSpan = zMax - zMin;

            }
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
                points[i][j].z = pixmap.getPixel( pixelX, pixelY ) / (float)Math.pow(2, 32) * zCeiling;

                float z = points[i][j].z;
                if( z > zMax ) zMax = z;
                if( z < zMin ) zMin = z;

            }
        }
    }



    private void computeDelaunayIndices() {
        Gdx.app.log( TerraNova.LOG, "MapManager: generateRandomMap(): computing Delaunay indices" );
        DelaunayTriangulator dt = new DelaunayTriangulator();
        int count = 0;
        for( int i = 0; i < pointsX; i++ ) {
            for( int j = 0; j < pointsY; j++ ) {
                delPoints[count++] = points[i][j].x;
                delPoints[count++] = points[i][j].y;
            }
        }

        delIndices = dt.computeTriangles( delPoints, 0, delPoints.length / 2, false );
    }

    private void createDelaunayTriangles() {
        Gdx.app.log( TerraNova.LOG, "MapManager: generateRandomMap(): constructing triangles" );
        for( int i = 0; i < delIndices.size-2; i+=3 ) {
            Triangle triangle = new Triangle();
            triangle.vertices[0] = new Vector3(
                    delPoints[ 2 * delIndices.get(i+0) ],
                    delPoints[ 2 * delIndices.get(i+0) + 1 ],
                    points[ delIndices.get(i+0) / pointsY ]
                            [ delIndices.get(i+0) % pointsY ].z );
            triangle.vertices[1] = new Vector3(
                    delPoints[ 2 * delIndices.get(i+1) ],
                    delPoints[ 2 * delIndices.get(i+1) + 1 ],
                    points[ delIndices.get(i+1) / pointsY ]
                            [ delIndices.get(i+1) % pointsY ].z );
            triangle.vertices[2] = new Vector3(
                    delPoints[ 2 * delIndices.get(i+2) ],
                    delPoints[ 2 * delIndices.get(i+2) + 1 ],
                    points[ delIndices.get(i+2) / pointsY ]
                            [ delIndices.get(i+2) % pointsY ].z );
            triangles.add( triangle );
        }
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
    }

    public void determineEnvironment() {

        // for each triangle, determine whether triangle constitutes part of a coastline
        for( Triangle t : triangles ) {

            int numPointsAboveSeaLevel = 0;
            for( int i = 0; i < 3; i++ ) {
                if ( t.vertices[i].z > zSeaLevel ) numPointsAboveSeaLevel++;
            }
            t.isUnderwater = ( numPointsAboveSeaLevel == 0 );
            t.isCoastalPoint = ( numPointsAboveSeaLevel == 1 );
            t.isCoastalSegment = ( numPointsAboveSeaLevel == 2 );

        }

        // TODO: determine moisture value of triangle

    }

    private void determineColorAndAddToMap(float zMax) {
        Gdx.app.log( TerraNova.LOG, "MapManager: generateRandomMap(): setting triangle color and adding to map" );
        for( Triangle t : triangles ) {

            Color[] color = new Color[3];   // array of colors for the three points of the triangle
            for( int i = 0; i < 3; i++ ) {

                if( t.vertices[i].z <= zSeaLevel && !t.isUnderwater )
                    color[i] = new Color( 237f/256, 201f/256, 175f/256, 1.0f );   // Sand
                else if( t.vertices[i].z <= zSeaLevel && t.isUnderwater )
                    color[i] = new Color( 0f, 0f, 0.5f, 1.0f );  // Navy Blue
                else if( t.vertices[i].z > 0.8f * zSpan + zMin ) color[i] = Color.WHITE;
                else if( t.vertices[i].z > 0.7f * zSpan + zMin ) color[i] = Color.DARK_GRAY;
                else if( t.vertices[i].z > 0.6f * zSpan + zMin ) color[i] = Color.GRAY;
                else if( t.vertices[i].z > 0.4f * zSpan + zMin ) color[i] = new Color( 0.8f, 0.8f, 0.3f, 1.0f ); // BROWN
                else color[i] = Color.GREEN;

            }

            map.addTriangle(
                    t.vertices[0], color[0],
                    t.vertices[1], color[1],
                    t.vertices[2], color[2]  );
        }
    }


    public void dispose() {


    }
}
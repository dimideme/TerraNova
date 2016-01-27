package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Dimitri on 1/24/2016.
 */
public class ContinentMap {

    //Position attribute - (x, y, z)
    public static final int POSITION_COMPONENTS = 3;

    //Color attribute - (r, g, b, a)
    public static final int COLOR_COMPONENTS = 4;

    //Total number of components for all attributes
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;

    //The "size" (total number of floats) for a single triangle
    public static final int PRIMITIVE_SIZE = 3 * NUM_COMPONENTS;

    //The maximum number of triangles our mesh will hold
    public static final int MAX_TRIS = 500000;

    //The maximum number of vertices our mesh will hold
    public static final int MAX_VERTS = MAX_TRIS * 3;

    //The array which holds all the data, interleaved like so:
    //	    x, y, z, r, g, b, a
    protected float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];
    // TODO: create and use an array of indices to improve performance?

    //The current index that we are pushing triangles into the array
    protected int idx = 0;

    public ContinentMap() {
        Gdx.app.log( TerraNova.LOG, "ContinentMap: ContinentMap()" );

    }

    public float[] getVerts() {
        Gdx.app.log( TerraNova.LOG, "ContinentMap: getVerts()" );
        return verts;
    }

    public int getVertexCount() {
        //Gdx.app.log( TerraNova.LOG, "ContinentMap: getVertexCount(): " + idx / NUM_COMPONENTS );
        return idx / NUM_COMPONENTS;
    }

    public void addTriangle( Vector3 p1, Color c1, Vector3 p2, Color c2, Vector3 p3, Color c3 ) {
        //Gdx.app.log( TerraNova.LOG, "ContinentMap: addTriangle()" );

        //bottom left vertex
        verts[idx++] = p1.x;		// Position (x, y, z)
        verts[idx++] = p1.y;
        verts[idx++] = p1.z;
        verts[idx++] = c1.r;     	// Color (r, g, b, a)
        verts[idx++] = c1.g;
        verts[idx++] = c1.b;
        verts[idx++] = c1.a;

        verts[idx++] = p2.x;		// Position (x, y, z)
        verts[idx++] = p2.y;
        verts[idx++] = p2.z;
        verts[idx++] = c2.r;		// Color (r, g, b, a)
        verts[idx++] = c2.g;
        verts[idx++] = c2.b;
        verts[idx++] = c2.a;

        verts[idx++] = p3.x;		// Position (x, y, z)
        verts[idx++] = p3.y;
        verts[idx++] = p3.z;
        verts[idx++] = c3.r;		// Color (r, g, b, a)
        verts[idx++] = c3.g;
        verts[idx++] = c3.b;
        verts[idx++] = c3.a;
    }

}

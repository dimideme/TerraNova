package com.demergis.terranova;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Dimitri on 1/24/2016.
 */
public class Triangle {

    public Vector3[] vertices;

    public Triangle() {
        vertices = new Vector3[3];
        //isDiscovered = false;
        //isVisible = false;
    }

    public Triangle( Vector3[] verts ) {
        vertices = verts;
        //isDiscovered = false;
        //isVisible = false;
    }

    public Vector3[] getVertices() {
        return vertices;
    }

    public void setVertices( Vector3[] verts ) {
        vertices = verts;
    }

    public void setDiscovererd( boolean d ) {
        //isDiscovered = d;
    }

    public void setVisible( boolean v ) {
        //isVisible = v;
    }

}

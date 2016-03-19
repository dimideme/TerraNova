package com.demergis.terranova;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Dimitri on 2/6/2016.
 */
public class Segment {

    private Vector3[] points;

    public Segment() {
        points = new Vector3[2];
    }

    public Segment( Vector3 a, Vector3 b ) {
        this.points[0] = a;
        this.points[1] = b;
    }

    public void addPoint( Vector3 p ) {
        for( int i = 0; i < 2; i++ ) {
            if( points[i] == null ) {
                points[i] = p;
                break;
            }
        }
    }

    public Vector3 getPoint( int i ) {
        return points[i];
    }

}

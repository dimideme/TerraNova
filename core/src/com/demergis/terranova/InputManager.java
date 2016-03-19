package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Dimitri on 1/23/2016.
 */
public class InputManager {

    public static Vector2 keyForce = new Vector2();
    public static Vector2 click = new Vector2();

    public static Vector2 mouseClickScreen = new Vector2();
    public static Vector2 mouseClickWorld = new Vector2();

    public static void update() {
        keyForce.x = 0;
        keyForce.y = 0;
        click.x = 0;
        click.y = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Gdx.app.log(TerraNova.LOG, "LEFT");
            keyForce.x -=1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            keyForce.x +=1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            keyForce.y -=1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            keyForce.y +=1;
        }
        if( Gdx.input.isTouched() ) {
            click.x = Gdx.input.getX();
            click.y = Gdx.input.getY();
        }
    }

}

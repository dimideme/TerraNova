package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Dimitri on 1/23/2016.
 */
public class InputManager {

    public static Vector2 keyForce = new Vector2();

    public static void update() {
        keyForce.x = 0;
        keyForce.y = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            keyForce.x -=1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            keyForce.x +=1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            keyForce.y -=1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            keyForce.y +=1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Gdx.app.log("KEYPRESS", "SPACE");
            keyForce.y +=100;
        }
    }
}

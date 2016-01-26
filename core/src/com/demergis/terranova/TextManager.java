package com.demergis.terranova;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by Dimitri on 1/23/2016.
 */
public class TextManager {

    private static BitmapFont bfont = new BitmapFont();
    private static SpriteBatch spriteBatchHandle;

    public void TextManager( SpriteBatch batch, BitmapFont bmf ) {
        this.bfont = bmf;
        this.spriteBatchHandle = batch;

        bfont.setColor(Color.CYAN);
    }


    public static void setSpriteBatch(SpriteBatch batch) {

        bfont.setColor(Color.CYAN);
        spriteBatchHandle = batch;
    }

    public static void draw(java.lang.CharSequence msg, OrthographicCamera cam, float x, float y ) {
        Vector3 position = new Vector3(x, y, 0);
        cam.unproject(position);
        bfont.draw(spriteBatchHandle, msg, position.x, position.y );

    }



}

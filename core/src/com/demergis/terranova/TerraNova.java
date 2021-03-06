package com.demergis.terranova;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TerraNova extends Game {

    public static String LOG = "LOG";

    public static String DEV_MODE = "DEBUG";

    public SpriteBatch batch;
    public BitmapFont bitmapFont;

    public static MapManager mapManager;
    public static TimeManager timeManager;

    public float screenWidth;
    public float screenHeight;

	@Override
	public void create () {

        Gdx.app.log(TerraNova.LOG, "TerraNova: create(): Creating mapManager");
        mapManager = new MapManager();
        timeManager = new TimeManager();

        bitmapFont = new BitmapFont();
		batch = new SpriteBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        Gdx.app.log(TerraNova.LOG, "TerraNova: create(): Setting screen to new MapScreen");
        this.setScreen(new MenuScreen(this));
        super.render();  // Need to call this immediately after setScreen, otherwise screens won't render!

	}

    public MapManager getMapManager() {
        return mapManager;
    }

	@Override
	public void render () {
        super.render();  // Need this here otherwise screens won't render!
	}

    @Override
    public void resize( int w, int h ) {
        Gdx.app.log( TerraNova.LOG, "TerraNova: resize()" );
        screenWidth = w;
        screenHeight = h;
    }

    @Override
    public void pause() {
        super.pause();
        Gdx.app.log( TerraNova.LOG, "TerraNova: pause()" );
    }

    @Override
    public void resume() {
        super.resume();
        Gdx.app.log(TerraNova.LOG, "TerraNova: resume()");
    }

    @Override
    public void setScreen( Screen screen ) {
        super.setScreen( screen );
        Gdx.app.log( TerraNova.LOG, "TerraNova: setScreen(): " + screen.getClass().getSimpleName());
    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.log(TerraNova.LOG, "TerraNova: dispose():");

    }
}

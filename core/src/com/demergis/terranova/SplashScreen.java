package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;


public class SplashScreen implements Screen {
	
	public SplashScreen( TerraNova game ) {

	}

    @Override
    public void show() {
        Gdx.app.log( TerraNova.LOG, "SplashScreen: show()" );
    }

    @Override
    public void render( float delta ) {
        Gdx.app.log( TerraNova.LOG, "SplashScreen: render()" );
    }

    @Override
    public void resize( int w, int h ) {
        Gdx.app.log( TerraNova.LOG, "SplashScreen: resize()" );
    }

    @Override
    public void hide() {
        Gdx.app.log( TerraNova.LOG, "SplashScreen: hide()" );
    }

    @Override
    public void pause() {
        Gdx.app.log( TerraNova.LOG, "SplashScreen: pause()" );
    }

    @Override
    public void resume() {
        Gdx.app.log(TerraNova.LOG, "SplashScreen: resume()");
    }

    @Override
    public void dispose() {
        Gdx.app.log( TerraNova.LOG, "SplashScreen: dispose()" );
    }


}

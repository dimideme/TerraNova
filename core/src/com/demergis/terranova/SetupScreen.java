package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class SetupScreen implements Screen {

    public SetupScreen( TerraNova game ) {

    }

    @Override
    public void show() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: show()" );
    }

    @Override
    public void render( float delta ) {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: render()" );
    }

    @Override
    public void resize( int w, int h ) {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: resize()" );
    }

    @Override
    public void hide() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: hide()" );
    }

    @Override
    public void pause() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: pause()" );
    }

    @Override
    public void resume() {
        Gdx.app.log(TerraNova.LOG, "MenuScreen: resume()");
    }

    @Override
    public void dispose() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: dispose()" );
    }

}

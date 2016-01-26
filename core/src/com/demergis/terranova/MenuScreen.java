package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen implements Screen {

	public MenuScreen(TerraNova game, SpriteBatch batch) {
		// TODO Auto-generated constructor stub
	}

    @Override
    public void show() {

        /*
        // retrieve the default table actor
        Table table = super.getTable();
        table.add( "Terra Nova" ).spaceBottom( 50 );
        table.row();

        // register the "Game Setup" button
        TextButton setupButton = new TextButton( "Game Setup", getSkin() );
        setupButton.addListener( new DefaultActorListener() {
            @Override
            public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
                super.touchUp( event, x, y, pointer, button );
                //game.getSoundManager().play( TerraNovaSound.CLICK );
                game.setScreen( new SetupScreen( game ) );
            }
        } );
        table.add( setupButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
        table.row();
        
        // register the "Exit" button
        TextButton exitButton = new TextButton( "Exit", getSkin() );
        setupButton.addListener( new DefaultActorListener() {
            @Override
            public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
                super.touchUp( event, x, y, pointer, button );
                //game.getSoundManager().play( TerraNovaSound.CLICK );
                //TODO: exit game
            }
        } );
        table.add( exitButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
        table.row();
        */

		/*
		// creates the table actor
		Table table = new Table();
		// 100% width and 100% height on the table (fills the stage)
		table.setFillParent(true);
		// add the table to the stage
		stage.addActor(table);
		// add the welcome message with a margin-bottom of 50 units
		table.add( "Welcome to Tyrian for Android!" ).spaceBottom( 50 );
		// move to the next row
		table.row();
		// add the start-game button sized 300x60 with a margin-bottom of 10 units
		table.add( startGameButton ).size( 300f, 60f ).uniform().spaceBottom( 10 );
		// move to the next row
		table.row();
		// add the options button in a cell similiar to the start-game button's cell
		table.add( optionsButton ).uniform().fill().spaceBottom( 10 );
		// move to the next row
		table.row();
		// add the high-scores button in a cell similiar to the start-game button's cell
		table.add( highScoresButton ).uniform().fill();
		*/



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
        Gdx.app.log( TerraNova.LOG, "MenuScreen: resume()" );
    }

    @Override
    public void dispose() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: dispose()" );
    }
	
}

package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class SetupScreen implements Screen {
	
	private ButtonGroup mapButtons;
	private ButtonGroup nationButtons;
	
	private TextButton northAmericaButton;
	private TextButton irelandButton;
	private TextButton japanButton;
    private TextButton randomButton;
    
    
    private TextButton englandButton;
    private TextButton franceButton;
    private TextButton spainButton;
    private TextButton portugalButton;
    private TextButton startButton;

    //private MapSelectionListener mapSelectionListener;
    //private NationSelectionListener nationSelectionListener;

    public SetupScreen( TerraNova game, SpriteBatch batch ) {

        // create the listeners
        //mapSelectionListener = new MapSelectionListener();
        //nationSelectionListener = new NationSelectionListener();
    }

    @Override
    public void show() {

        // start playing the menu music (the player might be returning from the
        // level screen)
        //game.getMusicManager().play( TyrianMusic.MENU );

        // retrieve the default table actor
        Table table = new Table();
        table.defaults().spaceBottom( 20 );
        table.columnDefaults( 0 ).padRight( 20 );
        table.columnDefaults( 4 ).padLeft( 10 );
        
        table.row();
        table.add( "Start Game" ).colspan( 5 );

        // create the map selection buttons
        table.row();
        table.add( "Maps" );

        /*
        randomButton = new TextButton( "Random Map", getSkin(), "toggle" );
        randomButton.addListener( mapSelectionListener );
        randomButton.setName("random");
        table.add( randomButton ).fillX().padRight(10);

        northAmericaButton = new TextButton( "North America", getSkin(), "toggle" );
        northAmericaButton.addListener( mapSelectionListener );
        northAmericaButton.setName("NorthAmerica");
        table.add( northAmericaButton ).fillX().padRight(10);
        
        irelandButton = new TextButton( "Ireland", getSkin(), "toggle" );
        irelandButton.addListener( mapSelectionListener );
        irelandButton.setName("Ireland");
        table.add( irelandButton ).fillX().padRight(10);
        
        japanButton = new TextButton( "Japan", getSkin(), "toggle" );
        japanButton.addListener( mapSelectionListener );
        japanButton.setName("Japan");
        table.add( japanButton ).fillX().padRight(10);
        
        mapButtons = new ButtonGroup();
        mapButtons.setMinCheckCount(1);
        mapButtons.setMaxCheckCount(1);
        mapButtons.setUncheckLast(true);
        mapButtons.add(randomButton);
        mapButtons.add(northAmericaButton); 
        mapButtons.add(irelandButton); 
        mapButtons.add(japanButton); 
        
        // create the country selection buttons
        table.row();
        table.add( "Nations" );

        englandButton = new TextButton( "England", getSkin(), "toggle" );
        englandButton.addListener( nationSelectionListener );
        englandButton.setName("england");
        table.add( englandButton ).fillX().padRight(10);

        franceButton = new TextButton( "France", getSkin(), "toggle" );
        franceButton.addListener( nationSelectionListener );
        franceButton.setName("france");
        table.add( franceButton ).fillX().padRight(10);

        spainButton = new TextButton( "Spain", getSkin(), "toggle" );
        spainButton.addListener( nationSelectionListener );
        spainButton.setName("spain");
        table.add( spainButton ).fillX().padRight(10);

        portugalButton = new TextButton( "Portugal", getSkin(), "toggle" );
        portugalButton.addListener( nationSelectionListener );
        portugalButton.setName("portugal");
        table.add( portugalButton ).fillX().padRight(10);
        
        nationButtons = new ButtonGroup();
        nationButtons.add( englandButton ); 
        nationButtons.add( franceButton ); 
        nationButtons.add( spainButton ); 
        nationButtons.add( portugalButton );
        nationButtons.setMinCheckCount(1);
        nationButtons.setMaxCheckCount(1);
        nationButtons.setUncheckLast(true);
        
        // create the start button
        TextButton startButton = new TextButton( "Start Game!", getSkin() );
        startButton.addListener( new DefaultActorListener() {
            public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
                //game.getSoundManager().play( TyrianSound.CLICK );
                Gdx.app.log( TerraNova.LOG, "Starting game: " + mapButtons.getChecked().getName() + ", " + nationButtons.getChecked().getName() );
                game.setScreen( new MapScreen( game, mapButtons.getChecked().getName() ) );
            }
        } );
        table.row();
        table.add( startButton ).size( 250, 60 ).colspan( 5 );

        // create the back button
        TextButton backButton = new TextButton( "Back to main menu", getSkin() );
        backButton.addListener( new DefaultActorListener() {
            public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
                //game.getSoundManager().play( TyrianSound.CLICK );
                game.setScreen( new MenuScreen( game ) );
            }
        } );
        table.row();
        table.add( backButton ).size( 250, 60 ).colspan( 5 );

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
        Gdx.app.log(TerraNova.LOG, "MenuScreen: resume()");
    }

    @Override
    public void dispose() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: dispose()" );
    }


    /**
     * Listeners for all the buttons.
     */
    /*
    private class MapSelectionListener extends DefaultActorListener {
        @Override
        public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
            super.touchUp( event, x, y, pointer, button );
            //game.getSoundManager().play( TyrianSound.CLICK );

            Actor actor = event.getListenerActor();
            
            // set the correct checked state
            if( actor == randomButton ) {
            	randomButton.setChecked(true);
            } else if( actor == northAmericaButton ) {
            	northAmericaButton.setChecked(true);
            } else if( actor == irelandButton ) {
            	irelandButton.setChecked(true);
            } else if( actor == japanButton ) {
            	japanButton.setChecked(true);
            } else {
                return;
            }

            Gdx.app.log( TerraNova.LOG, "Selecting map: " + mapButtons.getChecked().getName() );
        }
    }
    
    private class NationSelectionListener extends DefaultActorListener {
        @Override
        public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
            super.touchUp( event, x, y, pointer, button );
            //game.getSoundManager().play( TyrianSound.CLICK );

            Actor actor = event.getListenerActor();
            
            // set the correct button check state
            if( actor == englandButton ) {
            	englandButton.setChecked(true);
            } else if( actor == franceButton ) {
            	franceButton.setChecked(true);
            } else if( actor == spainButton ) {
            	spainButton.setChecked(true);
            } else if( actor == portugalButton ) {
            	portugalButton.setChecked(true);
            } else {
                return;
            }

            Gdx.app.log( TerraNova.LOG, "Selecting nation: " + nationButtons.getChecked().getName() );

        }
    }
    */
}

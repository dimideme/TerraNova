package com.demergis.terranova;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import javafx.stage.Stage;

public class SplashScreen implements Screen {

    private Stage stage;

    private Image splashImage;
	
	public SplashScreen( TerraNova game, SpriteBatch batch ) {
		// TODO Auto-generated constructor stub
	}

    @Override
    public void show() {

        // start playing the menu music
        //game.getMusicManager().play( TerraNovaMusic.MENU );

        // retrieve the splash image's region from the atlas
        //AtlasRegion splashRegion = getAtlas().findRegion( "splash-screen/splash-image" );
        //Drawable splashDrawable = new TextureRegionDrawable( splashRegion );

        // here we create the splash image actor; its size is set when the
        // resize() method gets called
        //splashImage = new Image( splashDrawable, Scaling.stretch );
        splashImage.setFillParent( true );

        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        splashImage.getColor().a = 0f;

        // configure the fade-in/out effect on the splash image
        splashImage.addAction( sequence( fadeIn( 0.75f ), delay( 0.25f ), fadeOut( 0.75f ),
            new Action() {
                @Override
                public boolean act(
                    float delta )
                {
                    // the last action will move to the next screen
                    //game.setScreen( new MenuScreen( game, batch ) );
                    return true;
                }
            } ) );

        // and finally we add the actor to the stage
        //stage.addActor( splashImage );
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

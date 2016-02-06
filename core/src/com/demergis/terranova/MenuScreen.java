package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {

    private Skin skin;
    private Stage stage;
    private TerraNova game;

    private int screenWidth, screenHeight;
    private int buttonWidth, buttonHeight;

    TextButton randomMapButton, northAmericaButton, japanButton, irelandButton, exitButton;


	public MenuScreen( TerraNova g ) {
        game = g;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        buttonWidth = screenWidth / 4;
        buttonHeight = screenHeight / 10;
        createBasicSkin();

        randomMapButton = new TextButton("Random Map", skin);
        randomMapButton.setPosition( screenWidth / 2 - buttonWidth / 2, screenHeight / 2 + 2.5f * buttonHeight);
        stage.addActor(randomMapButton);

        northAmericaButton = new TextButton("North America", skin);
        northAmericaButton.setPosition( screenWidth / 2 - buttonWidth / 2, screenHeight / 2 + buttonHeight / 2);
        stage.addActor(northAmericaButton);

        japanButton = new TextButton("Japan", skin);
        japanButton.setPosition( screenWidth / 2 - buttonWidth / 2, screenHeight / 2 - buttonHeight / 2);
        stage.addActor(japanButton);

        irelandButton = new TextButton("Ireland", skin);
        irelandButton.setPosition( screenWidth / 2 - buttonWidth / 2, screenHeight / 2 - 1.5f * buttonHeight );
        stage.addActor(irelandButton);

        exitButton = new TextButton("Exit", skin);
        exitButton.setPosition( screenWidth / 2 - buttonWidth / 2, screenHeight / 2 - 3.5f * buttonHeight );
        stage.addActor(exitButton);

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        randomMapButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MapScreen(game, "random"));
            }
        });
        northAmericaButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MapScreen(game, "NorthAmerica"));
            }
        });
        japanButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MapScreen(game, "Japan"));
            }
        });
        irelandButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MapScreen(game, "Ireland"));
            }
        });
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
	}

    private void createBasicSkin(){
        // Create a skin, add a font to skin
        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        // Create a basic button texture
        Pixmap pixmap = new Pixmap( buttonWidth, buttonHeight, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background",new Texture(pixmap));

        // Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

    }

    @Override
    public void show() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: show()" );
    }

    @Override
    public void render( float delta ) {
        //Gdx.app.log(TerraNova.LOG, "MenuScreen: render()");
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        //Table.drawDebug(stage);
    }

    @Override
    public void resize(int w, int h) {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: resize()" );
        stage.getCamera().viewportWidth = w;
        stage.getCamera().viewportHeight = h;
    }

    @Override
    public void hide() {
        Gdx.app.log( TerraNova.LOG, "MenuScreen: hide()" );
        // hide() is called when the screen changes to mapScreen
        // Unless the new screen sets a new ipnutprocessor, need to disable this inputprocessor!
        Gdx.input.setInputProcessor(null);
        // dispose of the screen if we don't plan to re-use it
        this.dispose();
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
        Gdx.app.log(TerraNova.LOG, "MenuScreen: dispose()");
        stage.dispose();
        skin.dispose();
    }

}

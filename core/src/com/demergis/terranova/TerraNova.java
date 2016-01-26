package com.demergis.terranova;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import javafx.stage.Stage;


public class TerraNova extends Game {

    public static String LOG = "LOG";
    public static boolean DEV_MODE = true;

    public SpriteBatch batch;
    public BitmapFont bitmapFont;

    public Stage stage;

    public World world;
    public  Player player;

    public static MapManager mapManager;
    public static TextManager textManager;
    public static TimeManager timeManager;

    public float screenWidth;
    public float screenHeight;

    public OrthographicCamera camera;
    public Box2DDebugRenderer b2dr;
    public Matrix4 debugCamMatrix;

    public int levelPixelWidth;
    public int levelPixelHeight;

	@Override
	public void create () {

        world = new World(new Vector2(0f, 0f ), false);
        player = new Player( true, "User", "France", new Texture("images\\blueShip.png"), world);


        List<Player> players = new ArrayList<Player>();
        players.add(new Player(true, "User", "France", new Texture("images\\blueShip.png"), world));
        players.add(new Player(false, "Computer", "England", new Texture("images\\redShip.png"), world));

        Gdx.app.log(TerraNova.LOG, "TerraNova: create(): Creating mapManager");
        mapManager = new MapManager();
        textManager = new TextManager();
        timeManager = new TimeManager();

        bitmapFont = new BitmapFont();
		batch = new SpriteBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.viewportWidth = screenWidth;
        camera.viewportHeight = screenHeight;
        camera.update();

        debugCamMatrix = new Matrix4(camera.combined);
        debugCamMatrix.scale(1f, 1f, 1f);
        b2dr = new Box2DDebugRenderer();

        TextManager.setSpriteBatch(batch);

        Gdx.app.log(TerraNova.LOG, "TerraNova: create(): Setting screen to new MapScreen");
        setScreen(new MapScreen(this) );
	}

	@Override
	public void render () {

        world.step(1f / 60f, 6, 2);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        InputManager.update();
        TimeManager.update();
        player.update();

        camera.position.x = Math.min(Math.max(player.getX(), screenWidth / 2), levelPixelWidth - (screenWidth / 2));
        camera.position.y = Math.min(Math.max(player.getY(), screenHeight / 2), levelPixelHeight - (screenHeight / 2));
        camera.update();


        b2dr.render(world, debugCamMatrix);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        player.draw(batch);
        TextManager.draw("FPS: " + Gdx.graphics.getFramesPerSecond() + ", timeRatio: " + TimeManager.timeRatio, camera, 10, 20);
        //TextManager.draw("X: " + player.getX() + ", Y: " + player.getY(), camera, 10, 20);
        batch.end();

        if ( Gdx.input.isKeyPressed(Input.Keys.ESCAPE) ) Gdx.app.exit();

	}

    public MapManager getMapManager() {
        return mapManager;
    }

    @Override
    public void resize( int width, int height ) {
        this.screenWidth = width;
        this.screenHeight = height;

        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override
    public void pause() {
        super.pause();
        Gdx.app.log( TerraNova.LOG, "TerraNova: pause(): Pausing game" );
    }

    @Override
    public void resume() {
        super.resume();
        Gdx.app.log(TerraNova.LOG, "TerraNova: resume(): Resuming game");
    }

    @Override
    public void setScreen( Screen screen ) {
        super.setScreen( screen );
        Gdx.app.log( TerraNova.LOG, "TerraNova: setScreen(): Setting screen: " + screen.getClass().getSimpleName() );
    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.log(TerraNova.LOG, "TerraNova: dispose(): Disposing game" );

        world.dispose();
        mapManager.dispose();

    }
}

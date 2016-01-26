package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MapScreen implements Screen {

	private final ContinentMap map;


    private Stage stage;
	private final OrthographicCamera cam;

	private Mesh mesh;
	private ShaderProgram shader;
    private SpriteBatch batch;

	public static final int POSITION_COMPONENTS = 3;				//Position attribute - (x, y, z)
	public static final int COLOR_COMPONENTS = 4;					//Color attribute - (r, g, b, a)
	public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS; 	//Total number of components for all attributes
	public static final int PRIMITIVE_SIZE = 3 * NUM_COMPONENTS;  	//The "size" (total number of floats) for a single triangle
	public static final int MAX_TRIS = 500000;  						//The maximum number of triangles our mesh will hold
	public static final int MAX_VERTS = MAX_TRIS * 3;  				//The maximum number of vertices our mesh will hold
	protected float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];  	//The array which holds all the data, interleaved like so:

    //private TextButton exploreButton;
    //private ButtonSelectionListener buttonSelectionListener;
	
	public static final String VERT_SHADER =  
			"attribute vec3 a_position;\n" +
			"attribute vec4 a_color;\n" +			
			"uniform mat4 u_projTrans;\n" + 
			"varying vec4 vColor;\n" +			
			"void main() {\n" +  
			"	vColor = a_color;\n" +
			"	gl_Position =  u_projTrans * vec4(a_position, 1.0);\n" +
			"}";
	
	public static final String FRAG_SHADER = 
            "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
			"varying vec4 vColor;\n" + 			
			"void main() {\n" +  
			"	gl_FragColor = vColor;\n" + 
			"}";
	
	protected static ShaderProgram createMeshShader() {
		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
		String log = shader.getLog();
		if (!shader.isCompiled())
			throw new GdxRuntimeException(log);		
		if (log!=null && log.length()!=0)
			System.out.println("Shader Log: "+log);
		return shader;
	}
	
	public MapScreen( TerraNova game ) {
		
        Gdx.app.log( TerraNova.LOG, "MapScreen: MapScreen()" );

        this.batch = game.batch;
		
		map = game.getMapManager().createMap( "random" );
		
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, w, h);
        //cam.near = 2000f;
        //cam.far = 0f;
        //cam.zoom = 1f;
        cam.position.set(0f, 0f, 100f);
        cam.lookAt(0f, 0f, 0f);
        
        cam.update();

        FitViewport viewp = new FitViewport(w, h, cam);
        stage = new Stage(viewp, batch);
        
	    mesh = new Mesh(true, MAX_VERTS, 0,
	            new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
	            new VertexAttribute(Usage.ColorPacked, COLOR_COMPONENTS, "a_color"));
	    
	    shader = createMeshShader();
        
	}

    @Override
    public void show() {
        Gdx.app.log(TerraNova.LOG, "MapScreen: show()");

        /*
        // Add UI buttons
        Table table = super.getTable();
        table.defaults().spaceBottom( 20 );
        table.row();
        
        exploreButton = new TextButton( "Explore", getSkin() );
        exploreButton.addListener( buttonSelectionListener );
        exploreButton.setName("explore");
        table.add( exploreButton ).fillX().padRight(10);
        
        TextButton exploreButton = new TextButton( "Explore", getSkin() );
        exploreButton.addListener( new DefaultActorListener() {
            public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
                //game.getSoundManager().play( TyrianSound.CLICK );
            	Actor actor = event.getListenerActor();
                Gdx.app.log( TerraNova.LOG, "Selecting: " + actor.getName() );
            }
        } );
        table.row();
        table.add( exploreButton );
        */

        // play the level music
        //game.getMusicManager().play( TerraNovaMusic.LEVEL );

        // create the ship and add it to the stage
        //ship2d = Ship2D.create( profile.getShip(), getAtlas() );

        // center the ship horizontally
        //ship2d.setInitialPosition( ( stage.getWidth() / 2 - ship2d.getWidth() / 2 ), ship2d.getHeight() );

        // add the ship to the stage
        //stage.addActor( ship2d );

        // add a fade-in effect to the whole stage
        //stage.getRoot().getColor().a = 0f;
        //stage.getRoot().addAction( Actions.fadeIn( 0.5f ) );
    }
    
    @Override
    public void resize( int w, int h ) {
        Gdx.app.log(TerraNova.LOG, "MapScreen: resize()");

        cam.setToOrtho(false, w, h);
        cam.update();
    }
    
    @Override
    public void render( float delta ) {
        Gdx.app.log( TerraNova.LOG, "MapScreen: render()" );
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        Gdx.gl.glViewport(0, 0, (int) Gdx.graphics.getWidth(), (int) Gdx.graphics.getWidth());
        
        // update the state of all actors on stage
        stage.act( delta );
        
        // update the camera
        updateCamera();
        
        // draw the map
        renderMap();
        
        // draw the stage actors
        stage.draw();
        
    }

	private void updateCamera() {
		
		// update the camera position
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
        	cam.translate(-2.0f, 0.0f, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
        	cam.translate(2.0f, 0.0f, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
        	cam.translate(0.0f, 2.0f, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
        	cam.translate(0.0f, -2.0f, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
        	cam.translate(0.0f, 0.0f, 2.0f);
            Gdx.app.log( TerraNova.LOG, "MapScreen: updateCamera(): zoom = " + cam.position.z );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
        	cam.translate(0.0f, 0.0f, -2.0f);
            Gdx.app.log( TerraNova.LOG, "MapScreen: updateCamera(): zoom = " + cam.position.z );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
        	cam.rotateAround(cam.position, new Vector3(0,0,1), (float)(-30*Math.PI/360.) );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
        	cam.rotateAround(cam.position, new Vector3(0,0,1), (float)(30*Math.PI/360.) );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.C)){
        	cam.zoom*=1.05;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.V)){
        	cam.zoom/=1.05;
        }
        cam.update();
	}
    
    private void renderMap() {
        Gdx.app.log( TerraNova.LOG, "MapScreen: renderMap()" );
		//if we've already flushed
		if ( map.getVertexCount() == 0) {
	        Gdx.app.log( TerraNova.LOG, "MapScreen: already flushed" );
			return;
		}
		
		//sends our vertex data to the mesh
		mesh.setVertices(map.getVerts());
		
		//no need for depth...
		Gdx.gl.glDepthMask(false);
		
		//enable blending, for alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
	    //start the shader before setting any uniforms
	    shader.begin();

	    //update the projection matrix so our triangles are rendered in 2D
	    shader.setUniformMatrix("u_projTrans", cam.combined);
		
		//render the mesh
		mesh.render(shader, GL20.GL_TRIANGLES, 0, map.getVertexCount() );
		
		shader.end();
		
		//re-enable depth to reset states to their default
		Gdx.gl.glDepthMask(true);
    }

    /*
    private class ButtonSelectionListener extends DefaultActorListener {
        @Override
        public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
            super.touchUp( event, x, y, pointer, button );
            //game.getSoundManager().play( TyrianSound.CLICK );

            Actor actor = event.getListenerActor();
            
            // set the correct button check state
            if( actor == exploreButton ) {
            	exploreButton.setChecked(true);
            } else {
                return;
            }

            Gdx.app.log( TerraNova.LOG, "Selecting Button: " + actor.getName() );

        }
    }
    */

    @Override
    public void hide() {
        Gdx.app.log( TerraNova.LOG, "MapScreen: hide()" );
    }

    @Override
    public void pause() {
        Gdx.app.log( TerraNova.LOG, "MapScreen: pause()" );
    }

    @Override
    public void resume() {
        Gdx.app.log( TerraNova.LOG, "MapScreen: resume()" );
    }

    @Override
    public void dispose() {
        Gdx.app.log( TerraNova.LOG, "MapScreen: dispose()" );
    }

}

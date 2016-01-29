package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MapScreen implements Screen {

    private final TerraNova game;

	private final ContinentMap map;

	private OrthographicCamera camera;
    private Viewport viewport;

	private Mesh mesh;
	private ShaderProgram shader;

    private int screenWidth;
    private int screenHeight;

    private float worldMinX = -1000f;
    private float worldMaxX = 1000f;
    private float worldMinY = -1000f;
    private float worldMaxY = 1000f;

	public static final int POSITION_COMPONENTS = 3;				    //Position attribute - (x, y, z)
	public static final int COLOR_COMPONENTS = 4;					    //Color attribute - (r, g, b, a)
	public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS; 	//Total number of components for all attributes
	public static final int PRIMITIVE_SIZE = 3 * NUM_COMPONENTS;  	    //The "size" (total number of floats) for a single triangle
	public static final int MAX_TRIS = 500000;  						//The maximum number of triangles our mesh will hold
	public static final int MAX_VERTS = MAX_TRIS * 3;  				        //The maximum number of vertices our mesh will hold
	public float[] vertexData = new float[MAX_VERTS * NUM_COMPONENTS];  	//The array which holds all the data, interleaved like so:
                                                                            // (x1, y1, z1, r1, g1, b1, a1, x2, y2, z2...)

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
	
	public MapScreen( TerraNova g ) {
		
        Gdx.app.log( TerraNova.LOG, "MapScreen: MapScreen()" );

        this.game = g;
		
		map = TerraNova.mapManager.createMap("NorthAmerica");
        vertexData = map.getVerts();
		
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        camera = new OrthographicCamera( );
        camera.viewportWidth = ( worldMaxX - worldMinX ) / 2;
        camera.viewportHeight = camera.viewportWidth * screenHeight / screenWidth;
        camera.near = 0;
        camera.far = 1000f;
        camera.position.set( 0f , 0f, 1000f);
        camera.update();
        
	    mesh = new Mesh(true, MAX_VERTS, 0,
	            new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
	            new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));

        //sends our vertex data to the mesh
        mesh.setVertices( vertexData );
	    
	    shader = createMeshShader();
        
	}
    
    @Override
    public void render( float delta ) {
        //Gdx.app.log( TerraNova.LOG, "MapScreen: render()" );

        // Initialize gl graphics
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
        
        // Update the camera
        updateCamera();
        
        // Draw the map
        renderMap();

        // Draw UI elements on top
        Matrix4 uiMatrix = camera.combined.cpy();
        uiMatrix.setToOrtho2D(0, 0, screenWidth, screenHeight);
        game.batch.setProjectionMatrix(uiMatrix);
        game.batch.begin();

        game.bitmapFont.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + ", timeRatio: " + TimeManager.timeRatio, 20, 100);
        game.bitmapFont.draw(game.batch, "AWSD to move camera, ER rotate, ZQ forward/back, CV zoom in/out, HLJK change field of view", 20, 80);
        game.bitmapFont.draw( game.batch, "Camera Position: " + camera.position.x + ", " + camera.position.y + ", " + camera.position.z, 20, 60);
        game.bitmapFont.draw( game.batch, "Camera Params: Near: " + camera.near + ", Far: " + camera.far + ", Zoom: " + camera.zoom, 20, 40);
        game.bitmapFont.draw( game.batch, "Camera Viewport: Width: " + camera.viewportWidth + ", Height: " + camera.viewportHeight, 20, 20);
        game.batch.end();
        
    }

    private void renderMap() {
        //Gdx.app.log( TerraNova.LOG, "MapScreen: renderMap()" );

        // if we've already flushed
        if ( map.getVertexCount() == 0) {
            Gdx.app.log( TerraNova.LOG, "MapScreen: already flushed" );
            return;
        }

        // no need for depth...
        Gdx.gl.glDepthMask(false);

        // enable blending, for alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // start the shader before setting any uniforms
        shader.begin();

        // update the projection matrix so our triangles are rendered in 2D
        shader.setUniformMatrix("u_projTrans", camera.combined);

        // render the mesh
        mesh.render(shader, GL20.GL_TRIANGLES, 0, map.getVertexCount() );

        shader.end();

        // re-enable depth to reset states to their default
        Gdx.gl.glDepthMask(true);
    }

	private void updateCamera() {

		// update the camera position
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.translate(-5.0f, 0.0f, 0.0f);
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera position: " + camera.position.x + ", " + camera.position.y + ", " + camera.position.z );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.translate(5.0f, 0.0f, 0.0f);
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera position: " + camera.position.x + ", " + camera.position.y + ", " + camera.position.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.translate(0.0f, 5.0f, 0.0f);
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera position: " + camera.position.x + ", " + camera.position.y + ", " + camera.position.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.translate(0.0f, -5.0f, 0.0f);
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera position: " + camera.position.x + ", " + camera.position.y + ", " + camera.position.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
            camera.translate(0.0f, 0.0f, 2.0f);
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera position: " + camera.position.x + ", " + camera.position.y + ", " + camera.position.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            camera.translate(0.0f, 0.0f, -2.0f);
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera position: " + camera.position.x + ", " + camera.position.y + ", " + camera.position.z);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.H)){
            camera.viewportWidth--;
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera viewportWidth: " + camera.viewportWidth);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.L)){
            camera.viewportWidth++;
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera viewportWidth: " + camera.viewportWidth);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            camera.viewportHeight--;
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera viewportHeight: " + camera.viewportHeight);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)){
            camera.viewportHeight++;
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new camera viewportHeight: " + camera.viewportHeight);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            camera.rotateAround(camera.position, new Vector3(0,0,1), (float)(-30*Math.PI/360.) );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            camera.rotateAround(camera.position, new Vector3(0,0,1), (float)(30*Math.PI/360.) );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.C)){
            camera.zoom*=1.05;
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new zoom: " + camera.zoom);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.V)){
            camera.zoom/=1.05;
            //Gdx.app.log(TerraNova.LOG, "MapScreen: updateCamera(): new zoom: " + camera.zoom);
        }

        // Clamp camera zoom level to ensure not too far zoomed out or in
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, (worldMaxX - worldMinX)/camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        // Clamp camera position in order to stay within world borders
        camera.position.x = MathUtils.clamp(camera.position.x, worldMinX + effectiveViewportWidth / 2f, worldMaxX - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, worldMinY + effectiveViewportHeight / 2f, worldMaxY - effectiveViewportHeight / 2f);

        camera.update();
	}

    @Override
    public void show() {
        Gdx.app.log(TerraNova.LOG, "MapScreen: show()");
    }

    @Override
    public void resize( int w, int h ) {
        Gdx.app.log(TerraNova.LOG, "MapScreen: resize()");
        screenWidth = w;
        screenHeight = h;
        camera.viewportWidth = ( worldMaxX - worldMinX ) / 2;
        camera.viewportHeight = camera.viewportWidth * h / w;
        camera.update();
    }

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

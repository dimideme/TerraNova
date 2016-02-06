package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Dimitri on 2/4/2016.
 */
public class Ship extends Actor {

    private MapScreen mapScreen;

    private Texture texture;

    private Body body;
    private BodyDef bodyDef;

    public Vector2 keyForce;
    public Vector2 inertiaForce;

    public Ship( MapScreen mapScreen, Vector2 worldPosition, String nation ) {
        Gdx.app.log( TerraNova.LOG, "Ship()" );

        this.mapScreen = mapScreen;

        texture = new Texture( new FileHandle( "images/blueShip.png") );
        setBounds( getX(), getY(), texture.getWidth(), texture.getHeight() );

        if( nation.equals("France") ) setColor( Color.BLUE );
        if( nation.equals("England") ) setColor( Color.RED );

        float width = 10.0f;
        float height = 5.0f;

        float density = 0.01f;
        float friction = 0f;
        float restitution = 1f;

        createBody( worldPosition, width, height, density, friction, restitution );

    }

    protected void createBody(Vector2 position, float width, float height, float density, float friction, float restitution ){

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.linearDamping = 0.8f;
        bodyDef.angularDamping = 0.8f;
        body = mapScreen.getWorld().createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void act( float delta ) {
        //Gdx.app.log( TerraNova.LOG, "Ship: act()" );

        keyForce = new Vector2( 0f, 0f );

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            keyForce.x -=100f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            keyForce.x +=100f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            keyForce.y -=100f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            keyForce.y +=100f;
        }

        //inertiaForce = new Vector2( -100 * body.getLinearVelocity().x, -100 * body.getLinearVelocity().y );
        body.applyForceToCenter( keyForce, true );

        // This takes the body's position (world coordinates) and converts it to screen coordinates
        Vector3 projectedPosition = mapScreen.getCamera().project(new Vector3(body.getPosition(), 0f));

        // Update the actor's position (screen coordinates)
        setPosition(projectedPosition.x, projectedPosition.y);

    }

    @Override
    public void draw ( Batch batch, float parentAlpha ) {
        //Gdx.app.log( TerraNova.LOG, "Ship: draw()" );

        batch.draw( texture, getX(), getY() );
    }

}

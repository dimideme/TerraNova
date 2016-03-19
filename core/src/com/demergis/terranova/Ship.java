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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Dimitri on 2/4/2016.
 */
public class Ship extends Actor {

    private MapScreen mapScreen;

    private Texture texture;

    private Body body;
    private BodyDef bodyDef;

    public float speed;
    public float turnRate;

    public Vector2 keyForce;
    public Vector2 click;

    public Vector3 velocity;
    public Vector3 destination;
    public float course;
    public float heading;

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

        createBody(worldPosition, width, height, density, friction, restitution);

        speed = 100f;
        turnRate = 1f;
        destination = new Vector3( body.getPosition(), 0f );
        velocity = new Vector3( 0f, 0f, 0f );
    }

    protected void createBody( Vector2 position, float width, float height, float density, float friction, float restitution ){

        FixtureDef fixtureDef;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.angle = (float)Math.PI/2f;
        bodyDef.linearDamping = 0.8f;
        bodyDef.angularDamping = 0.8f;
        body = mapScreen.getWorld().createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( width / 2f, height / 2f );

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        body.createFixture( fixtureDef );

        PolygonShape bow = new PolygonShape();
        Vector2 vert1 = new Vector2( -width / 2f, -height / 2f );
        Vector2 vert2 = new Vector2( -width / 2f, height / 2f );
        Vector2 vert3 = new Vector2( -width, 0f );
        Vector2[] bowVerts = { vert1, vert2, vert3 };
        bow.set(bowVerts);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = bow;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        body.createFixture(fixtureDef);


        shape.dispose();
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void act( float delta ) {
        //Gdx.app.log( TerraNova.LOG, "Ship: act()" );

        keyForce = new Vector2( 0f, 0f );
        click = new Vector2( 0f, 0f );

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
        if( Gdx.input.isTouched() ) {
            click.x = Gdx.input.getX();
            click.y = Gdx.input.getY();
            destination = mapScreen.getCamera().unproject(new Vector3(click.x, click.y, 0));
            destination = new Vector3( destination.x, destination.y, 0 );
            Gdx.app.log(TerraNova.LOG, "new destination = " + destination);
        }

        velocity = destination.cpy();
        velocity.sub( body.getPosition().x, body.getPosition().y, 0f ).scl(speed);
        Gdx.app.log(TerraNova.LOG, "ship position = " + body.getPosition() );
        Gdx.app.log(TerraNova.LOG, "ship destination = " + destination);
        Gdx.app.log(TerraNova.LOG, "ship velocity = " + velocity);

        course = (float)Math.toDegrees(Math.atan2( velocity.x, velocity.y ));
        heading = (float)(-body.getAngle()*180f/Math.PI + 270f);

        //body.applyForceToCenter( keyForce, true );
        body.applyForceToCenter( velocity.x, velocity.y, true );
        if( heading - course > 1f ) {
            body.applyAngularImpulse( turnRate, true );
        } else if ( heading - course < -1f ) {
            body.applyAngularImpulse( -turnRate, true );
        } else {
            body.applyAngularImpulse( -body.getAngularVelocity(), true );
        }

        // This takes the body's position (world coordinates) and converts it to screen coordinates
        Vector3 projectedPosition = mapScreen.getCamera().project(new Vector3(body.getPosition(), 0f));

        // Update the actor's position (screen coordinates)
        setPosition( projectedPosition.x, projectedPosition.y );

    }

    @Override
    public void draw ( Batch batch, float parentAlpha ) {
        //Gdx.app.log( TerraNova.LOG, "Ship: draw()" );

        batch.draw( texture, getX(), getY() );
    }

}

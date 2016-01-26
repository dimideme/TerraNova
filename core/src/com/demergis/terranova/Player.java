package com.demergis.terranova;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Dimitri on 1/23/2016.
 */
public class Player extends Sprite {

    private Body shipBody;
    private boolean isHuman;
    private String playerName;
    private String countryName;

    public Player(boolean isHuman, String playerName, String countryName, Texture texture, World world) {

        super(texture);
        setTexture(texture);
        setPosition(500f, 0f);

        this.isHuman = isHuman;
        this.playerName = playerName;
        this.countryName = countryName;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set( 500f, 0f);
        bodyDef.fixedRotation = true;

        shipBody = world.createBody( bodyDef );

        PolygonShape box = new PolygonShape();
        box.setAsBox(1f, 1f, new Vector2(0f, 0f), 0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1f;
        shipBody.createFixture(fixtureDef);

        box.dispose();
    }

    public void update() {

        shipBody.applyForceToCenter(InputManager.keyForce, true);

        setPosition( shipBody.getPosition().x / 100, shipBody.getPosition().y / 100 );

        Gdx.app.log("", "Body:   x = " + shipBody.getPosition().x + ", y = " + shipBody.getPosition().y);
        Gdx.app.log("", "Player: x = " + getX() + ", y = " + getY() + ", w = " + getWidth() + ", h = " + getHeight() );

    }

    @Override
    public void draw(Batch batch) {
        batch.draw(this.getTexture(), this.getX(), this.getY());
    }

}

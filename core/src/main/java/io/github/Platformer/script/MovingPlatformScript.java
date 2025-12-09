package io.github.Platformer.script;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.physics.PhysicsContact;
import games.rednblack.editor.renderer.scripts.BasicScript;
import games.rednblack.editor.renderer.utils.ItemWrapper;

public class MovingPlatformScript extends BasicScript implements PhysicsContact {
    private ComponentMapper<TransformComponent> transformMapper;
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<MainItemComponent> mainItemMapper;

    private PhysicsBodyComponent mPhysicsBodyComponent;
    private TransformComponent transformComponent;


    public float speedY = 2;
    public float minY = 0;
    public float maxY = 500;

    private boolean movingUp = true;

    private final Vector2 velocity = new Vector2();

    @Override
    public void init(int item) {
        super.init(item);

        mPhysicsBodyComponent = physicsMapper.get(item);
        transformComponent = transformMapper.get(item);

        mPhysicsBodyComponent.body.setGravityScale(0f);
        mPhysicsBodyComponent.body.setFixedRotation(true);


        if (minY == 0 && maxY == 500) {
            minY = transformComponent.y;
            maxY = transformComponent.y + 300;
        }
    }

    @Override
    public void act(float delta) {
        Body body = mPhysicsBodyComponent.body;
        float currentY = transformComponent.y;

        if (speedY != 0) {
            if (movingUp) {
                if (currentY >= maxY) {
                    movingUp = false;
                }
            } else {
                if (currentY <= minY) {
                    movingUp = true;
                }
            }
            velocity.y = movingUp ? speedY : -speedY;
        } else {
            velocity.y = 0;
        }

        body.setLinearVelocity(velocity);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void beginContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
    }

    @Override
    public void endContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
    }

    @Override
    public void preSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        MainItemComponent mainItemComponent = mainItemMapper.get(contactEntity);

        if (mainItemComponent != null && mainItemComponent.tags.contains("player")) {
            Vector2 normal = contact.getWorldManifold().getNormal();
            if (Math.abs(normal.y) > 0.7f) {
                contact.setFriction(1.0f); // chtob чел не соскользнул,бустим трение для платформы(вроде работает)
            }
        }
    }

    @Override
    public void postSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
    }
}

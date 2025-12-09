package io.github.Platformer.script;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.physics.PhysicsContact;
import games.rednblack.editor.renderer.scripts.BasicScript;
import games.rednblack.editor.renderer.utils.ItemWrapper;
import io.github.Platformer.component.FlyEnemyComponent;

public class FlyEnemyScript extends BasicScript implements PhysicsContact {


    protected ComponentMapper<FlyEnemyComponent> flyEnemyMapper;
    protected ComponentMapper<MainItemComponent> mainItemMapper;

    protected com.artemis.World mEngine;
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    private PhysicsBodyComponent mPhysicsBodyComponent;
    private int animEntity;


    private final Vector2 impulse = new Vector2(0, 0);
    private final Vector2 speed = new Vector2(0, 0);

    public static final int LEFT = 1;
    public static final int RIGHT = -1;

    @Override
    public void init(int item) {
        super.init(item);

        ItemWrapper itemWrapper = new ItemWrapper(item, mEngine);
        animEntity = itemWrapper.getChild("flyenemy-anim").getEntity();

        mPhysicsBodyComponent = physicsMapper.get(item);

        mPhysicsBodyComponent.body.setFixedRotation(true);
    }

    @Override
    public void act(float delta) {
        Body body = mPhysicsBodyComponent.body;
        FlyEnemyComponent flyEnemyComponent = flyEnemyMapper.get(animEntity);

        if (flyEnemyComponent.touch == 0) {
            moveEnemy(LEFT);
        } else {
            moveEnemy(RIGHT);
        }
    }

    public void moveEnemy(int direction) {
        Body body = mPhysicsBodyComponent.body;
        speed.set(body.getLinearVelocity());

        switch (direction) {
            case LEFT:
                impulse.set(-8000, speed.y);
                break;
            case RIGHT:
                impulse.set(8000, speed.y);
                break;
        }

        body.applyLinearImpulse(impulse.sub(speed), body.getWorldCenter(), true);
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
        FlyEnemyComponent flyEnemyComponent = flyEnemyMapper.get(animEntity);

        if (flyEnemyComponent.touch == 0) {
            moveEnemy(LEFT);
            if (mainItemComponent.tags.contains("left_target")) {
                flyEnemyComponent.touch = 1;
            }
        } else {
            moveEnemy(RIGHT);
            if (mainItemComponent.tags.contains("right_target")) {
                flyEnemyComponent.touch = 0;
            }
        }
    }

    @Override
    public void postSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }

    @Override
    public void dispose() {

    }
}

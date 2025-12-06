package io.github.Platformer.script;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.physics.PhysicsContact;
import games.rednblack.editor.renderer.scripts.BasicScript;
import games.rednblack.editor.renderer.utils.ItemWrapper;
import io.github.Platformer.audio.AudioService;
import io.github.Platformer.component.CoinComponent;
import io.github.Platformer.component.PlayerComponent;
import io.github.Platformer.component.SkeletonComponent;
import io.github.Platformer.component.SpikeComponent;

public class EnemyScript extends BasicScript implements PhysicsContact {


    protected ComponentMapper<SkeletonComponent> skeletonMapper;
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
        animEntity = itemWrapper.getChild("skeleton-anim").getEntity();

        mPhysicsBodyComponent = physicsMapper.get(item);

        mPhysicsBodyComponent.body.setFixedRotation(true);
    }

    @Override
    public void act(float delta) {
        Body body = mPhysicsBodyComponent.body;
        SkeletonComponent skeletonComponent = skeletonMapper.get(animEntity);

        if (skeletonComponent.touch == 0) {
            moveEnemy(LEFT);
        } else {
            moveEnemy(RIGHT);
        }

        body.setGravityScale(9.83f);
    }

    public void moveEnemy(int direction) {
        Body body = mPhysicsBodyComponent.body;
        speed.set(body.getLinearVelocity());

        switch (direction) {
            case LEFT:
                impulse.set(-10000, speed.y);
                break;
            case RIGHT:
                impulse.set(10000, speed.y);
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
        Vector2 normal = contact.getWorldManifold().getNormal();
        MainItemComponent mainItemComponent = mainItemMapper.get(contactEntity);
        SkeletonComponent skeletonComponent = skeletonMapper.get(animEntity);

        // через нормал чекаем направление с которым плеер столкнулся и выдаём нужное трение для неё
        if (Math.abs(normal.x) > 0.7f) { // тут понимаемЮчто это стена и выдаём маленькое трение чтоб чел упал
            contact.setFriction(0.1f);
        } else if (Math.abs(normal.y) > 0.7f) { // а тут понимаем,что это пол и даём нормальное трение
            contact.setFriction(1);
        } else {
            contact.setFriction(0);
        }

        if (skeletonComponent.touch == 0) {
            moveEnemy(LEFT);
            if (mainItemComponent.tags.contains("left_target")) {
                skeletonComponent.touch = 1;
            }
        } else {
            moveEnemy(RIGHT);
            if (mainItemComponent.tags.contains("right_target")) {
                skeletonComponent.touch = 0;
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

package io.github.Platformer.script;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import io.github.Platformer.component.PlayerComponent;

public class PlayerScript extends BasicScript implements PhysicsContact {

    protected com.artemis.World mEngine;
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<TransformComponent> transformMapper;
    protected ComponentMapper<PlayerComponent> playerMapper;
    protected ComponentMapper<MainItemComponent> mainItemMapper;
    protected ComponentMapper<DimensionsComponent> dimensionsMapper;

    public static final int LEFT = 1;
    public static final int RIGHT = -1;
    public static final int JUMP = 0;


    public static final int JUMP_RIGHT = 3;
    public static final int JUMP_LEFT = 2;

    private int animEntity;
    private PhysicsBodyComponent mPhysicsBodyComponent;

    private final Vector2 impulse = new Vector2(0, 0);
    private final Vector2 speed = new Vector2(0, 0);

    @Override
    public void init(int item) {
        super.init(item);

        ItemWrapper itemWrapper = new ItemWrapper(item, mEngine);
        animEntity = itemWrapper.getChild("player-anim").getEntity();

        mPhysicsBodyComponent = physicsMapper.get(item);

    }

    @Override
    public void act(float delta) {
        Body body = mPhysicsBodyComponent.body;
        speed.set(body.getLinearVelocity());
        PlayerComponent playerComponent = playerMapper.get(animEntity);

        body.setGravityScale(9.83f); // команда задаёт гравитацию

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (body.getLinearVelocity().y >= 0 && playerComponent.touchedPlatforms != 1){
                movePlayer(JUMP_LEFT);
            } else {
                movePlayer(LEFT);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (body.getLinearVelocity().y >= 0 && playerComponent.touchedPlatforms != 1){
                movePlayer(JUMP_RIGHT);
            }else {
                movePlayer(RIGHT);
            }
        } else{
            body.setLinearVelocity(0, speed.y);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && playerComponent.touchedPlatforms != 0) { // нужна сделать проверку именно на напольные платформы,а то он даёт прыгнуть и от стен
            movePlayer(JUMP);
        }


    }

    public void movePlayer(int direction) {
        Body body = mPhysicsBodyComponent.body;
        speed.set(body.getLinearVelocity());
        switch (direction) {
            case JUMP_RIGHT: //задаём разную скорость в воздухе и на земле,а то странно это выглядело
                impulse.set(1200, speed.y);
                break;
            case JUMP_LEFT:
                impulse.set(-1200, speed.y);
                break;
            case LEFT:
                impulse.set(-5000, speed.y);
                break;
            case RIGHT:
                impulse.set(5000, speed.y);
                break;
            case JUMP:
                impulse.set(speed.x,150000);
                break;
        }

        body.applyLinearImpulse(impulse.sub(speed), body.getWorldCenter(), true);
    }

    public PlayerComponent getPlayerComponent() {
        return playerMapper.get(animEntity);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void beginContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        MainItemComponent mainItemComponent = mainItemMapper.get(contactEntity);

        PlayerComponent playerComponent = playerMapper.get(animEntity);
        if (mainItemComponent.tags.contains("ground")){
            playerComponent.touchedPlatforms = 1;
        }
        System.out.println(playerComponent.touchedPlatforms);
    }

    @Override
    public void endContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        PlayerComponent playerComponent = playerMapper.get(animEntity);
        playerComponent.touchedPlatforms = 0;
    }


    // помогает персонажу не улететь в небеса,грубо говоря добавляет трение
    @Override
    public void preSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        TransformComponent transformComponent = transformMapper.get(this.entity);

        TransformComponent colliderTransform = transformMapper.get(contactEntity);
        DimensionsComponent colliderDimension = dimensionsMapper.get(contactEntity);

        if (transformComponent.y < colliderTransform.y + colliderDimension.height) {
            contact.setFriction(0);
        } else {
            contact.setFriction(1);
        }
    }

    @Override
    public void postSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }
}

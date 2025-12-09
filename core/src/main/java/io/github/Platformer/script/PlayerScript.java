package io.github.Platformer.script;

import com.artemis.ComponentMapper;
import com.artemis.World;
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
import io.github.Platformer.audio.AudioService;
import io.github.Platformer.component.CoinComponent;
import io.github.Platformer.component.PlayerComponent;
import io.github.Platformer.component.SkeletonComponent;
import io.github.Platformer.component.SpikeComponent;

public class PlayerScript extends BasicScript implements PhysicsContact {
    protected World mEngine;
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<PlayerComponent> playerMapper;
    protected ComponentMapper<MainItemComponent> mainItemMapper;
    protected ComponentMapper<CoinComponent> coinMapper;
    protected ComponentMapper<SpikeComponent> spikeMapper;
    protected AudioService audioService;

    public static final int LEFT = 1;
    public static final int RIGHT = -1;
    public static final int JUMP = 0;


    public static final int JUMP_RIGHT = 3;
    public static final int JUMP_LEFT = 2;

    public static final int ATTACK = 5;

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

        mPhysicsBodyComponent.body.setFixedRotation(true);
    }

    @Override
    public void act(float delta) {
        Body body = mPhysicsBodyComponent.body;
        speed.set(body.getLinearVelocity());
        PlayerComponent playerComponent = playerMapper.get(animEntity);

        body.setGravityScale(13f);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (playerComponent.touchedPlatforms != 1){
                movePlayer(JUMP_LEFT);
            } else {
                movePlayer(LEFT);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (playerComponent.touchedPlatforms != 1){
                movePlayer(JUMP_RIGHT);
            }else {
                movePlayer(RIGHT);
            }
        }
        else{
            body.setLinearVelocity(0, speed.y);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && playerComponent.touchedPlatforms != 0) { // нужна сделать проверку именно на напольные платформы,а то он даёт прыгнуть и от стен
            movePlayer(JUMP);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)){ // атакуем на S dada
            movePlayer(ATTACK);
        }
        else {
            playerComponent.attacking=false; // если не атакуем, то ставим attacking false
        }

    }

    public void movePlayer(int direction) {
        Body body = mPhysicsBodyComponent.body;
        speed.set(body.getLinearVelocity());
        audioService = new AudioService("jump.mp3");

        switch (direction) {
            case JUMP_RIGHT: //задаём разную скорость в воздухе и на земле,а то странно это выглядело
                impulse.set(24000, speed.y);
                break;
            case JUMP_LEFT:
                impulse.set(-24000, speed.y);
                break;
            case LEFT:
                impulse.set(-50000, speed.y);
//                PlayerComponent playerComponent = playerMapper.get(animEntity);
//                System.out.println(playerComponent.coinsCollected); // тупо чекал сбор монеток
                break;
            case RIGHT:
                impulse.set(50000, speed.y);
//                PlayerComponent pplayerComponent = playerMapper.get(animEntity);
//                System.out.println(pplayerComponent.coinsCollected); // тупо чекал сбор монеток
                break;
            case JUMP:
                audioService.play();
                impulse.set(speed.x,40000000);
                break;
            case ATTACK:
                PlayerComponent playerComponent = playerMapper.get(animEntity);
                playerComponent.attacking=true;
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

        audioService = new AudioService("coin.mp3");
        CoinComponent coinComponent = coinMapper.get(contactEntity);
        if (coinComponent != null) {
            playerComponent.coinsCollected += coinComponent.value; //+денежки когда косаешся монеты
            mEngine.delete(contactEntity);  //удаляет монетку при контакте с ней
            audioService.play();
        }

        if (mainItemComponent.tags.contains("platform")) {
            Vector2 normal = contact.getWorldManifold().getNormal();
            if (Math.abs(normal.y) > 0.7f) { // проверка,чтоб если чел ударился моделькой об бок платформы ему не давало возможность прыгнуть ещё раз
                playerComponent.touchedPlatforms = 1;
            }
        }

        SpikeComponent spikeComponent = spikeMapper.get(contactEntity);
        if (mainItemComponent.tags.contains("spike")){
            Vector2 normal = contact.getWorldManifold().getNormal();
            if (Math.abs(normal.y) > 0.7f) { // проверка,чтоб если чел ударился моделькой об бок платформы ему не давало возможность прыгнуть ещё раз
                playerComponent.hp -= spikeComponent.value; //отнимает хп когда касаешся шипов
            }
        }

        if (mainItemComponent.tags.contains("skeleton") && playerComponent.attacking==false){ //проверяем, атакует ли игрок в момент контакта, тут не атакует и дохнет
            playerComponent.hp -= 1;
        }
        else if (mainItemComponent.tags.contains("skeleton") && playerComponent.attacking==true){ //проверяем, атакует ли игрок в момент контакта, тут атакует и не дохнет
            mEngine.delete(contactEntity);
        }

        if (mainItemComponent.tags.contains("eye")){
            playerComponent.hp -= 1;
        }

        if (mainItemComponent.tags.contains("flag")) {
            playerComponent.win = true;
        }
    }

    @Override
    public void endContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        MainItemComponent item = mainItemMapper.get(contactEntity);
        if (item != null && item.tags.contains("platform")) {
            PlayerComponent playerComponent = playerMapper.get(animEntity);
            playerComponent.touchedPlatforms = 0;
        }
    }


    // помогает персонажу не улететь в небеса,грубо говоря добавляет трение
    @Override
    public void preSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        Vector2 normal = contact.getWorldManifold().getNormal();

        // через нормал чекаем направление с которым плеер столкнулся и выдаём нужное трение для неё
        if (Math.abs(normal.x) > 0.7f) { // тут понимаемЮчто это стена и выдаём маленькое трение чтоб чел упал
            contact.setFriction(0.1f);
        } else if (Math.abs(normal.y) > 0.7f) { // а тут понимаем,что это пол и даём нормальное трение
            contact.setFriction(1);
        } else {
            contact.setFriction(0);
        }
    }

    @Override
    public void postSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }
}

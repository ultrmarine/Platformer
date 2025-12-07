package io.github.Platformer.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;

import games.rednblack.editor.renderer.components.ParentNodeComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.components.sprite.SpriteAnimationComponent;
import games.rednblack.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import io.github.Platformer.component.PlayerComponent;

@All(PlayerComponent.class)
public class PlayerAnimationSystem extends IteratingSystem {

    protected ComponentMapper<ParentNodeComponent> parentMapper;
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<PlayerComponent> playerMapper;
    protected ComponentMapper<SpriteAnimationComponent> spriteMapper;
    protected ComponentMapper<SpriteAnimationStateComponent> spriteStateMapper;
    protected ComponentMapper<TransformComponent> transformMapper;

    @Override
    protected void process(int entity) {
        ParentNodeComponent nodeComponent = parentMapper.get(entity);
        Body body = physicsMapper.get(nodeComponent.parentEntity).body;

        if (body == null)
            return;

        PlayerComponent playerComponent = playerMapper.get(entity);
        SpriteAnimationComponent spriteAnimationComponent = spriteMapper.get(entity);
        SpriteAnimationStateComponent spriteAnimationStateComponent = spriteStateMapper.get(entity);
        TransformComponent transformComponent = transformMapper.get(entity);

        // proverka na povorot personaja
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            transformComponent.flipX = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            transformComponent.flipX = false;
        }

        if (Math.abs(body.getLinearVelocity().x) > 0) {
            spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;

            spriteAnimationComponent.currentAnimation = "run";
        }

        if (Math.abs(body.getLinearVelocity().x) >= 0 && Math.abs(body.getLinearVelocity().x) <= 1.5) {
            spriteAnimationComponent.currentAnimation = "idle";
        }

        if (body.getLinearVelocity().y >= 10 && playerComponent.touchedPlatforms != 1) {
            spriteAnimationComponent.currentAnimation = "jump";
        }

        if (body.getLinearVelocity().y <= -0 && playerComponent.touchedPlatforms != 1){
            spriteAnimationComponent.currentAnimation = "fall";
        }

        if (playerComponent.attacking==true){
            spriteAnimationComponent.currentAnimation = "fall"; //пока что это затычка, нужно будет найти анимацию атаки
        }

        spriteAnimationStateComponent.set(spriteAnimationComponent);
    }
}

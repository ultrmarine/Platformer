package io.github.Platformer.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
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

        if (Math.abs(body.getLinearVelocity().x) > 0) {
            spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;

            spriteAnimationComponent.currentAnimation = "run";
//            spriteAnimationComponent.fps = Math.max(6, (int)Math.abs(body.getLinearVelocity().x) * 3);

            transformComponent.flipX = body.getLinearVelocity().x < 0;
        } else if (Math.abs(body.getLinearVelocity().x) < 0) {
            transformComponent.flipX = body.getLinearVelocity().x < 0;
        }
//        else if (playerComponent.touchedPlatforms > 0) {
//            spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
//
//            spriteAnimationComponent.currentAnimation = "idle";
//        }

        if (Math.abs(body.getLinearVelocity().x) >= 0 && Math.abs(body.getLinearVelocity().x) <= 1.5) {
            spriteAnimationComponent.currentAnimation = "idle";
        }

        spriteAnimationStateComponent.set(spriteAnimationComponent);
    }
}

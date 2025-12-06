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
import io.github.Platformer.component.SkeletonComponent;


@All(SkeletonComponent.class)
public class SkeletonAnimationSystem extends IteratingSystem {
    protected ComponentMapper<ParentNodeComponent> parentMapper;
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<SpriteAnimationComponent> spriteMapper;
    protected ComponentMapper<SpriteAnimationStateComponent> spriteStateMapper;
    protected ComponentMapper<TransformComponent> transformMapper;


    @Override
    protected void process(int entity) {
        ParentNodeComponent nodeComponent = parentMapper.get(entity);
        Body body = physicsMapper.get(nodeComponent.parentEntity).body;

        if (body == null)
            return;

        SpriteAnimationComponent spriteAnimationComponent = spriteMapper.get(entity);
        SpriteAnimationStateComponent spriteAnimationStateComponent = spriteStateMapper.get(entity);
        TransformComponent transformComponent = transformMapper.get(entity);

        if (body.getLinearVelocity().x < 0){
            transformComponent.flipX = true;
        } else if (body.getLinearVelocity().x > 0){
            transformComponent.flipX = false;
        }

        if (Math.abs(body.getLinearVelocity().x) > 0) {
            spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;

            spriteAnimationComponent.currentAnimation = "walk";
        }

        if (Math.abs(body.getLinearVelocity().x) >= 0 && Math.abs(body.getLinearVelocity().x) <= 1.5) {
            spriteAnimationComponent.currentAnimation = "idle";
        }

        spriteAnimationStateComponent.set(spriteAnimationComponent);
    }
}

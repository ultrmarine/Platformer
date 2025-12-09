package io.github.Platformer.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import games.rednblack.editor.renderer.components.ParentNodeComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import io.github.Platformer.component.FlyEnemyComponent;


@All(FlyEnemyComponent.class)
public class FlyEnemyAnimationSystem extends IteratingSystem {
    protected ComponentMapper<ParentNodeComponent> parentMapper;
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<TransformComponent> transformMapper;

    @Override
    protected void process(int entity) {
        ParentNodeComponent nodeComponent = parentMapper.get(entity);
        Body body = physicsMapper.get(nodeComponent.parentEntity).body;

        if (body == null)
            return;

        TransformComponent transformComponent = transformMapper.get(entity);

        if (body.getLinearVelocity().x < 0){
            transformComponent.flipX = false;
        } else if (body.getLinearVelocity().x > 0){
            transformComponent.flipX = true;
        }


    }
}

package io.github.Platformer.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ViewPortComponent;

@All(ViewPortComponent.class)
public class CameraSystem extends IteratingSystem {

    protected ComponentMapper<TransformComponent> transformMapper;
    protected ComponentMapper<ViewPortComponent> viewportMapper;

    private int focus = -1;
    private static final Vector3 mVector3 = new Vector3();

    @Override
    protected void process(int entity) {
        ViewPortComponent viewPortComponent = viewportMapper.get(entity);
        Camera camera = viewPortComponent.viewPort.getCamera();

        if (focus != -1) {
            TransformComponent transformComponent = transformMapper.get(focus);

            if (transformComponent != null) {

                float x = transformComponent.x + 40; // получаем х плеера и прибавляем к нему рандомное число (наверн в пикселях я хз лол),потом по этому иксу камера будет следить за ним
                float y = transformComponent.y + 20; // тоже самое только с y

                mVector3.set(x, y, 0);
                camera.position.lerp(mVector3, 0.1f); // lerp это плавное смещение камеры,0.1f это скорость следования,больше 1 кстати артефакты с двоением экрана начинается
            }
        }
    }


    public void setFocus(int focus) {
        this.focus = focus;
    }
}

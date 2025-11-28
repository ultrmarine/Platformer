package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class PlayerComponent extends PooledComponent {
    public int touchedPlatforms = 0;
    public int hp = 1;


    @Override
    public void reset() {
        touchedPlatforms = 0;
        hp = 1;
    }
}

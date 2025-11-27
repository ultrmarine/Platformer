package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class PlayerComponent extends PooledComponent {
    public int touchedPlatforms = 0;
    public int jump = 0;


    @Override
    public void reset() {
        touchedPlatforms = 0;
        jump = 1;
    }
}

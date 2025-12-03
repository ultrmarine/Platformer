package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class PlayerComponent extends PooledComponent {
    public int touchedPlatforms = 0;
    public int hp = 1;
    public int coinsCollected = 0;


    @Override
    public void reset() {
        coinsCollected = 0;
        touchedPlatforms = 0;
        hp = 1;
    }
}

package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class FlyEnemyComponent extends PooledComponent {
    public int hp = 1;
    public int touch = 0; // на этом будет работать движение противников

    @Override
    protected void reset() {
        hp = 1;
        touch = 0;
    }
}

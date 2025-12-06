package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class SkeletonComponent extends PooledComponent {
    public int hp = 2;
    public int touch = 0; // на этом будет работать движение противников

    @Override
    protected void reset() {
        hp = 2;
        touch = 0;
    }
}

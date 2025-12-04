package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class SpikeComponent extends PooledComponent { // вот создал шип, он наносит 1 демедж

    public int value = 1;

    @Override
    public void reset() {
        value = 1;
    }
}

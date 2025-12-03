package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class CoinComponent extends PooledComponent { // вот создал монетку, стоит целый 1 чтото

    public int value = 1;

    @Override
    public void reset() {
        value = 1;
    }
}

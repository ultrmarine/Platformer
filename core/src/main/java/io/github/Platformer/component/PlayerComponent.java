package io.github.Platformer.component;

import com.artemis.PooledComponent;

public class PlayerComponent extends PooledComponent {
    public int touchedPlatforms = 0;
    public int hp = 1;
    public int coinsCollected = 0;
    public int damage = 3; // дамаг игрока
    public boolean attacking = false; // статус, атакует ли игрок
    public boolean win = false; // победил ли игрочёк

//    public int getCoinsCollected(){
//        return coinsCollected;
//    }

    @Override
    public void reset() {
        coinsCollected = 0;
        touchedPlatforms = 0;
        hp = 1;
        damage = 3;
        attacking = false;
        win = false;
    }
}

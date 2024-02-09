package com.example.a24a_10357_exe1.Logic;

public class GameManager {
    private int life;
    private int crashes = 0;

    public GameManager(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public int getCrashes() {
        return crashes;
    }

    public GameManager setCrashes(int crashes) {
        this.crashes = crashes;
        return this;
    }
}

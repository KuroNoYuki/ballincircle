package com.snowy.games;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.snowy.games.sprites.CircleSprite;

class Ball extends CircleSprite {

    private final Vector2 v = new Vector2();
    private final Vector2 g = new Vector2();

    Ball(TextureRegion region, float r, float vx, float vy, float g) {
        super(region, r);
        v.set(vx, vy);
        this.g.set(0f, -g);
    }

    void update(float deltaTime) {
        v.mulAdd(g, deltaTime);
        pos.mulAdd(v, deltaTime);
    }

    Vector2 getV() {
        return v;
    }

    Vector2 getG() {
        return g;
    }
}

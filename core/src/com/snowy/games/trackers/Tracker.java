package com.snowy.games.trackers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.snowy.games.sprites.CircleSprite;

public class Tracker extends CircleSprite {

    private static final float INCREASE_SPEED = 2f;
    private static final float SHOW_PERIOD = 3f;
    private static final float DECREASE_SPEED = 1f;

    private enum State {INCREASE, SHOW, DECREASE}

    private State state;
    private float alpha;
    private float showTimer;
    private boolean isDestroyed;

    Tracker(TextureRegion region, float r) {
        super(region, r);
    }

    public void set(Vector2 pos) {
        this.pos.set(pos);
        state = State.INCREASE;
        alpha = 0f;
        showTimer = 0f;
        isDestroyed = false;
    }

    void update(float deltaTime) {
        switch (state) {
            case INCREASE:
                alpha += INCREASE_SPEED * deltaTime;
                if(alpha >= 1f) {
                    alpha = 1f;
                    state = State.SHOW;
                }
                break;
            case SHOW:
                showTimer += deltaTime;
                if(showTimer >= SHOW_PERIOD) state = State.DECREASE;
                break;
            case DECREASE:
                alpha -= DECREASE_SPEED * deltaTime;
                if(alpha <=0) isDestroyed = true;
                break;
            default:
                throw new RuntimeException("state = " + state);
        }
    }

    boolean isDestroyed() {
        return isDestroyed;
    }

    float getAlpha() {
        return alpha;
    }
}

package com.snowy.games.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class CircleSprite {

    private final TextureRegion region;
    protected final Vector2 pos = new Vector2();
    private float r;

    public CircleSprite(TextureRegion region, float r) {
        this.region = region;
        this.r = r;
    }

    public void draw(SpriteBatch batch) {
        final float d = 2f * r;
        batch.draw(region, getLeft(), getBottom(), d, d);
    }

    private float getLeft() {
        return pos.x - r;
    }

    private float getBottom() {
        return pos.y - r;
    }

    public float getR() {
        return r;
    }

    public Vector2 getPos() {
        return pos;
    }
}

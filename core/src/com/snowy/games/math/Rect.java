package com.snowy.games.math;

import com.badlogic.gdx.math.Vector2;

public class Rect {

    public final Vector2 pos = new Vector2();
    private float halfWidth;
    private float halfHeight;

    public Rect() {}

    public Rect(float posX, float posY, float halfWidth, float halfHeight) {
        pos.set(posX, posY);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    public float getWidth() {
        return halfWidth * 2f;
    }

    public float getHeight() {
        return halfHeight * 2f;
    }

    public void setSize(float width, float height) {
        halfWidth = width / 2f;
        halfHeight = height / 2f;
    }
}

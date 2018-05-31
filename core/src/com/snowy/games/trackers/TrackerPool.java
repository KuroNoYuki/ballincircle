package com.snowy.games.trackers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class TrackerPool {

    private final ArrayList<Tracker> freeObjects = new ArrayList<Tracker>();
    private final ArrayList<Tracker> activeObjects = new ArrayList<Tracker>();

    private final TextureRegion region;
    private final float trackerR;

    public TrackerPool(TextureRegion region, float trackerR) {
        this.region = region;
        this.trackerR = trackerR;
    }

    public Tracker obtain() {
        Tracker obj;
        if (freeObjects.isEmpty())
            obj = new Tracker(region, trackerR);
        else
            obj = freeObjects.remove(freeObjects.size() - 1);
        activeObjects.add(obj);
        debugLog();
        return obj;
    }

    private void free(Tracker obj) {
        if (!activeObjects.remove(obj)) throw new RuntimeException("Deleting not active obj = " + obj);
        freeObjects.add(obj);
        debugLog();
    }

    public void freeAllDestroyedActiveObjects() {
        for (int i = 0; i < activeObjects.size(); i++) {
            Tracker obj = activeObjects.get(i);
            if(obj.isDestroyed()) {
                free(obj);
                i--;
            }
        }
    }

    public void updateActiveObjects(float deltaTime) {
        for (int i = 0, cnt = activeObjects.size(); i < cnt; i++) {
            Tracker obj = activeObjects.get(i);
            if(obj.isDestroyed()) throw new RuntimeException("Update destroyed obj");
            obj.update(deltaTime);
        }
    }

    public void drawActiveObjects(SpriteBatch batch) {
        for (int i = 0, cnt = activeObjects.size(); i < cnt; i++) {
            Tracker obj = activeObjects.get(i);
            if(obj.isDestroyed()) throw new RuntimeException("Update destroyed obj");
            batch.setColor(1f, 1f, 1f, obj.getAlpha());
            obj.draw(batch);
        }
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private void debugLog() {
//        System.out.println("" + activeObjects.size() + "/ "+ freeObjects.size());
    }
}

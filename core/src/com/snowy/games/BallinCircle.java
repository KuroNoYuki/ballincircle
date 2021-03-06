package com.snowy.games;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.snowy.games.math.Rect;
import com.snowy.games.sprites.CircleSprite;
import com.snowy.games.trackers.TrackerPool;

public class BallinCircle implements ApplicationListener, InputProcessor, Input.TextInputListener {

    private final Rect worldBounds = new Rect();
    private final Rect glBounds = new Rect(0f, 0f, 1f, 1f);
    private final Matrix4 matWorldToGl = new Matrix4();

	private SpriteBatch batch;
	private TextureAtlas atlas;
    private CircleSprite circle;
    private Ball ball;
    private TrackerPool trackerPool;

    private static final float CIRCLE_R = 0.26f;

    private static final float BALL_R = 0.05f;
    private static final float BALL_V0X = 0.1f;
    private static final float BALL_V0Y = 0.2f;
    private static final float G = 0.2f;

    private static final float MAX_DST = CIRCLE_R - BALL_R;

    private static final float TRACKER_R = 0.01f;
    private static final float TACKER_INTERVAL = 0.1f;
    private float trackerTimer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("Textures/mainAtlas.atlas");
        circle = new CircleSprite(atlas.findRegion("circle"), 0.5f);
        ball = new Ball(atlas.findRegion("ball"), BALL_R, BALL_V0X, BALL_V0Y, G);
        trackerPool = new TrackerPool(atlas.findRegion("tracker"), TRACKER_R);
        Gdx.input.setInputProcessor(this);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        System.gc();
	}

	@Override
	public void resize(int width, int height) {
        float aspect = width / (float) height;
        worldBounds.setSize(aspect, 1f);

        calcTransitionMatrix(matWorldToGl, worldBounds, glBounds); //Расчитываем матрицу перехода Мир-GL
        batch.setProjectionMatrix(matWorldToGl); //И устанавливаем её батчеру. В общем то она нам больше и не нужна
	}

    //Расчёт матрицы перехода 4x4
    private static void calcTransitionMatrix(Matrix4 mat, Rect src, Rect dst) {
        float scaleX = dst.getWidth() / src.getWidth();
        float scaleY = dst.getHeight() / src.getHeight();
        mat.idt().translate(dst.pos.x, dst.pos.y, 0f).scale(scaleX, scaleY, 1f).translate(-src.pos.x, -src.pos.y, 0f);
    }

    private final Vector2 d = new Vector2();
    private static final int ITERATIONS = 500;

	@Override
	public void render () {
        update(Gdx.graphics.getDeltaTime());
        draw(batch);
	}

	private void update(float dt) {
        trackerTimer += dt;
        if(trackerTimer >= TACKER_INTERVAL) {
            trackerTimer = 0f;
            trackerPool.obtain().set(ball.getPos());
        }
        trackerPool.updateActiveObjects(dt);
        trackerPool.freeAllDestroyedActiveObjects();
        dt /= ITERATIONS;
        for (int i = 0; i < ITERATIONS; i++) {
            ball.update(dt);
            final Vector2 ballPos = ball.getPos();
            final Vector2 circlePos = circle.getPos();
            if (ballPos.dst(circlePos) > MAX_DST) {
                d.set(ballPos).sub(circlePos).nor().scl(MAX_DST);
                ballPos.set(circlePos).add(d);
                float angle = 2f * (90f - (ball.getV().angle() - d.angle()));
//                System.out.println(angle);
                ball.getV().rotate(angle);
            }
        }
    }

    private void draw(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        circle.draw(batch);
        ball.draw(batch);
        trackerPool.drawActiveObjects(batch);
        batch.end();
    }

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		batch.dispose();
        atlas.dispose();
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.input.getTextInput(this, "Input x y vx vy g", "" ,"");
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void input(String text) {
        String[] tokens = text.split(" ");
        float x = Float.parseFloat(tokens[0]);
        float y = Float.parseFloat(tokens[1]);
        float vx = Float.parseFloat(tokens[2]);
        float vy = Float.parseFloat(tokens[3]);
        float gravity = Float.parseFloat(tokens[4]);
        ball.getPos().set(x, y);
        ball.getV().set(vx, vy);
        ball.getG().set(0, -gravity);
    }

    @Override
    public void canceled() {

    }
}

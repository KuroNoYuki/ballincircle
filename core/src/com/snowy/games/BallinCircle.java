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

public class BallinCircle implements ApplicationListener, InputProcessor, Input.TextInputListener {

    private final Rect worldBounds = new Rect();
    private final Rect glBounds = new Rect(0f, 0f, 1f, 1f);
    private final Matrix4 matWorldToGl = new Matrix4();

	private SpriteBatch batch;
	private TextureAtlas atlas;
    private TextureRegion regionCircle;
    private TextureRegion regionBall;

    private static final float CIRCLE_R = 0.26f;
    private static final float BALL_R = 0.005f;
    private static final float BALL_D = BALL_R * 2f;
    private static final float MAX_DST = CIRCLE_R - BALL_R;
    private final Vector2 circlePos = new Vector2();
//    private final Vector2 ballPos = new Vector2(MAX_DST * (float)Math.sqrt(2f) / 2f, 0f);
    private final Vector2 ballPos = new Vector2(-0.15f, 0.1f);
    private final Vector2 ballV = new Vector2(0.1f, 0.2f);
    private final Vector2 g = new Vector2(0f, -0.2f);
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("Textures/mainAtlas.atlas");
        regionCircle = atlas.findRegion("circle");
        regionBall = atlas.findRegion("ball");
        Gdx.input.setInputProcessor(this);
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
    private static final int ITERATIONS = 2000;

	@Override
	public void render () {
        //update
        float dt = Gdx.graphics.getDeltaTime();
        dt /= ITERATIONS;
        for (int i = 0; i < ITERATIONS; i++) {
            ballV.mulAdd(g, dt);
            ballPos.mulAdd(ballV, dt);
            if (ballPos.dst(circlePos) > MAX_DST) {
                d.set(ballPos).sub(circlePos).nor().scl(MAX_DST);
                ballPos.set(circlePos).add(d);
                float angle = 2f * (90f - (ballV.angle() - d.angle()));
//                System.out.println(angle);
                ballV.rotate(angle);
            }
        }
//        //draw
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        batch.draw(regionCircle, -0.5f, -0.5f, 1f, 1f);
        batch.draw(regionBall, ballPos.x - BALL_R, ballPos.y - BALL_R, BALL_D, BALL_D);
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
        ballPos.set(x, y);
        ballV.set(vx, vy);
        g.set(0, -gravity);
    }

    @Override
    public void canceled() {

    }
}

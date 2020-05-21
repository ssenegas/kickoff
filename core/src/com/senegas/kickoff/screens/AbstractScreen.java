package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.senegas.kickoff.KickOff;

public abstract class AbstractScreen implements Screen {

    private static final String TAG = AbstractScreen.class.getSimpleName();

    protected final KickOff app;
    protected Stage stage;

    public AbstractScreen(final KickOff app) {
        this.app = app;
        this.stage = new Stage();
    }

    public abstract void update(float deltaTime);

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose");
        this.stage.dispose();
    }

    public final KickOff getApp() {
        return this.app;
    }
}

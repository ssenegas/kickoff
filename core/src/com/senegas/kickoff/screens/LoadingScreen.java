package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.managers.GameScreenManager;

public class LoadingScreen extends AbstractScreen {

    private static final String TAG = LoadingScreen.class.getSimpleName();
    private BitmapFont font;
    private float progress;

    public LoadingScreen(KickOff app) {
        super(app);
        this.font = new BitmapFont();

        this.progress = 0;

        queueAssets();
    }

    @Override
    public void update(float deltaTime) {
        this.progress = MathUtils.lerp(this.progress, this.app.assets.getProgress(), .1f);
        if (this.app.assets.update() && this.progress >= this.app.assets.getProgress() - .01f) {
            this.app.gsm.setScreen(GameScreenManager.STATE.SPLASH);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        this.app.shapeBatch.begin(ShapeRenderer.ShapeType.Filled);

        this.app.shapeBatch.setColor(Color.DARK_GRAY);
        this.app.shapeBatch.rect(32, this.stage.getCamera().viewportHeight / 2 - 8,
                this.stage.getCamera().viewportWidth - 64, 2);

        this.app.shapeBatch.setColor(Color.LIGHT_GRAY);
        this.app.shapeBatch.rect(32, this.stage.getCamera().viewportHeight / 2 - 8,
                this.progress * (this.stage.getCamera().viewportWidth - 64), 2);

        this.app.shapeBatch.end();

        this.app.batch.begin();
        this.font.draw(this.app.batch, "Loading...", 20, 20);
        this.app.batch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
        this.font.dispose();
        Gdx.app.log(TAG, "dispose");
    }

    private void queueAssets() {
        this.app.assets.load("gfx/logo.png", Texture.class);
        this.app.assets.load("entities/ball.png", Texture.class);
        this.app.assets.load("entities/style1a.png", Texture.class);
        this.app.assets.load("entities/style1b.png", Texture.class);
        this.app.assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.app.assets.load("pitches/classic.tmx", TiledMap.class);
        this.app.assets.load("pitches/playermanager.tmx", TiledMap.class);
        this.app.assets.load("pitches/synthetic.tmx", TiledMap.class);
        this.app.assets.load("pitches/soggy.tmx", TiledMap.class);
        this.app.assets.load("pitches/wet.tmx", TiledMap.class);
        this.app.assets.load("sounds/crowd.ogg", Sound.class);
        this.app.assets.load("sounds/whistle.ogg", Sound.class);
        this.app.assets.load("sounds/dribble.ogg", Sound.class);
        this.app.assets.load("sounds/bounce.ogg", Sound.class);
    }
}

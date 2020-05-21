package com.senegas.kickoff;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.senegas.kickoff.managers.GameScreenManager;

public class KickOff extends Game {
	
	public static String TITLE = "Open Kick Off";
    public static String VERSION = "0.3.7";
    public static int APP_DESKTOP_WIDTH = 1280;
    public static int APP_DESKTOP_HEIGHT = 800;

    public static int V_WIDTH = 1280;
    public static int V_HEIGHT = 800;

	public GameScreenManager gsm;
    public AssetManager assets;

    public SpriteBatch batch;
    public ShapeRenderer shapeBatch;

	@Override
	public void create () {
        this.batch = new SpriteBatch();
        this.shapeBatch = new ShapeRenderer();

        this.assets = new AssetManager();
        this.gsm = new GameScreenManager(this);
	}

	@Override
	public void render() {
		super.render();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
        this.batch.dispose();
        this.shapeBatch.dispose();
        this.gsm.dispose();
        this.assets.dispose();
	}
}

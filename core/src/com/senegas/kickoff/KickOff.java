package com.senegas.kickoff;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.senegas.kickoff.managers.GameScreenManager;
import com.senegas.kickoff.screens.MainMenu;

public class KickOff extends Game {
	
	public static String TITLE = "Open Kick Off";
    public static String VERSION = "0.3.7";

	public GameScreenManager gsm;

	@Override
	public void create () {
		gsm = new GameScreenManager(this);
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
		gsm.dispose();
	}
}

package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.managers.GameScreenManager;

public class MainMenu extends AbstractScreen {
	private static final String TAG = MainMenu.class.getSimpleName();
	
	private Stage menustage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonPlay;
	private BitmapFont white;

	public MainMenu(final KickOff app) {
		super(app);
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void show() {
		this.menustage = new Stage();
		
		Gdx.input.setInputProcessor(this.menustage);

        this.atlas = new TextureAtlas("ui/button.pack");
        this.skin = new Skin(this.atlas);

        this.table = new Table(this.skin);
        this.table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.white = new BitmapFont(Gdx.files.internal("font/white.fnt"), false);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = this.skin.getDrawable("button.up");
		textButtonStyle.down = this.skin.getDrawable("button.down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = this.white;

        this.buttonPlay = new TextButton("PLAY", textButtonStyle);
        this.buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
                MainMenu.this.app.gsm.setScreen(GameScreenManager.STATE.PLAY);
			}
		});
        this.buttonPlay.pad(15);

        this.table.add(this.buttonPlay);
        this.table.debug();
		this.menustage.addActor(this.table);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		this.menustage.act(delta);
		this.menustage.draw();
	}

	@Override
	public void resize(int width, int height) {

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
		Gdx.app.log(TAG, "dispose");

		this.menustage.dispose();
		this.skin.dispose();
	}

}

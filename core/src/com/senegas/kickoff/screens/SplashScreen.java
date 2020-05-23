package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.managers.GameScreenManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;

public class SplashScreen extends AbstractScreen {

    private static final String TAG = SplashScreen.class.getSimpleName();
    private Image splashImage;

    public SplashScreen(KickOff app) {
        super(app);
    }

    @Override
    public void update(float deltaTime) {
        this.stage.act(deltaTime);
    }

    @Override
    public void show() {
        Texture splashTexture = this.app.assets.get("gfx/logo.png");
        //Texture splashTexture = new Texture(Gdx.files.internal("gfx/logo.png"));
        this.splashImage = new Image(splashTexture);
        this.splashImage.setOrigin(this.splashImage.getWidth() / 2, this.splashImage.getHeight() / 2);

        this.stage.addActor(this.splashImage);
        System.out.println("splashImage width: " + this.splashImage.getWidth() + " SplashImage height: " + this.splashImage.getHeight());
        System.out.println("Stage width: " + this.stage.getWidth() + " Stage height: " + this.stage.getHeight());
        this.splashImage.setPosition(this.stage.getWidth() / 2 - 150, this.stage.getHeight() / 2 + 32);

        Action switchScreenAction = new Action() {
            @Override
            public boolean act(float delta){
                SplashScreen.this.app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU);
                return true;
            }
        };

        this.splashImage.addAction(Actions.sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(this.stage.getWidth() / 2 - 150, this.stage.getHeight() / 2 - 32, 2f, Interpolation.swing)),
                delay(1f), fadeOut(1.25f), switchScreenAction));
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        this.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, false);
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
    }
}

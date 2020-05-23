package com.senegas.kickoff.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.senegas.kickoff.screens.MatchScreen;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    static private String time;
    private float timeCount;

    private String score;

    private Label timerLabel;
    private Label scoreLabel;

    private MatchScreen match;

    public Hud(MatchScreen match) {
        this.match = match;

        this.timeCount = 3 * 60;
        this.time = String.format("%2d : %02d", 4, 56);
        this.score = String.format("%2d    %2d", this.match.getScoreHome(), this.match.getScoreAway());

        this.viewport = new ScreenViewport();
        this.stage = new Stage(this.viewport, match.getApp().batch);

        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        this.timerLabel = new Label(String.format("%s", time), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        this.scoreLabel = new Label(String.format("%s", this.score), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        //table.add(this.scoreLabel).expandX().padTop(16);
        //table.row();
        //table.add(this.timerLabel).expandX().padTop(16);
        //table.row().expandX().fillX();

        //stage.addActor(radar);
        this.stage.addActor(table);
    }

    public void update(float deltaTime) {
        //this.timeCount -= deltaTime;
        int minutes = ((int) this.timeCount) / 60;
        int seconds = ((int) this.timeCount) % 60;
        //this.time = String.format("%2d : %02d", minutes, seconds);
        //this.timerLabel.setText(String.format("%s", this.time));

        this.score = String.format("%2d    %2d", this.match.getScoreHome(), this.match.getScoreAway());
        this.scoreLabel.setText(String.format("%s", this.score));
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}

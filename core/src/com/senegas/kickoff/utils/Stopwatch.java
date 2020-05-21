package com.senegas.kickoff.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stopwatch {

    private float timeCount;
    private int minutes;
    private int seconds;
    private final String format = "%2d : %02d";
    private BitmapFont font;
    GlyphLayout layout;

    public Stopwatch(float minutes) {
        super();
        this.timeCount = minutes * 60;
        this.layout = new GlyphLayout();
        this.font = new BitmapFont();
    }

    public void update(float deltaTime) {
        this.timeCount -= deltaTime;
        this.minutes = ((int) this.timeCount) / 60;
        this.seconds = ((int) this.timeCount) % 60;
    }

    public void draw(SpriteBatch batch) {
        this.layout.setText(this.font, getFormattedTime());
        float x = Gdx.graphics.getWidth() / 2 - this.layout.width / 2;
        float y = 16;
        this.font.setColor(0f, 0f, 0f, 255.0f);
        this.font.draw(batch, this.layout, x, y);
    }

    private String getFormattedTime() {
        return String.format(this.format, this.minutes, this.seconds);
    }
}

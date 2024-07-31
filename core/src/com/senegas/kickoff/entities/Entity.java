package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public interface Entity {
    void update(float deltaTime);
    void render(SpriteBatch batch);
}

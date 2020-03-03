package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public abstract class Entity {
    protected Vector3 position;
    protected Vector3 velocity;
    protected Texture texture;
    protected TextureRegion frames[][];

    public Entity(Texture texture, Vector3 position) {
        this.texture = texture;
        this.position = position;
        this.velocity = new Vector3(0, 0, 0);
    }

    public abstract void update(float deltaTime);

}

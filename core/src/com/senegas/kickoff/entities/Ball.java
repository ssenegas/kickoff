package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Ball implements Entity {

    public static final int SPRITE_SIZE = 16; // 16 px X 16 px
    public static final int HALF_SPRITE_SIZE = 8;

    /**
     * ball mass (kg)<br>
     * <a href="http://www.fifa.com/">FIFA.com</a> says: <em>between 410 g (14 oz) and 450 g (16 oz) in weight at the start of the match</em>
     */
    public static final float MASS_IN_GRAMS = 0.430f;

    /**
     * acceleration constant (m/s^2)
     */
    public static final float GRAVITY = 9.81f;

    /**
     * air resistance term
     */
    public static final float DRAG = 0.350f;

    /**
     * In order to save calculation time, K/M is precalculated
     */
    private static final float K_M = DRAG / MASS_IN_GRAMS;

    /**
     * In order to save calculation time, MG/K is precalculated
     */
    private static final float MG_K = MASS_IN_GRAMS * GRAVITY / DRAG;

    private Texture texture;
    private TextureRegion frames[][];
    private static final int FRAME_COUNT = 4;
    private float maxFrameTime = 0.1f; // max time between each frame

    private int currentFrame = 0;

    private Vector3 lastPosition;
    private Vector3 position;
    private Vector3 velocity;

    private Player owner = null;

    /**
     * Constructor
     *
     * @param position position of the ball
     */
    public Ball(Texture ball, Vector3 position) {
        this.texture = ball;
        this.frames = TextureRegion.split(this.texture, SPRITE_SIZE, SPRITE_SIZE);

        this.position = position;
        this.velocity = new Vector3(0, 0, 0);
        this.lastPosition = this.lastPosition = new Vector3(this.position);
    }

    @Override
    public void update(float deltaTime) {
        this.lastPosition.set(this.position.x, this.position.y, this.position.z);

        this.velocity.x -= (K_M * this.velocity.x) * deltaTime;
        this.velocity.y -= (K_M * this.velocity.y) * deltaTime;
        this.velocity.z -= (K_M * this.velocity.z + GRAVITY) * deltaTime;

        // update position
        if (this.position.z > 0) {
            this.velocity.add(0, 0, -GRAVITY);
        }

        if (deltaTime == 0)
            return;

        this.velocity.scl(deltaTime);
        this.position.add(this.velocity.x, this.velocity.y, this.velocity.z);
        if (this.position.z < 0) { // ball bounces on floor
            this.velocity.z = -this.velocity.z;
            this.position.z += this.velocity.z;

            if (this.velocity.z > 0.9f) {
                //this.bounce.play(0.2f);
            }

            this.velocity.z -= this.velocity.z / 4;
            this.velocity.x -= this.velocity.x / 32;
            this.velocity.y -= this.velocity.y / 32;
        }
        this.velocity.scl(1 / deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        int scrx = (int) this.position.x;
        int scry = (int) this.position.y;
        int shadx = scrx + (int) (this.position.z / 2);
        int shady = scry + (int) (this.position.z / 2);

        this.currentFrame = shadx - scrx;
        //low ball, shadow contained in sprite
        if (this.currentFrame < 4) {
            scry += this.position.z / 2;
            if (this.currentFrame >= 0 && this.currentFrame < 8) {
                batch.draw(this.frames[0][this.currentFrame], scrx - HALF_SPRITE_SIZE, scry - HALF_SPRITE_SIZE);
            }
        } else { //draw the shadow
            int shadowFrame = 8;
            batch.draw(this.frames[0][shadowFrame], shadx - HALF_SPRITE_SIZE, shady - HALF_SPRITE_SIZE);
            //draw the ball
            scry += (this.position.z / 2);
            this.currentFrame = (int) Math.min(3, this.position.z / 32);
            int ballFrame = this.currentFrame + 4;
            batch.draw(this.frames[0][ballFrame], scrx - HALF_SPRITE_SIZE, scry - HALF_SPRITE_SIZE);
        }
    }

    /**
     * Gets the ball position
     * @return the position
     */
    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getLastPosition() {
        return this.lastPosition;
    }

    /**
     * Apply a kick to the ball
     *
     * @param velocity
     * @param kickedBy
     */
    public void kick(Vector3 velocity, Player kickedBy) {
        this.velocity.set(velocity);
        this.owner = kickedBy;
    }
}

package com.senegas.kickoff.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.senegas.kickoff.utils.Direction;

import java.util.Arrays;
import java.util.List;

public class Player implements Entity {
    private static final int FRAME_COLS = 20;
    private static final int FRAME_ROWS = 11;
    protected static final int SPRITE_SIZE = 16; // 16 px X 16 px
    protected static final int HALF_SPRITE_SIZE = 8;
    private static final float SPEED = 150f;
    private int height = 177; // 1m 77
    private static final float IN_POSITION_THRESHOLD = 2.25f;

    private List<Integer> runningFrameAnimationSequence = Arrays.asList(0, 3, 2, 1, 1, 2, 3, 4, 7, 6, 5, 5, 6, 7);
    protected TextureRegion frames[][];
    private float maxFrameTime = 5 / SPEED;

    private Texture jersey;
    private TextureRegion currentFrame;
    private float stateTime;

    private Vector3 position;
    private Vector3 destination;
    private Direction direction;
    private Circle bounds;
    private Team team;

    public Player(Vector3 startPosition) {
        this(null, null, startPosition);
    }

    /**
     * Constructor
     * @param jersey
     * @param startPosition
     */
    public Player(Texture jersey, Team team, Vector3 startPosition) {
        this.jersey = jersey;
        this.frames = TextureRegion.split(jersey, SPRITE_SIZE, SPRITE_SIZE);
        this.position = startPosition;
        this.bounds = new Circle(this.position.x, this.position.y, HALF_SPRITE_SIZE);
        this.destination = startPosition;
        this.stateTime = 0f;
        this.direction = Direction.SOUTH;
        this.team = team;
    }

    public void update(float deltaTime) {
        if (!inPosition()) {
            Vector3 directionVector = this.destination.cpy().sub(this.position).nor();
            this.position.mulAdd(directionVector, SPEED * deltaTime);
            if (inPosition()) {
                this.position = this.destination;
            }

            this.bounds.setPosition(this.position.x, this.position.y);
            this.direction = Direction.getDirection(directionVector.x, directionVector.y);
            this.stateTime += deltaTime;

            int currentFrameIndex = (int) (this.stateTime / this.maxFrameTime) % runningFrameAnimationSequence.size();
            this.currentFrame = getFrameForAnimation(currentFrameIndex);
        }
    }

    private TextureRegion getFrameForAnimation(int frameIndex) {
        int sequenceIndex = runningFrameAnimationSequence.get(frameIndex);
        int row = (sequenceIndex + 8 * this.direction.ordinal()) / 20;
        int column = (sequenceIndex + 8 * this.direction.ordinal()) % 20;
        return this.frames[row][column];
    }

    public void render(SpriteBatch batch) {
        batch.draw(this.currentFrame, this.position.x - HALF_SPRITE_SIZE, this.position.y - HALF_SPRITE_SIZE);
    }

    /**
     * The player direction
     * @return the direction
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Gets the player boundaries used to check collision
     * @return Circle
     */
    public Circle getBounds() {
        return this.bounds;
    }

    /**
     * Gets the player position
     * @return the position
     */
    public Vector3 getPosition() {
        return this.position;
    }

    /**
     * Sets the player destination
     * @param destination
     */
    public void setDestination(Vector3 destination) {
        this.destination = destination;
    }

    /**
     * Gets the player destination
     * @return the destination
     */
    public Vector3 getDestination() {
        return this.destination;
    }

    /**
     * Gets the player speed
     * @return the destination
     */
    public float speed() {
        return SPEED;
    }

    /**
     * Checks if the player is in position
     * @return true if closer than threshold, false otherwise
     */
    public boolean inPosition() {
        return new Vector3(this.position).sub(this.destination).len() < IN_POSITION_THRESHOLD;
    }

    public Team getTeam() {
        return this.team;
    }

    public int height() {
        return this.height;
    }

    public void showBounds(ShapeRenderer shapeBatch) {
        // enable transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeBatch.begin(ShapeRenderer.ShapeType.Line);

        shapeBatch.setColor(new Color(0, 0, 0, 0.5f));
        shapeBatch.circle(this.bounds.x, this.bounds.y, this.bounds.radius);

        shapeBatch.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}

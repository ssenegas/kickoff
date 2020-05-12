package com.senegas.kickoff.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Ball entity class
 * @author Sébastien Sénégas
 *
 */
public class Ball extends Entity {

    public static final int SPRITE_WIDTH = 16;
    public static final int SPRITE_HEIGHT = 16;

	/** acceleration constant (m/s^2) */
	public static final float GRAVITY = 9.81f;
	/** ball mass (kg)<br>
	 * <a href="http://www.fifa.com/">FIFA.com</a> says: <em>not more than 450 g in weight and not less than 410 g</em>
	 */
	public static final float MASS_IN_GRAMS = 0.430f;
	/** air resistance term */
	public static final float DRAG = 0.350f;
	/** bounce angle factor (must be less that 1) */
	public static final float BOUNCE_SPEED_FACTOR = 0.6f;

    /** In order to save calculation time, M/K is precalculated */
    //private static final double	M_K = M/K;

    /** In order to save calculation time, K/M is precalculated */
	private static final float	K_M = DRAG / MASS_IN_GRAMS;
    /** In order to save calculation time, MG/K is precalculated */
	private static final float	MG_K = MASS_IN_GRAMS * GRAVITY / DRAG;

	private static final int FRAME_COUNT = 4;
    private int currentFrameAnimationColumn = 0;
    private float currentFrameTime = 0.0f;
    private float maxFrameTime = .1f; // max time between each frame
    private int runningFrameAnimation[] = { 0, 1, 1, 0 };
    private int currentFrame = 0;
    private float speed = 0;
    private Player owner = null;
	private Vector3 lastPosition;

    private static ShapeRenderer shapeRenderer = new ShapeRenderer(); // mainly used for debug purpose

	/**
	 * Constructor
	 * @param position position of the ball
	 */
	public Ball(Vector3 position) {
		super(new Texture("entities/ball.png"), position);
		frames = TextureRegion.split(texture, SPRITE_WIDTH, SPRITE_HEIGHT);
		lastPosition = new Vector3(getPosition());
	}

	/**
	 * Draw the ball and shadow animations
	 * @param batch
	 */
	public void draw(Batch batch) {

		//System.out.format("currentFrame: %d%n", currentFrame);
		//System.out.format("x: %f y: %f z: %f%n", position.x, position.y, position.z);

		int scrx = (int)position.x;
		int scry = (int)position.y;
		int shadx = scrx + (int)(position.z / 2);
		int shady = scry + (int)(position.z / 2);

		currentFrame = shadx - scrx;
		if (currentFrame < 4) {
			//low ball, shadow contained in sprite
			scry += position.z / 2;
			if (currentFrame >= 0 && currentFrame < 8) {
				batch.draw(frames[0][currentFrame], scrx - SPRITE_WIDTH/2, scry - SPRITE_HEIGHT/2);
			}
		} else {
			//draw the shadow
			int shadowFrame = 8;
			batch.draw(frames[0][shadowFrame], shadx - SPRITE_WIDTH/2, shady - SPRITE_HEIGHT/2);
			//draw the ball
			scry += (position.z / 2);
			currentFrame = (int) Math.min(3, position.z/32);
			int ballFrame = currentFrame + 4;
			batch.draw(frames[0][ballFrame], scrx - SPRITE_WIDTH/2, scry - SPRITE_HEIGHT/2);
		}
	}
	
	public void showPosition(OrthographicCamera camera) {
		// enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
		shapeRenderer.line(position.x - SPRITE_WIDTH/2, position.y, position.x + SPRITE_WIDTH/2, position.y);
		shapeRenderer.line(position.x, position.y - SPRITE_HEIGHT/2, position.x, position.y + SPRITE_HEIGHT/2);
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public Vector3 getLastPosition() {
		return lastPosition;
	}
	
	/**
	 * Update the ball's position and velocity
	 * @param deltaTime
	 */
	@Override
    public void update(float deltaTime) {
		lastPosition.set(position.x, position.y, position.z);

		velocity.x -= (K_M * velocity.x) * deltaTime;
		velocity.y -= (K_M * velocity.y) * deltaTime;	
		velocity.z -= (K_M * velocity.z + GRAVITY) * deltaTime;
		
		// update position
		if (position.z > 0)
			velocity.add(0, 0, -GRAVITY);
		
		velocity.scl(deltaTime);
		position.add(velocity.x, velocity.y, velocity.z);		
		if (position.z < 0) { // ball bounces on floor
			velocity.z = -velocity.z;
			position.z += velocity.z;
			
			velocity.z -= velocity.z / 4;
			velocity.x -= velocity.x / 32;
			velocity.y -= velocity.y / 32;
		}		
		velocity.scl(1/deltaTime);		
	}
	
	/**
	 * Apply a force to the ball
	 * @param speed
	 * @param angleDir
	 */
	public void applyForce(float speed, int angleDir) {
		float angle[] = {0, 45, 90, 135, 180, 225, 270, 315 }; //!Reimp
		
		if (angleDir >= 8)
		{
			//velocity.x = 0.0f;
			//velocity.y = 0.0f;
			return;
		}
		//System.out.format("angle %d%n", angleDir);
		
		// convert degrees to radians
		// libdgx rotation happens in a clockwise direction, but in mathematics it goes counterclockwise
		// to overcome differences add 90 degrees
		double radians = MathUtils.degRad * (90.0f - angle[angleDir]);
		
		float ballSpeed = speed;
		
		velocity.x = (float)(ballSpeed * Math.cos(radians));
		velocity.y = (float)(ballSpeed * Math.sin(radians));
		velocity.z = 80;
	}
	
	public void trap(Player player) { //!Reimp move to player class
		velocity = Vector3.Zero;
		owner = player;
	}
	
    public void dispose() {
		this.texture.dispose();
		this.shapeRenderer.dispose();
	}
}

